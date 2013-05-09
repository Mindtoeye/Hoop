@echo off
 setLocal EnableDelayedExpansion
 set CLASSPATH="
 for /R ./lib %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
 )
set CLASSPATH=!CLASSPATH!"

REM echo !CLASSPATH!
 
REM java -cp Hoop.jar;lib edu.cmu.cs.in.network.HoopBroker -port 8080 >trace.log 2>&1
java -cp Hoop.jar;!CLASSPATH! edu.cmu.cs.in.network.HoopBroker -port 8080