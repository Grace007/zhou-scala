package com.zhou.scala.study.base

import scala.collection.mutable
/**
  * @author eli
  * @date 2018/4/24 10:21
  *
  */
object Grammar01 {

  def main(args: Array[String]): Unit = {
//    BlockExpressionDemo();
//    ForDemo();
//    groupByDemo();
    mapValueDemo();
//    foldDemo()


  }


  /**
    * 块表达式
    */
  def BlockExpressionDemo() : Unit ={
    val x = 0;
    val result = {
      if (x==0) {
        -1
      }
      else {
        2
      }
    }
    println(result);
  }

  /**
    * for循环
    * i to j : i到j(包含j)
    * i until j : i到j(不包含j)
    */
  def ForDemo() : Unit = {

    //for(i <- 表达式),表达式1 to 10返回一个Range（区间）
    //每次循环将区间中的一个值赋给i
    for (i <- 1 to 10){
      println(i)
    }

    //for(i <- 数组)
    println("############")
    var array =Array("A","B","C");
    for (i <- array){
      println(i)
    }
    println("############")


    //高级for循环
    //每个生成器都可以带一个条件，注意：if前面没有分号
    for ( i<- 1 to 3;j<- 4 to 6 if (j!=4)){
      println(i+" "+j)
    }
    println("############")

    //for推导式：如果for循环的循环体以yield开始，则该循环会构建出一个集合
    //每次迭代生成集合中的一个值
    val v= {
      for (i<- 1 to 10) yield i*10;
    }
    println(v)
  }

  def groupByDemo() : Unit ={
    val data = List(("Han","Male"),("XSDYM","Femail"),("Mr.Wang","Male"))
    //解析groupBy(_._2) _是表示每一个元素  _2是元组元素的位置
    //总而言之,就是对List中第二列元素分组
    val group1 = data.groupBy(_._2) // = Map("Male" -> List(("HomeWay","Male"),("Mr.Wang","Male")),"Female" -> List(("XSDYM","Femail")))
    val group2 = data.groupBy{case (name,sex) => sex} // = Map("Male" -> List(("HomeWay","Male"),("Mr.Wang","Male")),"Female" -> List(("XSDYM","Femail")))
    val fixSizeGroup = data.grouped(2).toList // = Map("Male" -> List(("HomeWay","Male"),("XSDYM","Femail")),"Female" -> List(("Mr.Wang","Male")))

    println(group1)
    println(group2)
    println(fixSizeGroup)

  }

  def mapValueDemo() : Unit ={
    val a = List("dog", "tiger", "lion", "cat", "panther", " eagle");
    val b = a.map(x => (x.length, x)).toMap  //key：元组的长度 value：元组的值
    println(a)
    println(b);
    val c= b.mapValues("{"+_+"}")

    println(c)

  }

  /**
    * 从本质上说，fold函数将一种格式的输入数据转化成另外一种格式返回。fold, foldLeft和foldRight这三个函数除了有一点点不同外，做的事情差不多。
    * List中的fold方法需要输入两个参数：初始值以及一个函数。输入的函数也需要输入两个参数,
    *
    求和代码开始运行的时候，初始值0作为第一个参数传进到fold函数中，list中的第一个item作为第二个参数传进fold函数中。
　　1、fold函数开始对传进的两个参数进行计算，在本例中，仅仅是做加法计算，然后返回计算的值；
　　2、Fold函数然后将上一步返回的值作为输入函数的第一个参数，并且把list中的下一个item作为第二个参数传进继续计算，同样返回计算的值；
　　3、第2步将重复计算，直到list中的所有元素都被遍历之后，返回最后的计算值，整个过程结束；

    */
  def foldDemo() : Unit = {
    val listA = List(1,2,3)

    /**
      *foldLeft定义:
      * def foldLeft[B](z: B)(op: (B, A) => B): B = {
      *   var result = z
      *   this.seq foreach (x => result = op(result, x))
      *   result
      * }
      */
    //foldLeft(0)((sum,i)=>sum+i) 解析, 0是接受参数的类型B,op是一个函数
    val a =listA.foldLeft(0)((sum,i)=>sum+i);
    println(a)

  }


}
