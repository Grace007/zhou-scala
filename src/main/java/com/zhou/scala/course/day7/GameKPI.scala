package com.zhou.scala.course.day7

import com.zhou.scala.course.day7.util.{EventType, FilterUtils, TimeUtils}
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  * 数据格式
  * 数据事件类型(第几次登录)|时间|ip地址|name|职业|性别|
  * 1|2016年2月1日,星期一,10:01:08|10.51.4.168|李明克星|法师|男|1|0|0/800000000
  * 1|2016年2月1日,星期一,10:01:12|10.117.45.20|风道|道士|男|1|0|0/800000000
  *
  */
object GameKPI {

  def main(args: Array[String]) {

    val queryTime = "2016-02-02 00:00:00"
    val beginTime = TimeUtils(queryTime)
    val endTime = TimeUtils.getCertainDayTime(+1)
    val conf = new SparkConf().setAppName("GameKPI").setMaster("local[*]")
    val sc = new SparkContext(conf)


    //切分之后的数据
    val splitedLogs = sc.textFile("E:\\Test\\bigdata\\spark\\game\\GameLog.txt").map(_.split("\\|"))
    //过滤后并缓冲,间隔一天的数据
    val filteredLogs = splitedLogs.filter(fields => FilterUtils.filterByTime(fields, beginTime, endTime))
      .cache()

    //日新增用户数，Daily New Users 缩写 DNU
    val dnu = filteredLogs.filter(fields => FilterUtils.filterByType(fields, EventType.REGISTER))
      .count()

    //日活跃用户数 DAU （Daily Active Users）
    val dau = filteredLogs.filter(fields => FilterUtils.filterByTypes(fields, EventType.REGISTER, EventType.LOGIN))
      .map(_ (3))
      .distinct()
      .count()

    //  留存率：某段时间的新增用户数记为A，经过一段时间后，仍然使用的用户占新增用户A的比例即为留存率
    //  次日留存率（Day 1 Retention Ratio） Retention [rɪ'tenʃ(ə)n] Ratio ['reɪʃɪəʊ]
    //  日新增用户在+1日登陆的用户占新增用户的比例
    val t1 = TimeUtils.getCertainDayTime(-1)  //当前日期-1
    val lastDayRegUser = splitedLogs.filter(fields => FilterUtils.filterByTypeAndTime(fields, EventType.REGISTER, t1, beginTime))
      .map(x => (x(3), 1))
    val todayLoginUser = filteredLogs.filter(fields => FilterUtils.filterByType(fields, EventType.LOGIN))
      .map(x => (x(3), 1))
      .distinct()
    //(name,1)

    val d1r: Double = lastDayRegUser.join(todayLoginUser).count()
    println(d1r)
    val d1rr = d1r / lastDayRegUser.count()
    println(d1rr)


    sc.stop()
  }
}
// create table GameKPI (id, gamename, zone, datetime, dnu, dau, d1rr, d7rr ... )