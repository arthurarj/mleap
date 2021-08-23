package ml.combust.mleap.tensorflow

import java.io.File
import java.nio.file.{Files, Path}

import org.tensorflow
import org.tensorflow.op.Ops
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import org.tensorflow.ndarray.Shape
import org.tensorflow.ndarray.StdArrays
import org.tensorflow.types.TFloat32
import org.tensorflow.Signature
import org.tensorflow.op.core.Placeholder
import org.tensorflow.ConcreteFunction
import org.tensorflow.ndarray.FloatNdArray
import org.tensorflow.ndarray.Shape
import org.tensorflow.ndarray.StdArrays
import org.tensorflow.op.core.Init
import java.nio.file.Files
import java.nio.file.Path
/**
  * Created by hollinwilkins on 1/13/17.
  * Updated to TF2.0 by AustinZh on 6/3/2021
  */

object TestUtil {
  def createAddGraph(): tensorflow.Graph = {
    val graph = new tensorflow.Graph
    val tf = Ops.create(graph)
    val inputA = tf.withName("InputA").placeholder(classOf[TFloat32])
    val inputB = tf.withName("InputB").placeholder(classOf[TFloat32])
    tf.withName("MyResult").math.add(inputA, inputB)
    graph
  }

  val baseDir = {
    val temp: Path = Files.createTempDirectory("mleap-tensorflow")
    temp.toFile.deleteOnExit()
    temp.toAbsolutePath
  }

  def delete(file: File): Array[(String, Boolean)] = {
    Option(file.listFiles).map(_.flatMap(f => delete(f))).getOrElse(Array()) :+ (file.getPath -> file.delete)
  }

  def createMultiplyGraph(): tensorflow.Graph = {
    val graph = new tensorflow.Graph
    val tf = Ops.create(graph)
    val inputA = tf.withName("InputA").placeholder(classOf[TFloat32])
    val inputB = tf.withName("InputB").placeholder(classOf[TFloat32])
    tf.withName("MyResult").math.mul(inputA, inputB)
    graph
  }


  def createGraphWithVariables(tf: Ops, xShape: Shape) :  Signature = {
    val x = tf.placeholder(classOf[TFloat32], Placeholder.shape(xShape))
    val y = tf.variable(tf.random.randomUniform(tf.constant(xShape), classOf[TFloat32]))
    val z = tf.reduceSum(tf.math.add(x, y), tf.array(0, 1))
    val init = tf.init
    Signature.builder.input("input", x).output("reducedSum", z).build
  }

}
