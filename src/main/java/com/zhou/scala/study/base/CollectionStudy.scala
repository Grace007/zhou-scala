package com.zhou.scala.study.base

//
import scala.Array._
import scala.collection.mutable.{ArrayBuffer, Map, Set}

/** mutable: 可改变的
  * @author eli
  * @date 2018/3/8 14:11
  *
  */
object CollectionStudy {

  def main(args: Array[String]): Unit = {

//    listForLoopSetGet();

//    twoDimensionalArray();

//    conllectionArrayBuffer();

//    conllectionList();

    collectionSet();

//    collectionMap();

//    collectionTuple();

//    collectionOption();

  }

  /**
    * Array定长数组的循环
    */
  def listForLoopSetGet(): Unit ={
    var arrayList01 =new Array[String](3);
    //循环赋值
    for (i <- 0 until arrayList01.length){
      arrayList01(i)="##"+i;
    }
    //循环打印
    for (i <- arrayList01){
      println(i);
    }
    println("###################");
    //定义一个数组
    val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
    //将偶数取出乘以10后再生成一个新的数组
    val res = for (e <- arr if e % 2 == 0) yield e * 10
    println(res.toBuffer)

    //更高级的写法,用着更爽 _代表每一个元素
    //filter是过滤，接收一个返回值为boolean的函数
    //map相当于将数组中的每一个元素取出来，应用传进去的函数
    val r = arr.filter(_ % 2 == 0).map(_ * 10)
    println(r.toBuffer)

  }

  /**
    * 矩阵的赋值和打印
    */
  def twoDimensionalArray(): Unit ={
    var twoDimensional =Array.ofDim[String](3,3); //Array[Array[String]]也可以
    //var twoDimensional = Array[Array[String]]() //赋值报错,可能需要初始化
    //给矩阵赋值
    for (i <- 0 until 3; j <- 0 until 3){
      twoDimensional(i)(j)="["+(i+1)+"]["+(j+1)+"]";
    }
//    for (i <- 0 until 3){
//      for (j <- 0 until 3){
//        twoDimensional(i)(j)=j;
//      }
//    }
    //打印矩阵
    for (i <- 0 until 3) {
      for (j <- 0 until 3) {
        print("twoDimensional[" + i + "][" + j + "]=" + twoDimensional(i)(j) + "  ");
      }
      println();
    }
  }

  /**
    * 可变list
    */
  def conllectionArrayBuffer():Unit = {
    val list01= ArrayBuffer[Int]();
    list01 += 1
    //追加多个元素
    list01 += (2, 3, 4, 5)
    //追加一个数组++=
    list01 ++= Array(6, 7)
    //追加一个数组缓冲
    list01 ++= ArrayBuffer(8,9)
    //打印数组缓冲ab
    println(list01)
    //在数组某个位置插入元素用insert
    list01.insert(0, -1, 0)
    println(list01)
    //删除数组某个位置的元素用remove
    list01.remove(8, 2)
    println(list01)

    for (i <- 0 until list01.size){
      println(i);
    }
  }

  /**
    * 集合List操作
    * List(X,X,X)是不可变的,ListBuffer[Int]可以改变
    * :: 操作符是右结合的，如9 :: 5 :: 2 :: Nil相当于 9 :: (5 :: (2 :: Nil))
    */
  def conllectionList(): Unit ={
    var list01:List[String] = List("谢","谢","你");
    var list02 = List("1","2","3");


    for (i <- list01){
      print(i);
    }
    //添加数据
    list01 = list01.+:("逗逼");
    list01 = list01.::("前面");
    println()
    for (i <- list01){
      print(i);
    }

    println()
    println(list01.getClass.getName)
  }

  /**
    * 集合Set操作
    * 虽然可变Set和不可变Set都有添加或删除元素的操作，但是有一个非常大的差别。对不可变Set进行操作，会产生一个新的set，原来的set并没有改变，这与List一样。 而对可变Set进行操作，改变的是该Set本身，与ListBuffer类似。
    */
  def collectionSet(): Unit ={
    var set01=Set("周","润","民");
    println(set01);
    //添加
    set01.add("ok");
    set01+="无敌";
    println(set01);
    //删除
    set01.remove("ok");
    println(set01);
    //最值
    println(set01.min);
    println(set01.max);

  }

  /**
    * 集合Map操作
    */
  def collectionMap(): Unit ={
    var map01 = Map("red"->"红");
    map01.put("white","白");
    map01 += ("black"->"黑");
    println(map01);
    println(map01.keys);
    println(map01.values);

    //循环输出map中的键值对
    map01.keys.foreach { i =>
      print("[" + i + ",")
      println(map01(i) + "]")
    }

    //获得value,如果没有对应的key,则返回给的默认值
    println(map01.getOrElse("white01","null"))

    //修改value
    map01("red")="红色";
    println(map01);

  }

  def collectionTuple(): Unit ={
    var tuple01 = new Tuple3(1,"one","一");
    println(tuple01._1);
    println(tuple01._2);
    println(tuple01._3);

    //遍历元组
    tuple01.productIterator.foreach{
      i =>
        println(i)
    }
    //拉链操作
    var name=Array("zhou","run","min");
    var value = Array(1,2,3);
    var name_value =  name.zip(value).toMap;
    println(name_value)
  }

  /**
    * 集合Option
    */
  def collectionOption(): Unit ={
    var map01 = Map(1 -> "111",2->"222");

    println("1:"+map01.get(1));
    println("2:"+map01.get(3));


    def show(x:Option[String]) =
      x match
      {
        case Some(s) => s;
        case None =>"?";
      }
    println("1:"+show(map01.get(1)));
    println("2:"+show(map01.get(3)));
    //更好的方法
    println("1:"+map01.getOrElse(1,"无"));
    println("2:"+map01.getOrElse(2,"无"));
    println("3:"+map01.getOrElse(3,"无"));


  }


}
