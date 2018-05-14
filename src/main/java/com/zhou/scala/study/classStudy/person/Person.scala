package com.zhou.scala.study.classStudy.person

/**
  * 在Scala中，类并不用声明为public。
  * Scala源文件中可以包含多个类，所有这些类都具有公有可见性。
  * @author eli
  * @date 2018/4/24 12:26
  *
  */
class Person {
  //用val修饰的变量是只读属性，有getter但没有setter
  //（相当与Java中用final修饰的变量）
  val id = "9527"

  //用var修饰的变量既有getter又有setter
  var age: Int = 18

  //类私有字段,只能在类的内部使用
  private var name: String = "唐伯虎"

  //对象私有字段,访问权限更加严格的，Person类的方法只能访问到当前对象的字段
  private[this] val pet = "小强"
}

/**
  * Scala	Java 相当于
obj.isInstanceOf[C]	obj instanceof C
obj.asInstanceOf[C]	(C)obj
classOf[C]	C.class

  */
object Person{
  def main(args: Array[String]): Unit = {

    val person = new Person();
    //类类型检查 相当于java的obj instanceof C
    println(person.isInstanceOf[Person]);
    //类类型强制转换成Person  相当于java的(C)obj
    println(person.asInstanceOf[Person]);
    //得到类   相当于java的.class
    println(classOf[Person]);

  }
}

