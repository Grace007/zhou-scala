package com.zhou.scala.study.base

/**
  * @author eli
  * @date 2018/3/9 11:54
  *
  * 使用 String 类的 r() 方法构造了一个Regex对象。
  * 然后使用 findFirstIn 方法找到首个匹配项。
  * 如果需要查看所有的匹配项可以使用 findAllIn 方法。
  * mkString( ) 方法来连接正则表达式匹配结果的字符串，并可以使用管道(|)来设置不同的模式
  */
object RegexStudy {
  def main(args: Array[String]) {
    val pattern = "Scala".r
    val str = "Scala is Scalable and cool Scala."
    println((pattern findAllIn  str).mkString(","))


  }

}
