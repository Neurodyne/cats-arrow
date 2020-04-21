import BuildHelper._

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val commonSettings = Seq(
// Refine scalac params from tpolecat
  scalacOptions --= Seq(
    "-Xfatal-warnings"
  )
)

lazy val commonDeps = libraryDependencies ++= Seq(
  ("com.github.ghik" % "silencer-lib" % Version.silencer % Provided)
    .cross(CrossVersion.full),
  compilerPlugin(("com.github.ghik" % "silencer-plugin" % Version.silencer).cross(CrossVersion.full))
)

lazy val catsDeps = libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % Version.cats
)

lazy val zioDeps = libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test"     % Version.zio % "test",
  "dev.zio" %% "zio-test-sbt" % Version.zio % "test"
)

lazy val root = (project in file("."))
  .settings(
    organization := "Neurodyne",
    name := "cats-arrow",
    version := "0.0.1",
    scalaVersion := "2.13.1",
    maxErrors := 3,
    commonSettings,
    commonDeps,
    catsDeps,
    zioDeps,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

lazy val bench = (project in file("bench"))
  .settings(commonSettings, catsDeps, zioDeps)
  .settings(stdSettings("bench"))
  .enablePlugins(JmhPlugin, JCStressPlugin)
  .dependsOn(root)

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile it:compile")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")

// Bench
addCommandAlias("arrBench", "bench/jmh:run -i 2 -wi 2 -f1 -t1 .*ArrBenchmark")
addCommandAlias("grBench", "bench/jmh:run -i 2 -wi 2 -f1 -t1 .*GRBenchmark")

scalafixDependencies in ThisBuild += "com.nequissimus" %% "sort-imports" % "0.3.2"
