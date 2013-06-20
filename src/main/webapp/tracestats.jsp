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
			<h2>Traceroute Stats!</h2>
			
			<h3>General info:</h3>
			<display:table id="asTracerouteAggregationStat1" name="asTracerouteAggregationStat" export="false" class="table table-striped">
					<display:column property="info" title="info" />
					<display:column property="timeStamp" title="timeStamp" />
					<display:column property="numberASTracerouteStats" title="number ASTraceroute Stats" />
					<display:column property="numberCompletedASTracerouteStats" title="number Completed ASTraceroute Stats" />
					
					<display:column property="percentageCompleted" title="percentage Completed" />
					<display:column property="originAS" title="origin AS" />
					<display:column property="destination" title="destination" />
					<display:column property="destinationAS" title="destination AS" />		
					
					<display:column property="originCity" title="origin City" />
					<display:column property="originCountry" title="origin Country" />
					<display:column property="destinationCity" title="destination City" />
					<display:column property="destinationCountry" title="destination Country" />	
			</display:table>
			
			<h3>Average:</h3>
			<display:table id="asTracerouteAggregationStat2" name="asTracerouteAggregationStat" export="false" class="table table-striped">
					<display:column property="averageNumberIpHops" title="average Number Ip Hops" />
					<display:column property="averageNumberASHops" title="average Number AS Hops" />
					<display:column property="averageNumberSiblingRelationships" title="Sibling Relationships" />
					<display:column property="averageNumberProviderRelationships" title="Provider Relationships" />
					<display:column property="averageNumberCustomerRelationships" title="Customer Relationships" />
					<display:column property="averageNumberPeeringRelationships" title="Peering Relationships" />
					<display:column property="averageNumberSameAsRelationships" title="Same As Relationships" />
					<display:column property="averageNumberNotFoundRelationships" title="Not Found Relationships" />
					<display:column property="averageNumberIxpInterconnectionRelationships" title="Ixp Interconnection Relationships" />
				
					<display:column property="averageNumberASes" title="number ASes" />
					<display:column property="averageNumberIXPs" title="number IXPs" />
					<display:column property="averageNumberASesInIXPs" title="number ASes In IXPs" />
			</display:table>
			
			<h3>Standard deviation:</h3>
			<display:table id="asTracerouteAggregationStat3" name="asTracerouteAggregationStat" export="false" class="table table-striped">
					<display:column property="stdeviationNumberIpHops" title="stdeviation Number Ip Hops" />
					<display:column property="stdeviationNumberASHops" title="stdeviation Number AS Hops" />
					<display:column property="stdeviationNumberSiblingRelationships" title="Sibling Relationships" />
					<display:column property="stdeviationNumberProviderRelationships" title="Provider Relationships" />
					<display:column property="stdeviationNumberCustomerRelationships" title="Customer Relationships" />
					<display:column property="stdeviationNumberPeeringRelationships" title="Peering Relationships" />
					<display:column property="stdeviationNumberSameAsRelationships" title="Same As Relationships" />
					<display:column property="stdeviationNumberNotFoundRelationships" title="Not Found Relationships" />
					<display:column property="stdeviationNumberIxpInterconnectionRelationships" title="Ixp Interconnection Relationships" />
				
					<display:column property="stdeviationNumberASes" title="number ASes" />
					<display:column property="stdeviationNumberIXPs" title="number IXPs" />
					<display:column property="stdeviationNumberASesInIXPs" title="number ASes In IXPs" />
			</display:table>
				
			<h3>Median:</h3>
			<display:table id="asTracerouteAggregationStat4" name="asTracerouteAggregationStat" export="false" class="table table-striped">
					<display:column property="medianNumberIpHops" title="median Number Ip Hops" />
					<display:column property="medianNumberASHops" title="median Number AS Hops" />
					<display:column property="medianNumberSiblingRelationships" title="Sibling Relationships" />
					<display:column property="medianNumberProviderRelationships" title="Provider Relationships" />
					<display:column property="medianNumberCustomerRelationships" title="Customer Relationships" />
					<display:column property="medianNumberPeeringRelationships" title="Peering Relationships" />
					<display:column property="medianNumberSameAsRelationships" title="Same As Relationships" />
					<display:column property="medianNumberNotFoundRelationships" title="Not Found Relationships" />
					<display:column property="medianNumberIxpInterconnectionRelationships" title="Ixp Interconnection Relationships" />
				
					<display:column property="medianNumberASes" title="number ASes" />
					<display:column property="medianNumberIXPs" title="number IXPs" />
					<display:column property="medianNumberASesInIXPs" title="number ASes In IXPs" />
			</display:table>
		</section>
		
		<section>
		    <div id="chart_div" style="width: 900px; height: 500px;"></div>
			
			<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		    <script type="text/javascript">
		      google.load('visualization', '1', {packages: ['corechart']});
		    </script>
		    <script type="text/javascript">
				function drawVisualization() {
				  // Create and populate the data table.
				  
				  
				  
				var data = google.visualization.arrayToDataTable([
						['ID', 'IQR', '', '', '', 'Median', 'Average'],
						['Number Ip hops', 
						   		<s:property value="asTracerouteAggregationStat.q1NumberIpHops"  escapeJavaScript="true" />-(<s:property value="asTracerouteAggregationStat.q3NumberIpHops"  escapeJavaScript="true" />-<s:property value="asTracerouteAggregationStat.q1NumberIpHops"  escapeJavaScript="true" />)*1.5, 
						   		<s:property value="asTracerouteAggregationStat.q1NumberIpHops"  escapeJavaScript="true" />, 
						   		<s:property value="asTracerouteAggregationStat.q3NumberIpHops"  escapeJavaScript="true" />, 
						   		<s:property value="asTracerouteAggregationStat.q3NumberIpHops"  escapeJavaScript="true" />+(<s:property value="asTracerouteAggregationStat.q3NumberIpHops"  escapeJavaScript="true" />-<s:property value="asTracerouteAggregationStat.q1NumberIpHops"  escapeJavaScript="true" />)*1.5, 
						   		<s:property value="asTracerouteAggregationStat.medianNumberIpHops"  escapeJavaScript="true" />, 
						   		<s:property value="asTracerouteAggregationStat.averageNumberIpHops"  escapeJavaScript="true" />],
						  
						['Number AS hops', 
							   	<s:property value="asTracerouteAggregationStat.q1NumberASHops"  escapeJavaScript="true" />-(<s:property value="asTracerouteAggregationStat.q3NumberASHops"  escapeJavaScript="true" />-<s:property value="asTracerouteAggregationStat.q1NumberASHops"  escapeJavaScript="true" />)*1.5, 
							   	<s:property value="asTracerouteAggregationStat.q1NumberASHops"  escapeJavaScript="true" />, 
							   	<s:property value="asTracerouteAggregationStat.q3NumberASHops"  escapeJavaScript="true" />, 
							   	<s:property value="asTracerouteAggregationStat.q3NumberASHops"  escapeJavaScript="true" />+(<s:property value="asTracerouteAggregationStat.q3NumberASHops"  escapeJavaScript="true" />-<s:property value="asTracerouteAggregationStat.q1NumberASHops"  escapeJavaScript="true" />)*1.5, 
							   	<s:property value="asTracerouteAggregationStat.medianNumberASHops"  escapeJavaScript="true" />, 
							   	<s:property value="asTracerouteAggregationStat.averageNumberASHops"  escapeJavaScript="true" />]
						  
		
						  // Treat first row as data as well.
						]);
				  // Create and draw the visualization.
				  var ac = new google.visualization.ComboChart(document.getElementById('chart_div'));
				  ac.draw(data, {
					title : 'Box Plot with Median and Average',
					width: 900,
					height: 400,
					vAxis: {title: "Value"},
					hAxis: {title: "Serie ID"},
					series: { 0: {type: "candlesticks"}, 1: {type: "line", pointSize: 10, lineWidth: 0 }, 2: {type: "line", pointSize: 10, lineWidth: 0, color: 'black' } }
				  });
				}
				google.setOnLoadCallback(drawVisualization);
		    </script>
		</section>
		
		<section>
	
				<h3>cityOriginMatchings:</h3>
				<display:table id="cityOriginMatchings" name="asTracerouteAggregationStat.cityOriginMatchings" pagesize="20" export="false" class="table table-striped">
						<display:column property="city" title="city" sortable="true"/>
						<display:column property="country" title="country" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				<h3>cityDestinationMatchings:</h3>
				<display:table id="cityDestinationMatchings" name="asTracerouteAggregationStat.cityDestinationMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="city" title="city" sortable="true"/>
						<display:column property="country" title="country" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				<h3>countryOriginMatchings:</h3>
				<display:table id="countryOriginMatchings" name="asTracerouteAggregationStat.countryOriginMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="country" title="country" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				<h3>countryDestinationMatchings:</h3>
				<display:table id="countryDestinationMatchings" name="asTracerouteAggregationStat.countryDestinationMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="country" title="country" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				
				
				<h3>ipOriginMatchings:</h3>
				<display:table id="ipOriginMatchings" name="asTracerouteAggregationStat.ipOriginMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="ip" title="ip" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				<h3>ipDestinationMatchings:</h3>
				<display:table id="ipDestinationMatchings" name="asTracerouteAggregationStat.ipDestinationMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="ip" title="ip" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				<h3>asOriginMatchings:</h3>
				<display:table id="asOriginMatchings" name="asTracerouteAggregationStat.asOriginMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="asNumber" title="asNumber" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
				<h3>asDestinationMatchings:</h3>
				<display:table id="asDestinationMatchings" name="asTracerouteAggregationStat.asDestinationMatchings" pagesize="20" export="false"  class="table table-striped">
						<display:column property="asNumber" title="asNumber" sortable="true"/>
						<display:column property="number" title="number" sortable="true"/>
				</display:table>
		</section>	
		
	</div><!-- End container -->
	
	<%@ include file="footer.jsp" %>
</body>
</html>
