resolvers += Resolver.bintrayRepo("ktosopl", "sbt-plugins/sbt-jcstress")

addSbtPlugin("org.scalameta"             % "sbt-scalafmt" % "2.3.4")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix" % "0.9.14")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.11")
addSbtPlugin("pl.project13.scala"        % "sbt-jmh"      % "0.3.7")
addSbtPlugin("pl.project13.scala"        % "sbt-jcstress" % "0.2.0")
