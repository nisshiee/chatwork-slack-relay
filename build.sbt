lazy val commonSettings = Seq(
  organization := "org.nisshiee",
  version := "1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  enablePlugins(LocalConfigPlugin, AwsPlugin, DistributionPlugin).
  dependsOn(domain)

lazy val domain = (project in file("domain")).
  settings(commonSettings: _*)

name := "chatwork-lambda-test"

resolvers += Resolver.jcenterRepo
val exclusionUnnecessaryAws = Seq(
  ExclusionRule("com.amazonaws", "aws-java-sdk-s3"),
  ExclusionRule("com.amazonaws", "aws-java-sdk-sns"),
  ExclusionRule("com.amazonaws", "aws-java-sdk-kinesis"),
  ExclusionRule("com.amazonaws", "aws-java-sdk-kms"),
  ExclusionRule("com.amazonaws", "aws-java-sdk-sqs"),
  ExclusionRule("com.amazonaws", "aws-java-sdk-cognitoidentity"))

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-dynamodb"  % "[1.10.50,1.11[" excludeAll(exclusionUnnecessaryAws: _*),
  "com.amazonaws" % "aws-lambda-java-core"   % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.1.0"          excludeAll(exclusionUnnecessaryAws: _*),

  "com.iheart"              %% "ficus"          % "1.2.1",
  "com.typesafe"            %  "config"         % "1.3.0",
  "net.databinder.dispatch" %% "dispatch-core"  % "0.11.3",
  "org.json4s"              %% "json4s-jackson" % "3.3.0")

initialCommands := """
import org.nisshiee.chatwork_lambda_test._
import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._
import org.nisshiee.chatwork_lambda_test.infra.chatwork._
"""
cleanupCommands := """
dispatch.Http.shutdown
"""

proguardSettings
import ProguardKeys._

proguardVersion in Proguard := "5.2.1"
javaOptions in (Proguard, proguard) := Seq("-Xmx2G")
options in Proguard ++= Seq("-dontnote", "-dontwarn", "-ignorewarnings")
mergeStrategies in Proguard += ProguardMerge.discard("META-INF/.*".r)
mergeStrategies in Proguard += ProguardMerge.append("reference.conf")

options in Proguard += "-dontoptimize"
options in Proguard += "-dontobfuscate"

val keepClasses = Seq(
    "org.nisshiee.chatwork_lambda_test.**",
    "com.fasterxml.**",
    "org.apache.commons.logging.impl.**",
    "com.amazonaws.**",
    "com.ning.http.client.providers.**",
    "org.jboss.netty.**").
  mkString(",")
options in Proguard +=
  s"""-keep class ${keepClasses} {
     |  *;
     |}""".stripMargin

cleanAll := {
  (clean in domain).value
  clean.value
}

testAll := {
  (test in domain in Test).value
  (test in Test).value
}
