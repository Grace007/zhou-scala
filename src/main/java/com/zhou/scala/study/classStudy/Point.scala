package com.zhou.scala.study.classStudy

/**
  * @author eli
  * @date 2018/3/9 10:05
  *
  */
/**
  * Point里面有抽象方法就必须把类定义为abstract
  * @param xc
  * @param yc
  */
abstract class Point(xc:Int,yc:Int) {
  var x: Int = xc
  var y: Int = yc

  //定义辅助构造器
//  def  this(xc:Int,yc:Int,zc:Int){
//    this(xc,yc);
//    println("Point辅助构造器。。。")
//  }

  def move(dx: Int, dy: Int) {
    x = x + dx
    y = y + dy
    println("Point移动#####");
    println ("x 的坐标点: " + x);
    println ("y 的坐标点: " + y);
  }

  def abstractTest();
}


/**
  * 构造函数不用加override ,父类有抽象方法时，重写时override可加可不加
  * @param xc
  * @param yc
  * @param zc
  */
class Location( val xc:Int, val yc:Int,val zc:Int) extends Point(xc,yc){
  var z:Int =zc;

  def move(dx:Int,dy:Int,dz:Int): Unit ={
    x=x+dx;
    y=y+dy;
    z=z+dz;
    println("Location移动#####");
    println ("x 的坐标点: " + x);
    println ("y 的坐标点: " + y);
    println ("z 的坐标点: " + z);
  }

  def abstractTest(): Unit ={
    println("abstractTest是抽象方法的实现");
  }

  def this(){
    this(0,0,0);
    println("Location。。。")
  }




}

object Test_Point {
  def main(args: Array[String]) {
//    val pt = new Point(10, 20);
//    // 移到一个新的位置
//    pt.move(10, 10);

    val location = new Location(10,20,30);
    location.move(10,20,30);
    location.abstractTest();
    val location01 = new Location();
    location01.move(10,20,30);
  }
}

