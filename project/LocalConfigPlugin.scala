import sbt._, Keys._

import com.typesafe.config.{ Config, ConfigFactory }
import net.ceedubs.ficus.Ficus._

object LocalConfigPlugin extends AutoPlugin {
  object autoImport {
    lazy val localConfigFile       = settingKey[File]("Local config file")
    lazy val localConfig           = taskKey[Config]("Load local config")
    lazy val awsRegion             = taskKey[String]("Get aws region")
    lazy val awsLambdaFunctionName = taskKey[String]("Get aws lambda function name")
  }

  import autoImport._

  override def projectSettings = Seq(
    localConfigFile       := (resourceDirectory in Compile).value / "local.conf",
    localConfig           := ConfigFactory.parseFile(localConfigFile.value),
    awsRegion             := localConfig.value.as[String]("aws.region"),
    awsLambdaFunctionName := localConfig.value.as[String]("aws.lambda.functionName"))
}
