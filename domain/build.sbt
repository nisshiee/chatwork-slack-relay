name := "chatwork-lambda-test-domain"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time"                 % "2.8.0",
  "org.scalaz"             %% "scalaz-core"                 % "7.2.0",

  "com.github.julien-truffaut"  %%  "monocle-core"    % "1.2.0",
  "com.github.julien-truffaut"  %%  "monocle-generic" % "1.2.0",
  "com.github.julien-truffaut"  %%  "monocle-macro"   % "1.2.0",
  "com.github.julien-truffaut"  %%  "monocle-state"   % "1.2.0",
  "com.github.julien-truffaut"  %%  "monocle-refined" % "1.2.0",

  "org.scalatest"          %% "scalatest"                   % "2.2.6"  % "test",
  "org.scalamock"          %% "scalamock-scalatest-support" % "3.2.2"  % "test"
)

addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
