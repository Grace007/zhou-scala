package com.zhou.spark.bds

import java.util.Properties

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types._
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * collect：Actions算子。相当于toArrays,将分布式的RDD返回为一个单机的scala Array数组
  * -Dspark.master=local
  * 版本不一致会报错,序列化报错, 把把linux和本地的spark\hadoop\scala\java版本调到一致, 但是现在仍然没有解决,尴尬了
  * WARN AppClient$ClientEndpoint: Failed to connect to master
  * hadoop01:7077
  * 原因是所引用的jar和spark版本不一致。进入spark-shell，看spark和scala的版本
  * @author eli
  * @date 2018/5/4 10:48
  *
  */
object YoySpark {
  def main(args: Array[String]): Unit = {

//    val inputPath = "file:///E:\\Test\\bigdata\\spark\\YOY_hive_export.txt"
//    val inputPath = "hdfs://hadoop01:8020/user/admin/hive_export/0503_hive_IYA/file"
//    val outputPath = "E:\\Test\\bigdata\\spark\\YOY_hive_export"
    val inputPath = "hdfs://hadoop01:8020/user/admin/hive_export/0503_hive_IYA/file"
    val  outputPath = "hdfs://hadoop01:8020/user/admin/hive_export/0503_hive_IYA/output"
    type MVType = ((Int, Double), Double)
    type oldType = (Int, Double)
    val conf = new SparkConf().setAppName("YOY")
//      .setMaster("local")
      .setMaster("spark://hadoop01:7077")
//      .setJars(Array("F:\\HCR\\Java\\IdeaProjects\\zhou-scala\\target\\zhou-scala-1.0-SNAPSHOT.jar"));
    System.setProperty("user.name","root")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val array_01 = sc.textFile(inputPath).flatMap(_.split("\n")).map(_.split("\t")) ;
    val array_02 = array_01.map(x => (x(0),(x(1),x(2),x(3))))

    //获取店铺id的list,方便对shopid进行分区
    val ints = array_02.map(_._1).distinct().collect()
    val hostParitioner = new HostParitioner(ints)


    //对年份排序
    val array_03 = array_02.partitionBy(hostParitioner).mapPartitions(f = it => {

      val rdd01_partitions = it.toList.sortBy(_._2._1).reverse.iterator
      var list_partitons = rdd01_partitions.toList
      val result = new ListBuffer[String]()
      for (list_one <- list_partitons) {
        val year = list_one._2._1.toInt
        val last_year = year - 1
        var growth_rate = "null"
        for (list_two <- list_partitons) {
          val year_02 = list_two._2._1.toInt
          if (year_02 == last_year) {
            growth_rate = (((list_one._2._2.toDouble - list_two._2._2.toDouble) / list_two._2._2.toDouble) * 100).toString
          }
        }
        val result_01 = new Tuple4(list_one._1, year,list_one._2._2, growth_rate)

        result.append(result_01.toString())
      }

      result.iterator
    })

    val array_04 = array_03.repartition(1)
    println("######################")
    println(array_04.collect().toBuffer)
//    array_04.saveAsTextFile(outputPath)

    //开始向mysql输出
    //通过StructType直接指定每个字段的schema
    val schema = StructType(
      List(
        StructField("shop_id", StringType, true),
        StructField("year", IntegerType, true),
        StructField("score", DoubleType, true),
        StructField("yoy", StringType, true)
      )
    )
    //将RDD映射到rowRDD
    val rowRDD = array_04.map(p => {
      val str_temp = p.substring(1,p.length-1)
      val str_list = str_temp.split(",")
      Row(str_list(0), str_list(1).toInt, str_list(2).toDouble,str_list(3))
    }
//      Row(p(0).toString, p(1).toInt, p(2).toDouble,p(3).toString)
      )
    println(rowRDD.collect().toBuffer)
    //将schema信息应用到rowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    personDataFrame.show()
    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "root")
    //将数据追加到数据库
//    personDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/bigdata", "bigdata.YOY_table", prop)

    sc.stop();


  }

}

/**
  * 决定了数据到哪个分区里面
  * @param ins
  */
class HostParitioner(ins: Array[String]) extends Partitioner {

  val parMap = new mutable.HashMap[String, Int]()
  var count = 0
  for(i <- ins){
    parMap += (i -> count)
    count += 1
  }

  override def numPartitions: Int = ins.length

  override def getPartition(key: Any): Int = {
    parMap.getOrElse(key.toString, 0)
  }
}


/*
    打印RDD中的所有元素的通常作法是使用rdd.foreach(println)
    或者 rdd.map(println).在单一的机器上，这样做会产生期望的结果。
    然而，在集群模式下，输出将会在每个执行体的标准输出上，而不是在驱动节点上，
    所以驱动节点的标准输出将不会有结果。为了在驱动节点上打印所有元素，
    可以先使用collect方法将RDD带到驱动节点上，rdd.collect().foreach(println)。
    这样做可能会造成驱动节点内存溢出，因为collect方法将整个RDD收集到一台机器上。
    如果你只是想打印RDD的部分元素，可以使用较为安全的做法:rdd.take(100).foreach(println)
    */