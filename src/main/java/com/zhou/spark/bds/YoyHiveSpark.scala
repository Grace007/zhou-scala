package com.zhou.spark.bds

import java.util.Properties

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types._
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * 出现内存溢出
  * Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
  * 解决方法: -XX:PermSize=1024M -XX:MaxPermSize=2048M
  *
  *  java.lang.IllegalAccessError: tried to access method com.google.common.base.Stopwatch.<init>
  *  问题原因：
一般来说这样的问题是因为 pom文件中有google的guava依赖吧？或者其他地方引用到了。 这个问题就是guava冲突了，版本不一致。所以大家优先去pom文件中排查。
解决办法：
比如2.3.1版本的elasticsearch在跟spark整合的过程中就会出现这个问题。我们可以注释掉elasticsearch的依赖，或者整合其他版本的。
  * @author eli
  * @date 2018/5/4 10:48
  *
  */
object YoyHiveSpark {
  def main(args: Array[String]): Unit = {



    val conf = new SparkConf().setAppName("YOY")
      .setMaster("local")
//      .setMaster("spark://hadoop01:7077")
//      .setJars(Array("F:\\HCR\\Java\\IdeaProjects\\zhou-scala\\target\\zhou-scala-1.0-SNAPSHOT.jar"));
    System.setProperty("user.name","root")
    val sc = new SparkContext(conf)
    val hsqlContext = new HiveContext(sc)

//    val temp = hsqlContext.sql("use test0502");

    val comment_RDD = hsqlContext.sql("select * from test0502.old_eleme_comment_info limit 10");
//    comment_RDD.show()

    comment_RDD.foreach(println)

    sc.stop();


  }

}
