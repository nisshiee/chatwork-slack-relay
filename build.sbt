lazy val commonSettings = Seq(
  organization := "org.nisshiee",
  version := "1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  aggregate(lambda, domain)

lazy val lambda = (project in file("lambda")).
  settings(commonSettings: _*).
  dependsOn(domain)

lazy val domain = (project in file("domain")).
  settings(commonSettings: _*)
