package com.zhou.spark.rddApi

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author eli
  * @date 2018/5/4 11:51
  *
  */
object RDDApi {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDAPI").setMaster("local[3]");
    val sc = new SparkContext(conf)


//    aggregateByKeyDemo(sc);
//    aggregateDemo(sc);
    combineByKeyDemo(sc);
//    combineByKeyDemo01(sc)
//    combineByKeyDemo02(sc)
//    countByKeyDemo(sc)
//    filterByRangeDemo(sc)
//    flatMapValuesDemo(sc)
//    foldByKeyDemo(sc)
//    foreachPartitionDemo(sc)
  }

  /**
    * 实现mapPartitionsWithIndex的参数函数 需要实现f: (Int, Iterator[T]) => Iterator[U],
    * 把每个partition中的分区号和对应的值拿出来
    */
  val func_mapPartitionsWithIndex = (index: Int, iter: Iterator[(Int)]) => {
    iter.toList.map(x => "[partID:" +  index + ", val: " + x + "]").iterator
  }

  def func2(index: Int, iter: Iterator[(String, Int)]) : Iterator[String] = {
    iter.toList.map(x => "[partID:" +  index + ", val: " + x + "]").iterator
  }

  def aggregateDemo(sc : SparkContext) : Unit = {
    val rdd1 = sc.parallelize(List(1,2,3,4,5,6,7,8,9),2)
    val rdd101 = rdd1.mapPartitionsWithIndex(func_mapPartitionsWithIndex).collect()
        rdd101.foreach(println)
    val sum102 = rdd1.aggregate(0)(_+_,_+_) //45
        println(sum102)

    //类似map+reduce操作,map是并行化的,会导致结果不同(注意是string),其中map的操作也是类似队列那种思想的操作
    //第一个参数是初始值(对于每一个分区而言), 二:是2个函数[每个函数都是2个参数(第一个参数:先对个个分区进行合并, 第二个:对个个分区合并后的结果再进行合并)
    //其中还有一个坑:map端迭代的操作每次都是int转化成String,要注意
    val rdd2 = sc.parallelize(List("12","23","345","4567"),2)
    val result_201 = rdd2.aggregate("")((x,y) => math.max(x.length, y.length).toString ,(x,y) => x + y)
    //24 or 42  原因:"12","23"在同一个分区,"345","4567"在同一个分区 (分区按照了list的index顺序),第二个阶段(reduce合并) map是并行,所以使得两个数字顺序有所不同  如果有两个分区,则对应2个数字, 三个分区则对应三个数字
        println(result_201)

    val rdd4 = sc.parallelize(List("12","23","345",""),2)
    val result_401 =rdd4.aggregate("")((x,y) => math.min(x.length, y.length).toString, (x,y) => x + y)
    //01 or 10
        println(result_401)

    val rdd5 = sc.parallelize(List("12","23","","345"),2)
    val result_501 =rdd5.aggregate("")((x,y) => math.min(x.length, y.length).toString, (x,y) => x + y)
        println(result_501)


  }

  def aggregateByKeyDemo(sc : SparkContext) : Unit = {
    val rdd1 = sc.parallelize(List((1, 3), (1, 2), (1, 4), (2, 3)));
    val rdd2 = rdd1.aggregateByKey(10)(Math.max(_,_),_+_);
      rdd2.foreach(println);  //(2,3) (1,7)

    //分区的划分还是按照顺序,
    //aggregate针对一维,aggregateByKey可以操作二维
    val pairRDD = sc.parallelize(List( ("cat",4), ("cat", 5), ("mouse", 4),("cat", 12), ("dog", 12), ("mouse", 2)), 2)
    val result_pairRDD01 = pairRDD.mapPartitionsWithIndex(func2).collect();
      result_pairRDD01.foreach(println)
      println("##########")
    val result_pairRDD02 =  pairRDD.aggregateByKey(0)(math.max(_, _), _ + _).collect()
      result_pairRDD02.foreach(println)

    val result_pairRDD03 = pairRDD.aggregateByKey(100)(math.max(_, _), _ + _).collect()
      result_pairRDD03.foreach(println)
  }

  /**
    * combineByKey : 和reduceByKey是相同的效果
    * ###第一个参数x:原封不动取出来, 第二个参数:是函数, 局部运算, 第三个:是函数, 对局部运算后的结果再做运算
    * ###每个分区中每个key中value中的第一个值, (hello,1)(hello,1)(good,1)-->(hello(1,1),good(1))-->x就相当于hello的第一个1, good中的1
    * @param sc
    */
  def combineByKeyDemo(sc : SparkContext)  : Unit ={
    val rdd1 = sc.textFile("file:///E:\\Test\\bigdata\\spark\\wordcount.log").flatMap(_.split(" ")).map((_, 1))
    val rdd2 = rdd1.combineByKey(x => x, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
    val result_rdd1 = rdd1.collect
    val result_rdd2 = rdd2.collect

        result_rdd1.foreach(println)
        result_rdd2.foreach(println)

    //###当input下有3个文件时(有3个block块, 不是有3个文件就有3个block, ), 每个会多加3个10
    //本例子中有一个文件,原来有13个单词,但是结果显示是33,推测是有两个分区对应两个block,分区内各+10,结果就多加20.
    val rdd3 = rdd1.combineByKey(x => x + 10, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
    val result_rdd3=rdd3.collect()
//    result_rdd3.foreach(println)

    //将key相同的数据，放入一个集合中
    val rdd4 = sc.parallelize(List("dog","cat","gnu","salmon","rabbit","turkey","wolf","bear","bee"), 3)
    val rdd5 = sc.parallelize(List(1,1,2,2,2,1,2,2,2), 3)
    val rdd6 = rdd5.zip(rdd4)
    val rdd7 = rdd6.combineByKey(List(_), (x: List[String], y: String) => x :+ y, (m: List[String], n: List[String]) => m ++ n)
    rdd6.foreach(println)
    println("######")
    rdd7.foreach(println)

  }

  /**
    * 一个使用combineByKey来求解平均数
    *
  参数含义的解释
a 、score => (1, score)，我们把分数作为参数,并返回了附加的元组类型。 以"Fred"为列，当前其分数为88.0 =>(1,88.0)  1表示当前科目的计数器，此时只有一个科目

b、(c1: MVType, newScore) => (c1._1 + 1, c1._2 + newScore)，注意这里的c1就是createCombiner初始化得到的(1,88.0)。在一个分区内，我们又碰到了"Fred"的一个新的分数91.0。当然我们要把之前的科目分数和当前的分数加起来即c1._2 + newScore,然后把科目计算器加1即c1._1 + 1

c、 (c1: MVType, c2: MVType) => (c1._1 + c2._1, c1._2 + c2._2)，注意"Fred"可能是个学霸,他选修的科目可能过多而分散在不同的分区中。所有的分区都进行mergeValue后,接下来就是对分区间进行合并了,分区间科目数和科目数相加分数和分数相加就得到了总分和总科目数
    * @param sc
    */
  def combineByKeyDemo01(sc : SparkContext) :Unit ={
    val initialScores = Array(("Fred", 88.0), ("Fred", 95.0), ("Fred", 91.0), ("Wilma", 93.0), ("Wilma", 95.0), ("Wilma", 98.0))
    val d1 = sc.parallelize(initialScores)
    type MVType = (Int, Double) //定义一个元组类型(科目计数器,分数)
    val result = d1.combineByKey(
      score => (1, score),
      (c1: MVType, newScore) => (c1._1 + 1, c1._2 + newScore),
      (c1: MVType, c2: MVType) => (c1._1 + c2._1, c1._2 + c2._2)
    ).map { case (name, (num, socre)) => (name, socre / num) }.collect

    result.foreach(println)
  }

  /**
1.在createScoreCombiner采取双重价值，并返回一个元组（智力，双人间）
2.该scoreCombiner函数采用一个ScoreCollector它是一个类型别名为（中间体，双）的元组。我们将元组的值与alias的值相混淆，numberScores并totalScore为可读性添加一行代码。我们将得分数量增加1，并将当前得分添加到迄今收到的总得分中。
3.该scoreMerger函数有两个ScoreCollector小号增加得分，并在新的元组一起返回总分的总数。
4.然后我们调用combineByKey通过我们以前定义的函数的函数。
5.我们将得到的RDD，分数和collectAsMap函数调用以（name，（numberScores，totalScore））的形式得到我们的结果。
6.为了得到我们的最终结果，我们调用mapRDD得分传递函数，averagingFunction它简单地计算平均得分并返回一个元组（名称，averageScore）
    * @param sc
    */
  def combineByKeyDemo02(sc : SparkContext) :Unit = {
    //type alias for tuples, increases readablity
    type ScoreCollector = (Int, Double)
    type PersonScores = (String, (Int, Double))

    val initialScores = Array(("Fred", 88.0), ("Fred", 95.0), ("Fred", 91.0), ("Wilma", 93.0), ("Wilma", 95.0), ("Wilma", 98.0))

    val wilmaAndFredScores = sc.parallelize(initialScores).cache()

    val createScoreCombiner = (score: Double) => (1, score)

    val scoreCombiner = (collector: ScoreCollector, score: Double) => {
      val (numberScores, totalScore) = collector
      (numberScores + 1, totalScore + score)
    }

    val scoreMerger = (collector1: ScoreCollector, collector2: ScoreCollector) => {
      val (numScores1, totalScore1) = collector1
      val (numScores2, totalScore2) = collector2
      (numScores1 + numScores2, totalScore1 + totalScore2)
    }
    val scores = wilmaAndFredScores.combineByKey(createScoreCombiner, scoreCombiner, scoreMerger)

    val averagingFunction = (personScore: PersonScores) => {
      val (name, (numberScores, totalScore)) = personScore
      (name, totalScore / numberScores)
    }

    val averageScores = scores.collectAsMap().map(averagingFunction)

    println("Average Scores using CombingByKey")
    averageScores.foreach((ps) => {
      val(name,average) = ps
      println(name+ "'s average score : " + average)
    })
  }

  def countByKeyDemo(sc : SparkContext) : Unit ={
    val rdd1 = sc.parallelize(List(("a", 1), ("b", 2), ("b", 2), ("c", 2), ("c", 1)))
    rdd1.countByKey.foreach(println)
    rdd1.countByValue.foreach(println)

  }

  def filterByRangeDemo(sc : SparkContext) :Unit = {
    val rdd1 = sc.parallelize(List(("e", 5), ("c", 3), ("d", 4), ("c", 2), ("a", 1)))
    val rdd2 = rdd1.filterByRange("b", "d")
    rdd2.collect.foreach(println)
  }

  def flatMapValuesDemo(sc : SparkContext) : Unit = {
    val rdd3 = sc.parallelize(List(("a", "1 2"), ("b", "3 4")))
    val rdd4 = rdd3.flatMapValues(_.split(" "))
    rdd4.collect.foreach(println)
  }

  def foldByKeyDemo(sc : SparkContext) : Unit = {
    val rdd1 = sc.parallelize(List("dog", "wolf", "cat", "bear"), 2)
    val rdd2 = rdd1.map(x => (x.length, x))
    val rdd3 = rdd2.foldByKey("")(_+_)
    rdd3.foreach(println)
  }

  /**
    * 不会返回新的RDD,对当前的RDD进行操作
    * @param sc
    */
  def foreachPartitionDemo(sc :SparkContext) :Unit ={
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    rdd1.foreachPartition(x => println(x.reduce(_ + _)))

  }

}
