@echo off
 setLocal EnableDelayedExpansion
 set CLASSPATH="
 for /R ./lib %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
 )
set CLASSPATH=!CLASSPATH!"

REM echo !CLASSPATH!
 
REM java -cp Hoop.jar;lib HoopBroker -port 8080 >trace.log 2>&1
java -cp Hoop.jar;!CLASSPATH! HoopBroker -port 8080