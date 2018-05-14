package com.zhou.scala.study.actor

/**
  * @author eli
  * @date 2018/4/25 11:32
  *
  */
import java.io.File

import scala.actors.{Actor, Future}
import scala.collection.mutable
import scala.io.Source

/**
  * windows环境下的换行符是\r\n;
  * linux和html等开源或公开标准中的换行符是\n.
  * flat : 平坦
  * def flatMap[B](f: (A) => GenTraversableOnce[B]): Iterator[B]  :针对迭代器的序列中的每个元素应用函数f，并返回指向结果序列的迭代器。
  * def map[B](f: (A) => B): Iterator[B]  :将 it 中的每个元素传入函数 f 后的结果生成新的迭代器。
  * def toMap[T, U]: Map[T, U]  :将迭代器的所有键值对归入一个Map并返回。
  * def mapValues[U](f: (V) => U): RDD[(K, U)]  :同基本转换操作中的map，只不过mapValues是针对[K,V]中的V值进行map操作。
  * def groupBy[K](f: ((A, B)) ⇒ K): Map[K, HashMap[A, B]]  :解析groupBy(_._2) _是表示每一个元素  _2是元组元素的位置  总而言之,就是对List中第二列元素分组
  * def flatten[B]: immutable.HashMap[B]  :对集合进行平坦化
  * def foldRight[B](z: B)(op: ((A, B), B) ⇒ B): B    :B带有初始值的reduceLeft
  * def reduce(f: (T, T) ⇒ T): T RDD中的元素按顺序执行函数,并且结果作为参数
  * def reduceByKey(func: (V, V) => V): RDD[(K, V)] :
  */
class ActorWordCount01_Task extends Actor {
  override def act(): Unit = {
    loop {
      react {
        case ActorWordCount01_SubmitTask(fileName) => {
          println("开始执行task...")
          val contents = Source.fromFile(new File(fileName),"gb2312").mkString
          val arr = contents.split("\r\n")  //换行
          val result = arr.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.length)
          //val result = arr.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.foldLeft(0)(_ + _._2))
          sender ! ActorWordCount01_ResultTask(result)
        }
        case ActorWordCount01_StopTask => {
          exit()
        }
      }
    }
  }
}

object ActorWordCount01 {
  def main(args: Array[String]) {
    val files = Array("e://test//scala//wordcount.txt", "e://test//scala//wordcount.log")
    val replaySet = new mutable.HashSet[Future[Any]]  //
    val resultList = new mutable.ListBuffer[ActorWordCount01_ResultTask]

    for(f <- files) {
      val t = new ActorWordCount01_Task
      val replay = t.start() !! ActorWordCount01_SubmitTask(f)   //发送异步消息，返回值是 Future[Any]。
      replaySet += replay
    }

    while(replaySet.size > 0){
      val toCumpute = replaySet.filter(_.isSet)
      for(r <- toCumpute){
        val result = r.apply()
        resultList += result.asInstanceOf[ActorWordCount01_ResultTask]
        replaySet.remove(r)
      }
      Thread.sleep(100)
    }
    val finalResult = resultList.map(_.result).flatten.groupBy(_._1).mapValues(x => x.foldLeft(0)(_ + _._2))
    println(finalResult)
  }
}

case class ActorWordCount01_SubmitTask(fileName: String)
case object ActorWordCount01_StopTask
case class ActorWordCount01_ResultTask(result: Map[String, Int])
