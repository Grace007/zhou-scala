package com.zhou.scala.study

import java.util.concurrent.{Callable, Executors, Future}

/**
  * @author eli
  * @date 2018/4/25 18:11
  *
  */
object ThreadDemo {
  def main(args: Array[String]) {
    val pool = Executors.newFixedThreadPool(5)
//    for (i <- 1 to 10) {
//      pool.execute(new Runnable {
//        def run(): Unit = {
//          println(Thread.currentThread().getName)
//          Thread.sleep(1000)
//        }
//      })
//    }

    //callable 和 Runable都是启动一个线程, 不过Callable可以有返回值
    val f: Future[Int] = pool.submit(new Callable[Int] {
      override def call(): Int = {
        Thread.sleep(1000)
        100
      }
    })
    var status = f.isDone
    println(s"task status $status")

    Thread.sleep(2000)

    status =  f.isDone
    println(s"task status $status")

    if (status){
      println(f.get())
    }


  }

}
