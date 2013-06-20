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
			<h2>Last processed traceroutes!</h2>
			
			<display:table id="tracerouteIndexInfoList" name="tracerouteIndexInfoList" pagesize="10" export="false" requestURI="/getLastTraceroutes" class="table table-condensed table-hover">
					<display:column property="tracerouteGroupId" title="Group Id" href="getTraceroute" paramId="tracerouteGroupId" paramProperty="tracerouteGroupId" sortable="true"/>
					<display:column property="timeStamp" title="timeStamp" sortable="true"/>
					
					<display:column property="originIp" title="origin Ip" sortable="true"/>
					<display:column title="origin City" sortable="true">
						<a href="getASTracerouteStatsByOriginCity?originCity=<s:url value="%{#attr.tracerouteIndexInfoList.originCity}"/>&originCountry=<s:url value="%{#attr.tracerouteIndexInfoList.originCountry}"/>" >
							<s:url value="%{#attr.tracerouteIndexInfoList.originCity}"/>
						</a>
					</display:column>
					
					<display:column property="originCountry" title="origin Country" href="getASTracerouteStatsByOriginCountry" paramId="originCountry" paramProperty="originCountry" sortable="true" />			
					<display:column property="originAS" title="origin AS" href="getASTracerouteStatsByOriginAS" paramId="originAS" paramProperty="originAS" sortable="true" />
					<display:column property="originASName" title="origin AS Name" sortable="true"/>
					<display:column property="destination" title="destination" href="getASTracerouteStatsByDestination" paramId="destination" paramProperty="destination" sortable="true" />
					<display:column property="destinationIp" title="destination Ip" sortable="true"/>
					<display:column title="destination City" sortable="true" >
						<a href="getASTracerouteStatsByDestinationCity?destinationCity=<s:url value="%{#attr.tracerouteIndexInfoList.destinationCity}"/>&destinationCountry=<s:url value="%{#attr.tracerouteIndexInfoList.destinationCountry}"/>" >
							<s:url value="%{#attr.tracerouteIndexInfoList.destinationCity}"/>
						</a>
					</display:column>
					
					
					
					
					<display:column property="destinationCountry" title="destination Country" href="getASTracerouteStatsByDestinationCountry" paramId="destinationCountry" paramProperty="destinationCountry" sortable="true" />
					<display:column property="destinationAS" title="destination AS" href="getASTracerouteStatsByDestinationAS" paramId="destinationAS" paramProperty="destinationAS" sortable="true" />
					<display:column property="destinationASName" title="destination AS Name" sortable="true"/>
					
			
					<display:column title="view tr" >
						<a href="api/traceroute/getASTraceroute/<s:url value="%{#attr.tracerouteIndexInfoList.tracerouteGroupId}"/>" ><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column title="view rels" >
						<a href="api/traceroute/getASTracerouteRelationships/<s:url value="%{#attr.tracerouteIndexInfoList.tracerouteGroupId}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column title="view stats" >
						<a href="api/traceroute/getASTracerouteStats/<s:url value="%{#attr.tracerouteIndexInfoList.tracerouteGroupId}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					
				<display:setProperty name="paging.banner.placement" value="bottom" />
			</display:table>
		</section>
	
		<section>
			<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		    <script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		    	  
		    	  
					var jsonPoints = '<s:property value="evolutionPointsJson" escapeJavaScript="true" escapeHtml="true" />';
					jsonPoints = replaceAll(jsonPoints, '&quot;','\"');
					var jsonObj = JSON.parse(jsonPoints);
					
					var d1 = [];
					d1.push(['Date','Traces']);
					
					jQuery.each(jsonObj, function(i, val) {
					      var millis = parseInt(i);
					      x = new Date(millis);
					      //alert(x+" : "+val);
					      d1.push([x.toString(), val]);
					    });
		    	  
		        var data = google.visualization.arrayToDataTable(d1);
		
		        var options = {
		          title: 'Traces evolution'
		        };
		
		        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
		        chart.draw(data, options);
		      }
		      
				function replaceAll(string, token, newtoken) {
				    while(string.indexOf(token) != -1) {
				        string = string.replace(token, newtoken);
				    }
				    return(string);
				}
		    </script>
		    <div id="chart_div" style="width: 900px; height: 500px;"></div>
		</section>
		
	</div><!-- End container -->
	
	<%@ include file="footer.jsp" %>
</body>
</html>
