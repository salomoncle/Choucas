@REM choucas launcher script
@REM
@REM Environment:
@REM JAVA_HOME - location of a JDK home dir (optional if java on path)
@REM CFG_OPTS  - JVM options (optional)
@REM Configuration:
@REM CHOUCAS_config.txt found in the CHOUCAS_HOME.
@setlocal enabledelayedexpansion

@echo off


if "%CHOUCAS_HOME%"=="" (
  set "APP_HOME=%~dp0\\.."

  rem Also set the old env name for backwards compatibility
  set "CHOUCAS_HOME=%~dp0\\.."
) else (
  set "APP_HOME=%CHOUCAS_HOME%"
)

set "APP_LIB_DIR=%APP_HOME%\lib\"

rem Detect if we were double clicked, although theoretically A user could
rem manually run cmd /c
for %%x in (!cmdcmdline!) do if %%~x==/c set DOUBLECLICKED=1

rem FIRST we load the config file of extra options.
set "CFG_FILE=%APP_HOME%\CHOUCAS_config.txt"
set CFG_OPTS=
call :parse_config "%CFG_FILE%" CFG_OPTS

rem We use the value of the JAVACMD environment variable if defined
set _JAVACMD=%JAVACMD%

if "%_JAVACMD%"=="" (
  if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" set "_JAVACMD=%JAVA_HOME%\bin\java.exe"
  )
)

if "%_JAVACMD%"=="" set _JAVACMD=java

rem Detect if this java is ok to use.
for /F %%j in ('"%_JAVACMD%" -version  2^>^&1') do (
  if %%~j==java set JAVAINSTALLED=1
  if %%~j==openjdk set JAVAINSTALLED=1
)

rem BAT has no logical or, so we do it OLD SCHOOL! Oppan Redmond Style
set JAVAOK=true
if not defined JAVAINSTALLED set JAVAOK=false

if "%JAVAOK%"=="false" (
  echo.
  echo A Java JDK is not installed or can't be found.
  if not "%JAVA_HOME%"=="" (
    echo JAVA_HOME = "%JAVA_HOME%"
  )
  echo.
  echo Please go to
  echo   http://www.oracle.com/technetwork/java/javase/downloads/index.html
  echo and download a valid Java JDK and install before running choucas.
  echo.
  echo If you think this message is in error, please check
  echo your environment variables to see if "java.exe" and "javac.exe" are
  echo available via JAVA_HOME or PATH.
  echo.
  if defined DOUBLECLICKED pause
  exit /B 1
)


rem We use the value of the JAVA_OPTS environment variable if defined, rather than the config.
set _JAVA_OPTS=%JAVA_OPTS%
if "!_JAVA_OPTS!"=="" set _JAVA_OPTS=!CFG_OPTS!

rem We keep in _JAVA_PARAMS all -J-prefixed and -D-prefixed arguments
rem "-J" is stripped, "-D" is left as is, and everything is appended to JAVA_OPTS
set _JAVA_PARAMS=
set _APP_ARGS=

set "APP_CLASSPATH=%APP_LIB_DIR%\default.choucas-0.1.jar;%APP_LIB_DIR%\org.scala-lang.scala-library-2.12.4.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http_2.12-10.0.11.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-core_2.12-10.0.11.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-parsing_2.12-10.0.11.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream_2.12-2.5.7.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor_2.12-2.5.7.jar;%APP_LIB_DIR%\com.typesafe.config-1.3.2.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-java8-compat_2.12-0.8.0.jar;%APP_LIB_DIR%\org.reactivestreams.reactive-streams-1.0.1.jar;%APP_LIB_DIR%\com.typesafe.ssl-config-core_2.12-0.2.2.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-parser-combinators_2.12-1.0.4.jar;%APP_LIB_DIR%\org.apache.commons.commons-lang3-3.2.1.jar;%APP_LIB_DIR%\javax.validation.validation-api-1.1.0.Final.jar;%APP_LIB_DIR%\org.scala-lang.scala-reflect-2.12.4.jar;%APP_LIB_DIR%\com.thoughtworks.paranamer.paranamer-2.8.jar;%APP_LIB_DIR%\com.github.swagger-akka-http.swagger-akka-http_2.12-0.11.0.jar;%APP_LIB_DIR%\io.swagger.swagger-core-1.5.16.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-annotations-2.8.9.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-databind-2.8.9.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-core-2.8.9.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-yaml-2.8.9.jar;%APP_LIB_DIR%\org.yaml.snakeyaml-1.17.jar;%APP_LIB_DIR%\io.swagger.swagger-models-1.5.16.jar;%APP_LIB_DIR%\io.swagger.swagger-annotations-1.5.16.jar;%APP_LIB_DIR%\com.google.guava.guava-20.0.jar;%APP_LIB_DIR%\io.swagger.swagger-jaxrs-1.5.16.jar;%APP_LIB_DIR%\javax.ws.rs.jsr311-api-1.1.1.jar;%APP_LIB_DIR%\org.reflections.reflections-0.9.11.jar;%APP_LIB_DIR%\org.javassist.javassist-3.21.0-GA.jar;%APP_LIB_DIR%\io.swagger.swagger-scala-module_2.12-1.0.4.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-scala_2.12-2.8.9.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-paranamer-2.8.9.jar;%APP_LIB_DIR%\org.scalaj.scalaj-http_2.12-2.3.0.jar;%APP_LIB_DIR%\ch.qos.logback.logback-classic-1.2.3.jar;%APP_LIB_DIR%\ch.qos.logback.logback-core-1.2.3.jar;%APP_LIB_DIR%\org.slf4j.slf4j-api-1.7.25.jar;%APP_LIB_DIR%\net.databinder.dispatch.dispatch-core_2.12-0.13.3.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-xml_2.12-1.0.6.jar;%APP_LIB_DIR%\org.asynchttpclient.async-http-client-2.0.38.jar;%APP_LIB_DIR%\org.asynchttpclient.async-http-client-netty-utils-2.0.38.jar;%APP_LIB_DIR%\io.netty.netty-transport-native-epoll-4.0.54.Final-linux-x86_64.jar;%APP_LIB_DIR%\org.asynchttpclient.netty-resolver-dns-2.0.38.jar;%APP_LIB_DIR%\org.asynchttpclient.netty-resolver-2.0.38.jar;%APP_LIB_DIR%\org.asynchttpclient.netty-codec-dns-2.0.38.jar;%APP_LIB_DIR%\com.typesafe.netty.netty-reactive-streams-1.0.8.jar;%APP_LIB_DIR%\org.json4s.json4s-ext_2.12-3.2.11.jar;%APP_LIB_DIR%\org.joda.joda-convert-1.6.jar;%APP_LIB_DIR%\net.databinder.dispatch.dispatch-json4s-native_2.12-0.13.3.jar;%APP_LIB_DIR%\org.json4s.json4s-core_2.12-3.5.1.jar;%APP_LIB_DIR%\org.json4s.json4s-ast_2.12-3.5.1.jar;%APP_LIB_DIR%\org.json4s.json4s-scalap_2.12-3.5.1.jar;%APP_LIB_DIR%\org.json4s.json4s-native_2.12-3.5.1.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-core_2.12-5.5.3.jar;%APP_LIB_DIR%\com.sksamuel.exts.exts_2.12-1.44.0.jar;%APP_LIB_DIR%\org.typelevel.cats_2.12-0.9.0.jar;%APP_LIB_DIR%\org.typelevel.cats-macros_2.12-0.9.0.jar;%APP_LIB_DIR%\com.github.mpilquist.simulacrum_2.12-0.10.0.jar;%APP_LIB_DIR%\org.typelevel.macro-compat_2.12-1.1.1.jar;%APP_LIB_DIR%\org.typelevel.machinist_2.12-0.6.1.jar;%APP_LIB_DIR%\org.typelevel.cats-kernel_2.12-0.9.0.jar;%APP_LIB_DIR%\org.typelevel.cats-kernel-laws_2.12-0.9.0.jar;%APP_LIB_DIR%\org.scalacheck.scalacheck_2.12-1.13.4.jar;%APP_LIB_DIR%\org.scala-sbt.test-interface-1.0.jar;%APP_LIB_DIR%\org.typelevel.discipline_2.12-0.7.2.jar;%APP_LIB_DIR%\org.typelevel.catalysts-platform_2.12-0.0.5.jar;%APP_LIB_DIR%\org.typelevel.catalysts-macros_2.12-0.0.5.jar;%APP_LIB_DIR%\org.typelevel.cats-core_2.12-0.9.0.jar;%APP_LIB_DIR%\org.typelevel.cats-laws_2.12-0.9.0.jar;%APP_LIB_DIR%\org.typelevel.cats-free_2.12-0.9.0.jar;%APP_LIB_DIR%\org.typelevel.cats-jvm_2.12-0.9.0.jar;%APP_LIB_DIR%\org.elasticsearch.elasticsearch-5.5.2.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-core-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-analyzers-common-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-backward-codecs-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-grouping-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-highlighter-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-join-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-memory-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-misc-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-queries-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-queryparser-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-sandbox-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-spatial-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-spatial-extras-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-spatial3d-6.6.0.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-suggest-6.6.0.jar;%APP_LIB_DIR%\org.elasticsearch.securesm-1.1.jar;%APP_LIB_DIR%\net.sf.jopt-simple.jopt-simple-5.0.2.jar;%APP_LIB_DIR%\com.carrotsearch.hppc-0.7.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-smile-2.8.6.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-cbor-2.8.6.jar;%APP_LIB_DIR%\org.hdrhistogram.HdrHistogram-2.1.9.jar;%APP_LIB_DIR%\org.apache.logging.log4j.log4j-api-2.8.2.jar;%APP_LIB_DIR%\org.elasticsearch.jna-4.4.0.jar;%APP_LIB_DIR%\org.locationtech.spatial4j.spatial4j-0.6.jar;%APP_LIB_DIR%\com.vividsolutions.jts-1.13.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-tcp_2.12-5.5.3.jar;%APP_LIB_DIR%\io.netty.netty-all-4.1.10.Final.jar;%APP_LIB_DIR%\org.elasticsearch.client.transport-5.5.2.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.transport-netty3-client-5.5.2.jar;%APP_LIB_DIR%\io.netty.netty-3.10.6.Final.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.transport-netty4-client-5.5.2.jar;%APP_LIB_DIR%\io.netty.netty-buffer-4.1.11.Final.jar;%APP_LIB_DIR%\io.netty.netty-common-4.1.11.Final.jar;%APP_LIB_DIR%\io.netty.netty-codec-4.1.11.Final.jar;%APP_LIB_DIR%\io.netty.netty-transport-4.1.11.Final.jar;%APP_LIB_DIR%\io.netty.netty-resolver-4.1.11.Final.jar;%APP_LIB_DIR%\io.netty.netty-codec-http-4.1.11.Final.jar;%APP_LIB_DIR%\io.netty.netty-handler-4.1.11.Final.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.reindex-client-5.5.2.jar;%APP_LIB_DIR%\org.elasticsearch.client.rest-5.5.2.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-4.4.5.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpasyncclient-4.1.2.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-nio-4.4.5.jar;%APP_LIB_DIR%\commons-codec.commons-codec-1.10.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.lang-mustache-client-5.5.2.jar;%APP_LIB_DIR%\com.github.spullara.mustache.java.compiler-0.9.3.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.percolator-client-5.5.2.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.parent-join-client-5.5.2.jar;%APP_LIB_DIR%\org.apache.logging.log4j.log4j-core-2.8.2.jar;%APP_LIB_DIR%\org.apache.logging.log4j.log4j-1.2-api-2.6.2.jar;%APP_LIB_DIR%\joda-time.joda-time-2.9.9.jar;%APP_LIB_DIR%\com.tdunning.t-digest-3.1.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-http_2.12-5.5.3.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpclient-4.5.3.jar;%APP_LIB_DIR%\commons-logging.commons-logging-1.2.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-streams_2.12-5.5.3.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-jackson_2.12-5.5.3.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-joda-2.8.8.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-play-json_2.12-5.5.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-json_2.12-2.6.0-M6.jar;%APP_LIB_DIR%\com.typesafe.play.play-functional_2.12-2.6.0-M6.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jdk8-2.8.5.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jsr310-2.8.5.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-circe_2.12-5.5.3.jar;%APP_LIB_DIR%\io.circe.circe-core_2.12-0.7.1.jar;%APP_LIB_DIR%\io.circe.circe-numbers_2.12-0.7.1.jar;%APP_LIB_DIR%\io.circe.circe-generic_2.12-0.7.1.jar;%APP_LIB_DIR%\com.chuusai.shapeless_2.12-2.3.2.jar;%APP_LIB_DIR%\io.circe.circe-parser_2.12-0.7.1.jar;%APP_LIB_DIR%\io.circe.circe-jawn_2.12-0.7.1.jar;%APP_LIB_DIR%\org.spire-math.jawn-parser_2.12-0.10.4.jar;%APP_LIB_DIR%\com.sksamuel.elastic4s.elastic4s-json4s_2.12-5.5.3.jar;%APP_LIB_DIR%\org.json4s.json4s-jackson_2.12-3.5.1.jar;%APP_LIB_DIR%\org.apache.logging.log4j.log4j-api-scala_2.12-11.0.jar"
set "APP_MAIN_CLASS=main.HttpServer"
set "SCRIPT_CONF_FILE=%APP_HOME%\conf\application.ini"

rem if configuration files exist, prepend their contents to the script arguments so it can be processed by this runner
call :parse_config "%SCRIPT_CONF_FILE%" SCRIPT_CONF_ARGS

call :process_args %SCRIPT_CONF_ARGS% %%*

set _JAVA_OPTS=!_JAVA_OPTS! !_JAVA_PARAMS!

if defined CUSTOM_MAIN_CLASS (
    set MAIN_CLASS=!CUSTOM_MAIN_CLASS!
) else (
    set MAIN_CLASS=!APP_MAIN_CLASS!
)

rem Call the application and pass all arguments unchanged.
"%_JAVACMD%" !_JAVA_OPTS! !CHOUCAS_OPTS! -cp "%APP_CLASSPATH%" %MAIN_CLASS% !_APP_ARGS!

@endlocal

exit /B %ERRORLEVEL%


rem Loads a configuration file full of default command line options for this script.
rem First argument is the path to the config file.
rem Second argument is the name of the environment variable to write to.
:parse_config
  set _PARSE_FILE=%~1
  set _PARSE_OUT=
  if exist "%_PARSE_FILE%" (
    FOR /F "tokens=* eol=# usebackq delims=" %%i IN ("%_PARSE_FILE%") DO (
      set _PARSE_OUT=!_PARSE_OUT! %%i
    )
  )
  set %2=!_PARSE_OUT!
exit /B 0


:add_java
  set _JAVA_PARAMS=!_JAVA_PARAMS! %*
exit /B 0


:add_app
  set _APP_ARGS=!_APP_ARGS! %*
exit /B 0


rem Processes incoming arguments and places them in appropriate global variables
:process_args
  :param_loop
  call set _PARAM1=%%1
  set "_TEST_PARAM=%~1"

  if ["!_PARAM1!"]==[""] goto param_afterloop


  rem ignore arguments that do not start with '-'
  if "%_TEST_PARAM:~0,1%"=="-" goto param_java_check
  set _APP_ARGS=!_APP_ARGS! !_PARAM1!
  shift
  goto param_loop

  :param_java_check
  if "!_TEST_PARAM:~0,2!"=="-J" (
    rem strip -J prefix
    set _JAVA_PARAMS=!_JAVA_PARAMS! !_TEST_PARAM:~2!
    shift
    goto param_loop
  )

  if "!_TEST_PARAM:~0,2!"=="-D" (
    rem test if this was double-quoted property "-Dprop=42"
    for /F "delims== tokens=1,*" %%G in ("!_TEST_PARAM!") DO (
      if not ["%%H"] == [""] (
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
      ) else if [%2] neq [] (
        rem it was a normal property: -Dprop=42 or -Drop="42"
        call set _PARAM1=%%1=%%2
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
        shift
      )
    )
  ) else (
    if "!_TEST_PARAM!"=="-main" (
      call set CUSTOM_MAIN_CLASS=%%2
      shift
    ) else (
      set _APP_ARGS=!_APP_ARGS! !_PARAM1!
    )
  )
  shift
  goto param_loop
  :param_afterloop

exit /B 0
