package com.zhou.scala.study.classStudy.person

import java.io.IOException

/**
  * 每个类都有主构造器，主构造器的参数直接放置类名后面，与类交织在一起
  * @author eli
  * @date 2018/4/24 12:27
  *
  */
class Student(val name: String, val age: Int){
  //主构造器会执行类定义中的所有语句
  println("执行主构造器")
  try {
    println("读取文件")
    throw new IOException("io exception")
  } catch {
    case e: NullPointerException => println("打印异常Exception : " + e)
    case e: IOException => println("打印异常Exception : " + e)
  } finally {
    println("执行finally部分")
  }

  private var gender = "male"

  //用this关键字定义辅助构造器
  def this(name: String, age: Int, gender: String){
    //每个辅助构造器必须以主构造器或其他的辅助构造器的调用开始
    this(name, age)
    println("执行辅助构造器")
    this.gender = gender
  }
}

object Student{
  def main(args: Array[String]): Unit = {
    val student = new Student("zhourunmin",24);
    println("###########")
    val student01 = new Student("zhourunwei",16,"man")
  }
}

