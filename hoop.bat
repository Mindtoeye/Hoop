@echo off

cls

REM echo %CLASSPATH%
 
REM java -classpath %CCLASSPATH% Hoop >trace.log 2>&1
java -classpath "%CLASSPATH%;.\lib\*;.\Hoop.jar" Hoop
