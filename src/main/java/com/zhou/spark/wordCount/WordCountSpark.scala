package com.zhou.spark.wordCount

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author eli
  * @date 2018/5/3 9:57
  *
  */
object WordCountSpark {
//  def main(args: Array[String]): Unit = {
////    val inputPath = args(0)
////    val outputPath = args(1)
//    val inputPath="hdfs://hadoop01:8020/user/admin/spark/input/zhou-scala-data.txt"
//    val outputPath="hdfs://hadoop01:8020/user/admin/spark/output";
//    val conf = new SparkConf().setAppName("WC")
//    val sc = new SparkContext(conf)
//    sc.textFile(inputPath).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).sortBy(_._2,false).saveAsTextFile(outputPath)
//    sc.stop()
//  }

  def main(args: Array[String]): Unit = {
    val inputPath = "file:///E:\\Test\\bigdata\\spark\\wordcount.log"
    val outputPath = "E:\\Test\\bigdata\\spark\\wordcount"
    val conf = new SparkConf().setAppName("WC")
    val sc = new SparkContext(conf)
    val result = sc.textFile(inputPath).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).sortBy(_._2, false).saveAsTextFile(outputPath)
    println(result)
    sc.stop()
  }

}
