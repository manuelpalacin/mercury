<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
    <!-- Le styles -->
<!--     <link href="css/bootstrap.css" rel="stylesheet"> -->
<!--     <link href="css/bootstrap-responsive.css" rel="stylesheet"> -->
	<%@ include file="head.jsp" %>
</head>

<body>

	<%@ include file="header.jsp" %>
	
	<div class="container">
	
		<section>
		<h2>Traceroute!</h2>
		
		<p>To See last processed traceroutes <a href="getLastTraceroutes">click here</a>. Below you can use the WebTracerouteTool.</p>
		<!-- <applet id='applet' name='applet' archive='TracerouteApplet.jar' code='edu.upf.nets.applet.NetAppletLauncher.class' width='300' height='100' MAYSCRIPT ></applet> -->
		<!-- 
		<object type="application/x-java-applet" height="100" width="300">
		  <param name="code" value="edu.upf.nets.mercury.applet.TracerouteApplet.class" />
		  <param name="archive" value="applet/TracerouteApplet.jar" />
		  <param name="mayscript" value="yes">
		  Applet failed to run.  No Java plug-in was found. 
		</object>
		 -->
		<h4>Info:</h4>
		<p>Be sure, you are not behind a gateway blocking traceroute requests. 
		If you are using MacOS please enable applet plugin in Java preferences and restart the browser. 
		  		<img alt="" src="images/JavaPreferencesWebPlugin.png"></p>
		</section>
	</div><!-- End container -->
	
	<%@ include file="footer.jsp" %>
	
		
</body>
</html>
