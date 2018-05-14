package com.zhou.scala.study.base

/**
  * @author eli
  * @date 2018/3/9 12:06
  *
  * Scala 提取器是一个带有unapply方法的对象。unapply方法算是apply方法的反向操作：unapply接受一个对象，然后从对象中提取值，提取的值通常是用来构造该对象的值。
  *
  * 通过 apply 方法我们无需使用 new 操作就可以创建对象。所以你可以通过语句 Test("Zara", "gmail.com") 来构造一个字符串 "Zara@gmail.com"。
  * unapply方法算是apply方法的反向操作：unapply接受一个对象，然后从对象中提取值，提取的值通常是用来构造该对象的值。实例中我们使用 Unapply 方法从对象中提取用户名和邮件地址的后缀。
  */
object ExtractorStudy {
  def main(args: Array[String]) {

//    println ("Apply 方法 : " + apply("Zara", "gmail.com"));
//    println ("Unapply 方法 : " + unapply("Zara@gmail.com"));
//    println ("Unapply 方法 : " + unapply("Zara Ali"));


    val x = ExtractorStudy(5)
    println(x)

    x match
    {
      case ExtractorStudy(num) => println(x + " 是 " + num + " 的两倍！")
      //unapply 被调用
      case _ => println("无法计算")
    }

  }
//  // 注入方法 (可选)
//  def apply(user: String, domain: String) = {
//    user +"@"+ domain
//  }
//
//  // 提取方法（必选）
//  def unapply(str: String): Option[(String, String)] = {
//    val parts = str split "@"
//    if (parts.length == 2){
//      Some(parts(0), parts(1))
//    }else{
//      None
//    }
//  }


  /**
    * 在我们实例化一个类的时，可以带上0个或者多个的参数，编译器在实例化的时会调用 apply 方法。我们可以在类和对象中都定义 apply 方法。
    * @param x
    * @return
    */
  def apply(x: Int) = x*2

  /**
    * unapply 用于提取我们指定查找的值，它与 apply 的操作相反。 当我们在提取器对象中使用 match 语句是，unapply 将自动执行
    * @param z
    * @return
    */
  def unapply(z: Int): Option[Int] = if (z%2==0) Some(z/2) else None

}
