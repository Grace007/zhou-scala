package com.zhou.scala.study.classStudy

import scala.collection.mutable.ArrayBuffer


/**
  * 在Scala的类中，与类名相同的对象叫做伴生对象，类和伴生对象之间可以相互访问私有的方法和属性
  * 在Scala中没有静态方法和静态字段，但是可以使用object这个语法结构来达到同样的目的
  * 1.存放工具方法和常量
  * 2.高效共享单个不可变的实例
  * 3.单例模式
  * @author eli
  * @date 2018/3/9 10:55
  *
  */
object SingletonDemo {
  def main(args: Array[String]) {
    //单例对象，不需要new，用【类名.方法】调用对象中的方法
    val session01 = SessionFactory.getSession()
    val session02 = SessionFactory.getSession()
    val session03 = SessionFactory.getSession()
    val session04 = SessionFactory.getSession()
    val session05 = SessionFactory.getSession()
    val sessionOnly01 = SessionFactoryOnly.getSession();
    val sessionOnly02 = SessionFactoryOnly.getSession();
    val sessionPrivate01 = SessionPrivate.getSessionPrivate();
    val sessionPrivate02 = SessionPrivate.getSessionPrivate();

    println(session01)
    println(session02)
    println(session03)
    println(session04)
    println(session05)
    println(sessionOnly01);
    println(sessionOnly02)
    println(sessionPrivate01)
    println(sessionPrivate02)

  }
}

/**
  * 有5个session的工厂
  */
object SessionFactory{
  //该部分相当于java中的静态块
  var counts = 5
  val sessions = new ArrayBuffer[Session]()
  while(counts > 0){
    sessions += new Session
    counts -= 1
  }

  //在object中的方法相当于java中的静态方法
  def getSession(): Session ={
    sessions.remove(0)
  }
}


/**
  * 工程伪单例模式
  */
object SessionFactoryOnly{
  val session =new Session;
  def getSession():Session = {
    session;
  }

}

/**
  * Session 为public 可以无限new, SessionFactory、SessionFactoryOnly都可以调用
  */
class Session{
}

/**
  * Session 为private
  * class SessionPrivate private声明了SessionPrivate的首构造函数是私有的，这样SessionPrivate的所有构造函数都不能直接被外部调用，因为所有从构造函数都会首先调用其他构造函数（可以是主构造函数，也可以是从构造函数），结果就是主构造函数是类的唯一入口点。
  */
class SessionPrivate private{

}

/**
  * 单例模式
  * 单例模式就控制类实例的个数，通过伴生对象来访问类的实例就提供了控制实例个数的机会。
  */
object  SessionPrivate {
  val sessionPrivate =new SessionPrivate;
  def getSessionPrivate():SessionPrivate={
    sessionPrivate;
  }

}

