package com.zhou.scala.study.classStudy

/**
  * @author eli
  * @date 2018/3/9 11:34
  * Scala Trait(特征) 相当于 Java 的接口，实际上它比接口还功能强大。
  * 与接口不同的是，它还可以定义属性和方法的实现。
  * 一般情况下Scala的类只能够继承单一父类，但是如果是 Trait(特征) 的话就可以继承多个，从结果来看就是实现了多重继承。*Trait(特征) 定义的方式与类类似，但它使用的关键字是 trait
  *其实 Scala Trait(特征)更像 Java 的抽象类。
  *
  * 在Scala中，Trait是一种特殊概念。首先，Trait可以被作为接口来使用，此时Trait与Java的接口非常类似。同时在Trait可以定义抽象方法，其与抽象类中的抽象方法一样，不给出方法的具体实现。
  * 注意：类使用extends继承Trait，与Java不同，这里不是implement，在Scala中，无论继承类还是继承Trait都是用extends关键字。

  * 在Scala中，类继承Trait后，必须实现其中的抽象方法，实现时不需要使用override关键字，同时Scala同Java一样，不支持类多继承，但支持多重继承Trait，使用with关键字即可。
  */

/**
  * 判断两个对象是否相等，PointTrait的实现：判断对象是否是PointTrait的实例并且x、y相等。
  */
trait Equal {
  def isEqual(x:Any): Boolean
  def isNotEqual(x:Any):Boolean = !isEqual(x);
}

class PointTrait(xc: Int, yc: Int) extends Equal {
  var x: Int = xc
  var y: Int = yc
  def isEqual(obj: Any) =
    obj.isInstanceOf[PointTrait] &&
      obj.asInstanceOf[PointTrait].x == x &&
      obj.asInstanceOf[PointTrait].y == y
}

object Test_Equal {
  def main(args: Array[String]) {
    val p1 = new PointTrait(2, 3)
    val p2 = new PointTrait(2, 4)
    val p3 = new PointTrait(3, 3)
    val p4 = new PointTrait(3, 3)

    println(p1.isEqual(p2))
    println(p1.isEqual(p3))
    println(p3.isEqual(p4))

    println(p1.isNotEqual(p2))
    println(p1.isNotEqual(p3))
    println(p3.isNotEqual(p4))
  }
}


