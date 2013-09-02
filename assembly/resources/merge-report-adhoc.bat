@echo off
:: Run the merge report.
:: A sample adhoc run of the merge report that
:: will generate the email and just mail it to
:: the system user that ran the report. This is
:: good for seeing what's out there.

:: set the classpath
call build-classpath.bat

:: all of the base paths need to be updated to latest
:: before we run the report. See merge-report.xml for
:: more details.
echo svn update merge-report-trunk
svn update merge-report-trunk

:: run the merge report
echo %JAVA_HOME%\bin\java -cp %CP% %JVM_ARGS% au.net.thehardings.ims.mergereport.MergeReport
%JAVA_HOME%\bin\java -cp %CP% %JVM_ARGS% au.net.thehardings.ims.mergereport.MergeReport