@echo off
setLocal EnableDelayedExpansion
set CLASSPATH=".

for /f %%a in ('dir /b lib\*.jar') do (
   set CLASSPATH=!CLASSPATH!;lib/%%a
   REM echo %%a
)
set CCLASSPATH=!CLASSPATH!"
echo !CCLASSPATH!
 
java -classpath Hoop.jar;lib/jdom.jar;lib/je-5.0.34.jar;lib/jgraphx.jar;CCLASSPATH Hoop
