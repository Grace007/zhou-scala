package com.zhou.scala.study.classStudy

/**
  * @author eli
  * @date 2018/3/9 11:51
  *
  * Scala 提供了强大的模式匹配机制，应用也非常广泛。
  * 一个模式匹配包含了一系列备选项，每个都开始于关键字 case。每个备选项都包含了一个模式及一到多个表达式。箭头符号 => 隔开了模式和表达式。
  */
object CaseClass {
  def main(args: Array[String]) {
    val alice = new Person("Alice", 25)
    val bob = new Person("Bob", 32)
    val charlie = new Person("Charlie", 32)

    for (person <- List(alice, bob, charlie,"1")) {
      person match {
        case Person("Alice", 25) => println("Hi Alice!")
        case Person("Bob", 32) => println("Hi Bob!")
        case Person(name, age) =>
          println("Age: " + age + " year, name: " + name + "?")
        case _ => throw new Exception("not match exception")
      }
    }
  }
  // 样例类
  case class Person(name: String, age: Int)
}
