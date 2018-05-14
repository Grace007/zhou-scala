package com.zhou.scala.test

/**
  * @author eli
  * @date 2018/2/28 16:38
  *
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {

    for (i <- 1 to 3;j <- 1 to 10){
      println(i+" "+j)

    }

    def add(a:Int,b:Int): Int={

      val c=a+b;
      return c;
    }

    println(add(3,5));

    /**
      * 递归阶乘
      * @param num
      * @return
      */
    def factorial(num: Int): Int = {
      if (num <= 1) {
        1
      } else {
        num * factorial(num - 1)
      }
    }
    var list = List(1,2,3,4,5);
    for (i <- list){
      println(i+"的阶乘是："+factorial(i));
    }




  }

}
