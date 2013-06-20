<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.css" rel="stylesheet">

</head>
<body>
<h2>List!</h2>
<display:table id="userList" name="userList" pagesize="5" export="false" requestURI="/getHelloUsers" class="table table-striped">
		<display:column property="id" title="ID" paramId="id" sortable="true"/>
		<display:column property="username" title="user" sortable="true"/>
		<display:column property="password" title="password" sortable="true"/>
	<display:setProperty name="paging.banner.placement" value="bottom" />
</display:table>
</body>
</html>
