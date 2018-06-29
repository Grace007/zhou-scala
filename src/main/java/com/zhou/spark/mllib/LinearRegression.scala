package com.zhou.spark.mllib
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LinearRegressionModel

/**
  * MLlib的线性回归模型采用随机梯度下降算法来优化目标函数，MLlib实现了分布式的随机梯度下
降算法，其分布方法是：在每次迭代中，随机抽取一定比例的样本作为当前迭代的计算样本；对
计算样本中的每一个样本分别计算梯度（分布式计算每个样本的梯度）；然后再通过聚合函数对
样本的梯度进行累加，得到该样本的平均梯度及损失；最后根据最新的梯度及上次迭代的权重进
行权重的更新。
  */
object LinearRegression {

  def main(args: Array[String]) {
    // 构建Spark对象
    val conf = new SparkConf().setAppName("LinearRegressionWithSGD").setMaster("local[1]")
    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)

    //读取样本数据
    val data_path1 = "src/main/java/com/zhou/spark/mllib/data/lpsa.data"
    val data = sc.textFile(data_path1)
    val examples = data.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }.cache() //迭代计算需要的数据最好缓存到内存
    val numExamples = examples.count()

    // 新建线性回归模型，并设置训练参数
    val numIterations = 100   //循环次数
    val stepSize = 1    //步长,学习率
    val miniBatchFraction = 1.0 //每次迭代需要样本比例
    val model = LinearRegressionWithSGD.train(examples, numIterations, stepSize, miniBatchFraction)
    println(model.weights)
    println(model.intercept)

    // 对样本进行测试 ,features就是特征向量,label是标签值(y值)
    val prediction = model.predict(examples.map(_.features))
    val predictionAndLabel = prediction.zip(examples.map(_.label))
    val print_predict = predictionAndLabel.take(20)
    println("prediction" + "\t" + "label")
    for (i <- 0 to print_predict.length - 1) {
      println(print_predict(i)._1 + "\t" + print_predict(i)._2)
    }
    // 计算测试误差
    val loss = predictionAndLabel.map {
      case (p, l) =>
        val err = p - l
        err * err
    }.reduce(_ + _)
    val rmse = math.sqrt(loss / numExamples)
    println(s"Test RMSE = $rmse.")

    // 模型保存
    val ModelPath = "src/main/java/com/zhou/spark/mllib/data/LinearRegressionModel.result"
    model.save(sc, ModelPath)
    val sameModel = LinearRegressionModel.load(sc, ModelPath)

  }

}
