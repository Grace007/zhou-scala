package com.zhou.scala.study.base

import java.io._

import scala.io.{Source}

/**
  * @author eli
  * @date 2018/3/9 12:18
  *
  */
object FileIOStudy {
  def main(args: Array[String]): Unit = {

    var readString = readFile("e:\\test\\scala\\","wordcount.log");
    println(readString);

//    createFile("e:\\test\\scala\\","wordcount_copy.log",readString);

//    input()

  }

  /**
    * 创建文件
    * @param path
    * @param name
    */
  def createFile(path:String,name:String,content:String ): Unit ={
    val file = new File(path,name);
    val writer =new PrintWriter(file);
    writer.write(content);
    writer.close();
  }

  def input(): Unit ={
    print("请输入：")
//    val line = StdIn.readLine();
//    println(line);

  }

  /**
    * 读取文件(注意编码问题,"gb2312"读取中文,"utf-8")
    * Exception in thread "main" java.nio.charset.MalformedInputException: Input length = 1
    * 编码不对
    * @param path
    * @param name
    * @return
    */
  def readFile(path: String, name: String): String = {
    val file = new File(path, name);
    var result = Source.fromFile(file,"gb2312").mkString;
    return result;

  }

}
