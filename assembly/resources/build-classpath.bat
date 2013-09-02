@echo off
set JVM_ARGS=-Dmail.host=mail.mycompany.com -Dmail.domain=mycompany.com -Xms256m -Xmx256m
set JVM_ARGS=%JVM_ARGS% -Dlog4j.configuration=log4j.xml

:: Set the classpath. These need to be in this order.
set CP=conf
set CP=%CP%;lib\activation.jar
set CP=%CP%;lib\mail.jar

:: Add all remaining jar files from the lib directory to the classpath
for %%F in (lib\*.jar) do call classpath.bat %%F