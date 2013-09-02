@echo off
:: Run the merge report.
:: A sample formal run of the merge report that
:: will generate the email and mail it to anyone
:: who has an outstanding merge in the report.
:: The requesting user will be cc'd to the email.

:: set the classpath
call build-classpath.bat

:: all of the base paths need to be updated to latest
:: before we run the report. See merge-report.xml for
:: more details.
echo svn update merge-report-trunk > merge-report.log
svn update merge-report-trunk > merge-report.log

:: run the merge report
echo %JAVA_HOME%\bin\java -cp %CP% %JVM_ARGS% au.net.thehardings.ims.mergereport.MergeReport -a >> merge-report.log
%JAVA_HOME%\bin\java -cp %CP% %JVM_ARGS% au.net.thehardings.ims.mergereport.MergeReport -a >> merge-report.log