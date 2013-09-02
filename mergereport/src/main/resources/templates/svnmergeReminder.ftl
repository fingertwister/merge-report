<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<body style="font-family: Arial; font-size: 10pt;">

<p>You are receiving this email because there are some unmerged commits against your user id. Please merge your changes as soon as possible.

<#list users?keys as user>
<p><b>${user}</b><br>
<#list users[user]?keys as repository>
<span style="padding-left: 20px"><b>${repository}</b> (${commands[user][repository]})</span><br>
<#list (users[user])[repository] as commit>
<span style="padding-left: 20px">${commit.commitId}: ${commit.comment[0]}</span><br>
</#list>
</#list>
</#list>

<p>Some points for the people who are new to merging with svn:

<p>* All svn merge commands should be run from your "merge to" checkout directory.<br>
* Please make sure your "merge to" checkout is updated to latest and has no uncommitted changes outstanding (and the build is not broken!).<br>
* To see whats available to be merged, do this: svn mergeinfo --show-revs eligible [url to source]<br>
* If the revisions should not be merged or have been merged manually, do this: svn merge --record-only -c [rev list]<br>
* Don't forget to commit your changes as soon as the merge completes successfully (svn commit -m "merging changes from [source] to [dest]").

</body>
</html>
