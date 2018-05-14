package com.zhou.scala.study.base

/**
  * 函数式编程语言中，函数是“头等公民”，它可以像任何其他数据类型一样被传递和操作
  * 在Scala中方法不是值，而函数是。所以一个方法不能赋值给一个val变量，而函数可以。
  * @author eli
  * @date 2018/3/8 12:18
  *
  */
object FunctionStudy {
  def main(args: Array[String]): Unit = {
    //调用定义方法
    println("调用声明方法:"+declarationFuntion(2,3))
    //使用"_"将方法转换为函数
    val f1= declarationFuntion _
    println("调用'_'转换函数:"+f1(2,3));
    //调用高阶函数
    println("调用高阶函数:"+highFuntion(lowFuntion,3));
    //匿名函数
    var anonymousFunction=(x:Int) => x+1;
    println("调用匿名函数:"+anonymousFunction(3));
    //调用柯里化函数
    println("调用柯里化函数"+strcat("one")("two"));
    //调用闭包函数
    closure();
  }

  /**
    * 定义方法
    * @param x
    * @param y
    * @return
    */
  def declarationFuntion(x:Int,y:Int) : Int ={
    return x*y
  }

  /**
    * 低阶函数
    * @param temp
    * @tparam X
    * @return
    */
  def lowFuntion[X](temp:X) :String =
  {
    return temp.toString;
  }

  /**
    * 高阶函数
    * @param f
    * @param v
    * @return
    */
  def highFuntion(f:Int=>String,v:Int)=f(v);

  /**
    * 柯里化函数（原来接受两个参数的函数变成新的接受一个参数的函数的过程。新的函数返回一个以原有第二个参数为参数的函数。）  即把接受两个参数,例如 strcat(x,y) 变成 strcat01(x)和strcat02(y)的过程,简写为strcat(x)(y)
    * @param a
    * @param b
    * @return
    */
  def strcat(a:String)(b:String): String ={
    a+b;
  }

  /**
    * 闭包函数
    * 定义的函数变量 closureInternal 成为一个"闭包"，因为它引用到函数外面定义的变量，定义这个函数的过程是将这个自由变量捕获而构成一个封闭的函数。
    */
  def closure(): Unit ={

    var temp = 10;
    var closureInternal = (f:Int) => f*temp;
    println("闭包temp=10,f=1:"+closureInternal(1));
    println("闭包temp=10,f=2:"+closureInternal(2));
    temp=100;
    println("闭包temp=100,f=1:"+closureInternal(1));
    println("闭包temp=100,f=2:"+closureInternal(2));

  }


}
