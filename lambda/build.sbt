name := "chatwork-lambda-test"

resolvers += Resolver.jcenterRepo
libraryDependencies ++= Seq(
  "com.amazonaws"           %  "aws-java-sdk-dynamodb"   % "[1.10.50,1.11[",
  "com.amazonaws"           %  "aws-lambda-java-core"    % "1.1.0",
  "com.amazonaws"           %  "aws-lambda-java-events"  % "1.1.0",
  "com.iheart"              %% "ficus"                   % "1.2.1",
  "com.typesafe"            %  "config"                  % "1.3.0",
  "net.databinder.dispatch" %% "dispatch-core"           % "0.11.3",
  "org.json4s"              %% "json4s-jackson"          % "3.3.0"
)

initialCommands := """
import org.nisshiee.chatwork_lambda_test._
import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._
import org.nisshiee.chatwork_lambda_test.infra.chatwork._
"""

cleanupCommands := """
dispatch.Http.shutdown
"""

assemblyExcludedJars in assembly := {
  val cps = (fullClasspath in assembly).value
  val excludes = List(
    "scala-compiler",
    "scala-xml",
    "scala-parser-combinators",
    "aws-java-sdk-s3",
    "aws-java-sdk-sns",
    "aws-java-sdk-kinesis",
    "aws-java-sdk-kms",
    "aws-java-sdk-sqs",
    "aws-java-sdk-cognitoidentity")
  cps filter { cp =>
    val name = cp.data.getName
    excludes.foldLeft(false) { (a, e) =>
      a || name.startsWith(e)
    }
  }
}
