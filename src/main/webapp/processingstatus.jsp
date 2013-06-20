<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Le styles -->
<!--     <link href="css/bootstrap.css" rel="stylesheet"> -->
<!--     <link href="css/bootstrap-responsive.css" rel="stylesheet"> -->
<%-- 	<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script> --%>
	<%@ include file="head.jsp" %>
	
</head>
<body>
	<%@ include file="header.jsp" %>
	<div class="container">
	
		<section>
			<h2>Processing Status!</h2>
	
			<display:table id="processingCurrentStatusStat" name="processingCurrentStatusStat" export="false" class="table table-striped">
					<display:column property="timeStamp" title="timeStamp" />
					<display:column property="incompleted" title="incompleted" />
					<display:column property="completed" title="completed"/>
					<display:column property="processing" title="processing"/>
					<display:column property="pending" title="pending"/>
					<display:column property="error" title="error"/>
			</display:table>

		</section>


	</div><!-- End container -->
	
	<%@ include file="footer.jsp" %>
</body>
</html>
