import sbt._, Keys._

object DistributionPlugin extends AutoPlugin {
  object autoImport {
    val cleanAll = taskKey[Unit]("Clean all - including subprojects")
    val testAll  = taskKey[Unit]("Test all - including subprojects")
    val dist     = taskKey[File]("Clean, Build, Test, Package, and Deploy")
  }

  import autoImport._
  import AwsPlugin.autoImport._

  override def requires = AwsPlugin

  override def projectSettings = Seq(
    dist := deployLambda.value,
    dist <<= dist.dependsOn(testAll),
    dist <<= dist.dependsOn(cleanAll))
}
