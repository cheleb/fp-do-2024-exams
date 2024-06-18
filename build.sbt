scalaVersion := "3.4.2"

mainClass in reStart := Some("dotapir.HttpServer")

val Versions = new {
  val zioConfig = "4.0.2"
  val flywaydb = "10.15.0"
}

libraryDependencies += "dev.zio" %% "zio" % "2.1.3"
libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC8"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-zio" % "1.10.9"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % "1.10.9"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % "1.10.9"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.10.9"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-config" % Versions.zioConfig,
  "dev.zio" %% "zio-config-magnolia" % Versions.zioConfig,
  "dev.zio" %% "zio-config-typesafe" % Versions.zioConfig
)

libraryDependencies ++= Seq(
  "org.flywaydb" % "flyway-core" % Versions.flywaydb,
  "org.flywaydb" % "flyway-database-postgresql" % Versions.flywaydb,
  "io.getquill" %% "quill-jdbc-zio" % "4.8.5"
)

libraryDependencies += "org.postgresql" % "postgresql" % "42.7.3"
