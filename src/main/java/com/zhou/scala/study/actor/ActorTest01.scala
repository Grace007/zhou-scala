package com.zhou.scala.study.actor

import scala.actors.Actor

/**
  *
  * !	  发送异步消息，没有返回值。
  * !?	发送同步消息，等待返回值。
  * !!	发送异步消息，返回值是 Future[Any]。
  * @author eli
  * @date 2018/3/9 13:57
  *
  */
object ActorTest01 {

  def main(args: Array[String]): Unit = {
//    //测试actor并行执行
//    myActor01.start();
//    myActor02.start();

//    //测试actor接受消息
//    myActor03.start();
//    myActor03 ! "start"
//    myActor03 ! "stop"

//        myActor04.start();
//        myActor04 ! "start"
//        myActor04 ! "stop"

//    //测试类继承方式的actor接受消息
//    var myActor04 = new MyActor04_class();
//        myActor04.start();
//        myActor04 ! "start"
//        myActor04 ! "stop"

    //测试MyActor05_class 结合case class发送消息
    val a = new MyActor05_class
    a.start()
    //异步消息
    a ! AsyncMsg(1, "hello actor")
    println("异步消息发送完成")
    //同步消息
    val content = a !? (1000, SyncMsg(2, "hello actor"))
    println("content:"+content)
    println("同步消息发送完成")
    val reply = a !! SyncMsg(2, "hello actor")
    println(reply.isSet)
    //println("123")
    val c = reply.apply()
    println(reply.isSet)
    println(c)


  }



}

/**
  * 这两个Actor是并行执行的，act()方法中的for循环执行完成后actor程序就退出了
  */
object myActor01 extends  Actor {
  override def act(): Unit = {
    for (i <- 1 until  10 ){
      println("actor-1 " + i)
      Thread.sleep(2000)

    }
  }
}

/**
  * 这两个Actor是并行执行的，act()方法中的for循环执行完成后actor程序就退出了
  */
object myActor02 extends  Actor {
  override def act(): Unit = {
    for (i <- 1 until  10 ){
      println("actor-2 " + i)
      Thread.sleep(2000)

    }
  }
}

/**
  * 说明：在act()方法中加入了while (true) 循环，就可以不停的接收消息
注意：发送start消息和stop的消息是异步的，但是Actor接收到消息执行的过程是同步的按顺序执行
  */
object myActor03 extends Actor{
  def act():Unit ={
    while (true){
      receive{
        case "start" => {
          println("starting ...")
          Thread.sleep(5000)
          println("started")
        }
        case "stop" => {
          println("stopping ...")
          Thread.sleep(5000)
          println("stopped ...")
        }
      }
    }
  }
}

/**
  * 类继承
  * react方式会复用线程，比receive更高效
  * react 如果要反复执行消息处理，react外层要用loop，不能用while
  */
class MyActor04_class extends Actor {

  override def act(): Unit = {
    loop {
      react {
        case "start" => {
          println("starting ...")
          Thread.sleep(5000)
          println("started")
        }
        case "stop" => {
          println("stopping ...")
          Thread.sleep(8000)
          println("stopped ...")
        }
      }
    }
  }
}


/**
  * 结合case class发送消息
  */
class MyActor05_class extends Actor{
  override def act(): Unit = {
    while (true) {
      receive {
        case "start" => println("starting ...")
        case SyncMsg(id, msg) => {
          println(id + ",sync " + msg)
          Thread.sleep(5000)
          sender ! ReplyMsg(3,"finished")
        }
        case AsyncMsg(id, msg) => {
          println(id + ",async " + msg)
          Thread.sleep(5000)
        }
      }
    }
  }
}

//样例类
case class SyncMsg(id : Int, msg: String)
case class AsyncMsg(id : Int, msg: String)
case class ReplyMsg(id : Int, msg: String)



