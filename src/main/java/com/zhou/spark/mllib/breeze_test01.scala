package com.zhou.spark.mllib

import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import breeze.linalg._  //导入矩阵和向量方法包
import breeze.numerics._  //数值计算函数包
import org.apache.spark.mllib.linalg.Vectors

object breeze_test01 {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("breeze_test01").setMaster("local[1]")
    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)

    // 3.1.1 Breeze 创建函数
    val m1 = DenseMatrix.zeros[Double](2, 3)
    val v1 = DenseVector.zeros[Double](3)
    val v2 = DenseVector.ones[Double](3)
    val v3 = DenseVector.fill(3) { 5.0 }
    val v4 = DenseVector.range(1, 10, 2)
    val m2 = DenseMatrix.eye[Double](3)
    val v6 = diag(DenseVector(1.0, 2.0, 3.0))
    val m3 = DenseMatrix((1.0, 2.0), (3.0, 4.0))
    val v8 = DenseVector(1, 2, 3, 4)
    val v9 = DenseVector(1, 2, 3, 4).t
    val v10 = DenseVector.tabulate(3) { i => 2 * i }
    val m4 = DenseMatrix.tabulate(3, 2) { case (i, j) => i + j }
    val v11 = new DenseVector(Array(1, 2, 3, 4))
    val m5 = new DenseMatrix(2, 3, Array(11, 12, 13, 21, 22, 23))
    val v12 = DenseVector.rand(4)
    val m6 = DenseMatrix.rand(2, 3)

    // 3.1.2 Breeze 元素访问及操作函数
    // 元素访问
    val a = DenseVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(a(0))
    println(a(1 to 4))
    println(a(5 to 0 by -1))
    println(a(1 to -1))
    println(a(-1))
    val m = DenseMatrix((1.0, 2.0, 3.0), (3.0, 4.0, 5.0))
    println(m(0, 1))
    println(m(::, 1))

    // 元素操作
    val m_1 = DenseMatrix((1.0, 2.0, 3.0), (3.0, 4.0, 5.0))
    println(m_1.reshape(3, 2))
    println(m_1.toDenseVector)

    val m_3 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(lowerTriangular(m_3)) //只取下三角,上三角元素置0
    println(upperTriangular(m_3)) //只取上三角,下三角元素置0
    println(m_3.copy) //复制矩阵
    println(diag(m_3))  //取对角线
    println(m_3(::, 2) := 5.0)  //赋
    println(m_3)
    println(m_3(1 to 2, 1 to 2) := 5.0)
    println(m_3)

    val a_1 = DenseVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(a_1(1 to 4) := 5)
    println(a_1(1 to 4) := DenseVector(1, 2, 3, 4))
    println(a_1)
    val a1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val a2 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println(DenseMatrix.vertcat(a1, a2))  //垂直连接矩阵
    println(DenseMatrix.horzcat(a1, a2))  //横向连接矩阵
    val b1 = DenseVector(1, 2, 3, 4)
    val b2 = DenseVector(1, 1, 1, 1)
    println(DenseVector.vertcat(b1, b2))

    // 3.1.3 Breeze 数值计算函数 (元素计算)
    val a_3 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val b_3 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println(a_3 + b_3)
    println(a_3 :* b_3)
    println(a_3 :/ b_3)
    println(a_3 :< b_3)
    println(a_3 :== b_3)
    println(a_3 :+= 1.0)
    println(a_3 :*= 2.0)
    println(max(a_3))
    println(argmax(a_3))  //元素最大值及位置
    println(DenseVector(1, 2, 3, 4) dot DenseVector(1, 1, 1, 1))  //向量点积 (向量积)

    // 3.1.4 Breeze 求和函数
    val a_4 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(sum(a_4))
    println(sum(a_4, Axis._0))  //按行方向(一维方向)乘
    println(sum(a_4, Axis._1))  //按列方向(二维方向)乘
    println(trace(a_4))   //对角线元素和
    println(accumulate(DenseVector(1, 2, 3, 4)))  //累积和 (1, 3, 6, 10)

    // 3.1.5 Breeze 布尔函数
    val a_5 = DenseVector(true, false, true)
    val b_5 = DenseVector(false, true, true)
    println(a_5 :& b_5)
    println(a_5 :| b_5)
    println(!a_5)
    val a_5_2 = DenseVector(1.0, 0.0, -2.0)
    println(any(a_5_2))   //任意元素非零
    println(all(a_5_2))   //所有元素非零

    // 3.1.6 Breeze 线性代数函数
    val a_6 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    val b_6 = DenseMatrix((1.0, 1.0, 1.0), (1.0, 1.0, 1.0), (1.0, 1.0, 1.0))
    println(a_6 \ b_6)    //线性求解
    println(a_6.t)    //转置
    println(det(a_6))   //求特征值
    println(inv(a_6))   //求逆
    val svd.SVD(u, s, v) = svd(a_6)   //奇异值分解
    println(a_6.rows)   //矩阵行数
    println(a_6.cols)   //矩阵列数

    // 3.1.7 Breeze 取整函数
    val a_7 = DenseVector(1.2, 0.6, -2.3)
    println(round(a_7))   //四舍五入
    println(ceil(a_7))    //最小整数
    println(floor(a_7))   //最大整数
    println(signum(a_7))  //符号函数
    println(abs(a_7))   //取正数

  }
}