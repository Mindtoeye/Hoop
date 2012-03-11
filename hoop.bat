@echo off
setLocal EnableDelayedExpansion
set CLASSPATH="

for /R ./lib %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
)
set CCLASSPATH=!CLASSPATH!"
echo !CCLASSPATH!
 
java -classpath INHoop.jar;CCLASSPATH INHoop