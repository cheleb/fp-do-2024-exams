# Examen DO TP

**_Elève: Fabien Zoccola_**

---

10 steps.

## Init sbt project

Say hello, build a simple hello world application.

```bash
sbt runMain doit.HelloWorld

```

Should output: "Hello World"


## Hello ZIO World

* ZIO dependencies
  * "dev.zio" %% "zio" % "2.1.3"
  * "dev.zio" %% "zio-streams" % "2.1.3"

Build a simple hello world application using ZIO.

```bash
sbt runMain doit.ZIOHelloWorld

```

Should output: "Hello World"

## Http 

### Http Server

Build a simple http server using ZIO.

* lib dependencies
  * libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC8"

```bash
sbt runMain doit.ZIOHttpServer

```

Should start a server on port 8080 that returns "Hello $name" on every request.

```bash
curl http://localhost:8080/hello?name=World
```


### Http Client

Build a simple http client using ZIO.

```bash
sbt runMain doit.ZIOHttpClient

```

Should output: "Hello World"


### Tapir 

* Tapir dependencies
  * "com.softwaremill.sttp.tapir" %% "tapir-zio" % "1.10.9"
  * "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % "1.10.9"
  * "com.softwaremill.sttp.tapir" %% "tapir-zio" % "1.10.9"
  * "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % "1.10.9"
  * "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.10.9"



Build a simple http server using Tapir.

## ScalaJS (Optional)

* ScalaJS dependencies
  * "org.scala-js" %% "scalajs-dom" % "1.1.0"

## Tapir Http Client

* Tapir dependencies
  * "com.softwaremill.sttp.tapir" %% "tapir-zio" % "0.17.0"
  * "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.17.0"
