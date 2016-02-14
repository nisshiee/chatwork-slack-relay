import java.util.Date

import sbt._, Keys._
import com.typesafe.sbt._

object AwsPlugin extends AutoPlugin {
  object autoImport {
    val deployLambda = taskKey[File]("Deploy AWS Lambda Function")
    val invokeLambda = taskKey[File]("Invoke AWS Lambda Function")
  }

  import autoImport._
  import LocalConfigPlugin.autoImport._
  import SbtProguard.Proguard
  import SbtProguard.ProguardKeys._

  override def requires = LocalConfigPlugin

  override def projectSettings = Seq(
    deployLambda := {
      (proguard in Proguard).value match {
        case f :: Nil => {
          val command = Seq(
            "aws", "lambda", "update-function-code",
            "--region", awsRegion.value,
            "--function-name", awsLambdaFunctionName.value,
            "--zip-file", s"fileb://$f")
          command.!
          f
        }
      }
    },

    invokeLambda := {
      val now = "%tY%<tm%<td%<tH%<tM%<tS".format(new Date)
      val file = target.value / s"invoke-result-${now}"
      val command = Seq(
        "aws", "lambda", "invoke",
        "--region", awsRegion.value,
        "--function-name", awsLambdaFunctionName.value,
        "--payload",  "\"invoke from sbt\"",
        "--invocation-type", "RequestResponse",
        file.getCanonicalPath)
      command.!
      Seq("cat", file.getCanonicalPath).!
      "echo".!
      file
    })
}
