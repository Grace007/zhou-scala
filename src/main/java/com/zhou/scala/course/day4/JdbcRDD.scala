package com.zhou.scala.course.day4

/**
  * @author eli
  * @date 2018/5/7 18:29
  *
  */

import java.util.Properties
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.types.{StringType, IntegerType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

object JdbcRDD {
  def main(args: Array[String]) {



    val conf = new SparkConf().setAppName("MySQL-Demo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    //从mysql读取数据
    val jdbcDF = sqlContext.read.format("jdbc").options(Map("url" -> "jdbc:mysql://localhost:3306/bigdata", "driver" -> "com.mysql.jdbc.Driver", "dbtable" -> "person", "user" -> "root", "password" -> "root")).load()

    println(jdbcDF.collect().toBuffer)


    //通过并行化创建RDD
    val personRDD = sc.parallelize(Array("1 tom 5", "2 jerry 3", "3 kitty 6")).map(_.split(" "))
    //通过StructType直接指定每个字段的schema
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )
    //将RDD映射到rowRDD
    val rowRDD = personRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).toInt))
    //将schema信息应用到rowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "root")
    //将数据追加到数据库
    personDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/bigdata", "bigdata.person", prop)
    //停止SparkContext
    sc.stop()
  }
}
