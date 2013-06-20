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
			<h2>Traceroute Details!</h2>
			
			<display:table id="asTraceroute" name="asTraceroute" export="false" class="table table-condensed">
					
					<display:column property="tracerouteGroupId" title="Group Id"/>
					<display:column property="timeStamp" title="timeStamp" />
					
					<display:column property="originIp" title="origin Ip" />
						<display:column title="origin City" sortable="true">
							<a href="getASTracerouteStatsByOriginCity?originCity=<s:url value="%{#attr.asTraceroute.originCity}"/>&originCountry=<s:url value="%{#attr.asTraceroute.originCountry}"/>" >
								<s:url value="%{#attr.asTraceroute.originCity}"/>
							</a>
						</display:column>
					<display:column property="originCountry" title="origin Country" href="getASTracerouteStatsByOriginCountry" paramId="originCountry" paramProperty="originCountry" sortable="true" />
					<display:column property="originAS" title="origin AS" href="getASTracerouteStatsByOriginAS" paramId="originAS" paramProperty="originAS" />
					<display:column title="api">
						<a href="api/traceroute/getASTracerouteStatsByOriginAS/<s:url value="%{#attr.asTraceroute.originAS}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column property="originASName" title="origin AS Name" />
					<display:column property="destination" title="destination" href="getASTracerouteStatsByDestination" paramId="destination" paramProperty="destination" />
					<display:column title="api">
						<a href="api/traceroute/getASTracerouteStatsByDestination/<s:url value="%{#attr.asTraceroute.destination}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column property="destinationIp" title="destination Ip" />
					<display:column title="destination City" sortable="true" >
							<a href="getASTracerouteStatsByDestinationCity?destinationCity=<s:url value="%{#attr.asTraceroute.destinationCity}"/>&destinationCountry=<s:url value="%{#attr.asTraceroute.destinationCountry}"/>" >
								<s:url value="%{#attr.asTraceroute.destinationCity}"/>
							</a>
					</display:column>
					<display:column property="destinationCountry" title="destination Country" href="getASTracerouteStatsByDestinationCountry" paramId="destinationCountry" paramProperty="destinationCountry" sortable="true" />
					<display:column property="destinationAS" title="destination AS" href="getASTracerouteStatsByDestinationAS" paramId="destinationAS" paramProperty="destinationAS"/>	
					<display:column title="api">
						<a href="api/traceroute/getASTracerouteStatsByDestinationAS/<s:url value="%{#attr.asTraceroute.destinationAS}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column property="destinationASName" title="destination AS Name" />
					
					<display:column title="view traceroute" >
						<a href="api/traceroute/getASTraceroute/<s:url value="%{#attr.asTraceroute.tracerouteGroupId}"/>" ><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column title="view relationships" >
						<a href="api/traceroute/getASTracerouteRelationships/<s:url value="%{#attr.asTraceroute.tracerouteGroupId}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>
					<display:column title="view statistics" >
						<a href="api/traceroute/getASTracerouteStats/<s:url value="%{#attr.asTraceroute.tracerouteGroupId}"/>"><img alt="view" src="img/icn_alert_info.png" /></a>
					</display:column>		
			</display:table>
			
			<display:table id="asTracerouteStat1" name="asTracerouteStat" export="false" class="table table-condensed">
					<display:column property="completed" title="completed" />
					<display:column property="numberIpHops" title="number Ip Hops" />
					<display:column property="numberASHops" title="number AS Hops" />
					<display:column property="numberASes" title="number ASes" />
					<display:column property="numberIXPs" title="number IXPs" />
					<display:column property="numberASesInIXPs" title="number ASes In IXPs" />
			</display:table>
			
			<display:table id="asTracerouteStat2" name="asTracerouteStat" export="false" class="table table-condensed">
					<display:column property="numberSiblingRelationships" title="Sibling Relationships" />
					<display:column property="numberProviderRelationships" title="Provider Relationships" />
					<display:column property="numberCustomerRelationships" title="Customer Relationships" />
					<display:column property="numberPeeringRelationships" title="Peering Relationships" />
					<display:column property="numberSameAsRelationships" title="SameAs Relationships" />
					<display:column property="numberNotFoundRelationships" title="Not Found Relationships" />
					<display:column property="numberIxpInterconnectionRelationships" title="Ixp Interconnection Relationships" />
			</display:table>
			
		</section>
		
		<section>
			<div class="row-fluid">  
				<div class="span3"><div id="chart_div0"></div></div>
			</div>
			<div class="row-fluid"> 
				<div class="span3"><div id="chart_div1"></div></div>
				<div class="span3"><div id="chart_div11"></div></div>		
				<div class="span3"><div id="chart_div2"></div> </div>
				<div class="span3"><div id="chart_div21"></div> </div>
			</div> 
		</section>

		<section>
		<div class="row-fluid">
			<div class="span5">
				<input type="button" value="Format Traceroute JSON" onclick="formatJson(document.getElementById('traceroute').value,'traceroute')">
				<s:textarea id="traceroute" label="Traceroute" value="%{asTracerouteJson}" style="height: 800px; width: 90%;"/>
			</div>
			<div class="span5">
				<input type="button" value="Format Relationships JSON" onclick="formatJson(document.getElementById('relations').value,'relations')">
				<s:textarea id="relations" label="Relationships" value="%{asTracerouteRelationshipsJson}" style="height: 800px; width: 90%;"/>
			</div>
		</div>
		
		
		
		
		<script type="text/javascript">
		
		function formatJson(val,id) {
			var retval = '';
			var str = val;
		    var pos = 0;
		    var strLen = str.length;
			var indentStr = '\t';
		    var newLine = '\n';
			var char = '';
			
			for (var i=0; i<strLen; i++) {
				char = str.substring(i,i+1);
				
				if (char == '}' || char == ']') {
					retval = retval + newLine;
					pos = pos - 1;
					
					for (var j=0; j<pos; j++) {
						retval = retval + indentStr;
					}
				}
				
				retval = retval + char;	
				
				if (char == '{' || char == '[' || char == ',') {
					retval = retval + newLine;
					
					if (char == '{' || char == '[') {
						pos = pos + 1;
					}
					
					for (var k=0; k<pos; k++) {
						retval = retval + indentStr;
					}
				}
			}
			document.getElementById(id).value=retval;
			return retval;
		}
		
		</script>
		
			<script type="text/javascript" src="https://www.google.com/jsapi"></script>
			    <script type="text/javascript">
		
		      // Load the Visualization API and the piechart package.
		      google.load('visualization', '1.0', {'packages':['corechart']});
		
		      // Set a callback to run when the Google Visualization API is loaded.
		      google.setOnLoadCallback(drawChart);
		
		      // Callback that creates and populates a data table,
		      // instantiates the pie chart, passes in the data and
		      // draws it.
		      function drawChart() {
		
		        // Create the data table.
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Hops');
		        data.addColumn('number', '#');
		        data.addRows([
		          ['numberIpHops',  <s:property value="asTracerouteStat.numberIpHops"  escapeJavaScript="true" />],
		          ['numberASHops', <s:property value="asTracerouteStat.numberASHops"  escapeJavaScript="true" />]
		        ]);
		
		        // Set chart options
		        var options = {'title':'Number of Hops',
		                       'width':400,
		                       'height':300};
		
		        // Instantiate and draw our chart, passing in some options.
		        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div0'));
		        chart.draw(data, options);
		      }
		    </script>
		
			
			
		    <script type="text/javascript">
		
		      // Load the Visualization API and the piechart package.
		      google.load('visualization', '1.0', {'packages':['corechart']});
		
		      // Set a callback to run when the Google Visualization API is loaded.
		      google.setOnLoadCallback(drawChart);
		
		      // Callback that creates and populates a data table,
		      // instantiates the pie chart, passes in the data and
		      // draws it.
		      function drawChart() {
		
		        // Create the data table.
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Relationships');
		        data.addColumn('number', '#');
		        data.addRows([
		          ['numberSiblingRelationships',  <s:property value="asTracerouteStat.numberSiblingRelationships"  escapeJavaScript="true" />],
		          ['numberProviderRelationships', <s:property value="asTracerouteStat.numberProviderRelationships"  escapeJavaScript="true" />],
		          ['numberCustomerRelationships', <s:property value="asTracerouteStat.numberCustomerRelationships"  escapeJavaScript="true" />],
		          ['numberPeeringRelationships', <s:property value="asTracerouteStat.numberPeeringRelationships"  escapeJavaScript="true" />],
		          ['numberSameAsRelationships', <s:property value="asTracerouteStat.numberSameAsRelationships"  escapeJavaScript="true" />],
		          ['numberNotFoundRelationships', <s:property value="asTracerouteStat.numberNotFoundRelationships"  escapeJavaScript="true" />],
		          ['numberIxpInterconnectionRelationships', <s:property value="asTracerouteStat.numberIxpInterconnectionRelationships"  escapeJavaScript="true" />]
		        ]);
		
		        // Set chart options
		        var options = {'title':'AS Relationships',
		                       'width':400,
		                       'height':300};
		
		        // Instantiate and draw our chart, passing in some options.
		        var chart = new google.visualization.PieChart(document.getElementById('chart_div1'));
		        chart.draw(data, options);
		      }
		    </script>    
		
		
		    <script type="text/javascript">
		
		      // Load the Visualization API and the piechart package.
		      google.load('visualization', '1.0', {'packages':['corechart']});
		
		      // Set a callback to run when the Google Visualization API is loaded.
		      google.setOnLoadCallback(drawChart);
		
		      // Callback that creates and populates a data table,
		      // instantiates the pie chart, passes in the data and
		      // draws it.
		      function drawChart() {
		
		        // Create the data table.
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Relationships');
		        data.addColumn('number', '#');
		        data.addRows([
		          ['numberSiblingRelationships',  <s:property value="asTracerouteStat.numberSiblingRelationships"  escapeJavaScript="true" />],
		          ['numberProviderRelationships', <s:property value="asTracerouteStat.numberProviderRelationships"  escapeJavaScript="true" />],
		          ['numberCustomerRelationships', <s:property value="asTracerouteStat.numberCustomerRelationships"  escapeJavaScript="true" />],
		          ['numberPeeringRelationships', <s:property value="asTracerouteStat.numberPeeringRelationships"  escapeJavaScript="true" />],
		          ['numberSameAsRelationships', <s:property value="asTracerouteStat.numberSameAsRelationships"  escapeJavaScript="true" />],
		          ['numberNotFoundRelationships', <s:property value="asTracerouteStat.numberNotFoundRelationships"  escapeJavaScript="true" />],
		          ['numberIxpInterconnectionRelationships', <s:property value="asTracerouteStat.numberIxpInterconnectionRelationships"  escapeJavaScript="true" />]
		        ]);
		
		        // Set chart options
		        var options = {'title':'AS Relationships',
		                       'width':400,
		                       'height':300};
		
		        // Instantiate and draw our chart, passing in some options.
		        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div11'));
		        chart.draw(data, options);
		      }
		    </script>
		   
		
		    <script type="text/javascript">
		
		      // Load the Visualization API and the piechart package.
		      google.load('visualization', '1.0', {'packages':['corechart']});
		
		      // Set a callback to run when the Google Visualization API is loaded.
		      google.setOnLoadCallback(drawChart);
		
		      // Callback that creates and populates a data table,
		      // instantiates the pie chart, passes in the data and
		      // draws it.
		      function drawChart() {
		
		        // Create the data table.
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Relationships');
		        data.addColumn('number', '#');
		        data.addRows([
		          ['numberASes',  <s:property value="asTracerouteStat.numberASes"  escapeJavaScript="true" />],
		          ['numberIXPs', <s:property value="asTracerouteStat.numberIXPs"  escapeJavaScript="true" />],
		          ['numberASesInIXPs', <s:property value="asTracerouteStat.numberASesInIXPs"  escapeJavaScript="true" />]
		        ]);
		
		        // Set chart options
		        var options = {'title':'AS Types',
		                       'width':400,
		                       'height':300};
		
		        // Instantiate and draw our chart, passing in some options.
		        var chart = new google.visualization.PieChart(document.getElementById('chart_div2'));
		        chart.draw(data, options);
		      }
		    </script>    
		    
		    
		    <script type="text/javascript">
		
		      // Load the Visualization API and the piechart package.
		      google.load('visualization', '1.0', {'packages':['corechart']});
		
		      // Set a callback to run when the Google Visualization API is loaded.
		      google.setOnLoadCallback(drawChart);
		
		      // Callback that creates and populates a data table,
		      // instantiates the pie chart, passes in the data and
		      // draws it.
		      function drawChart() {
		
		        // Create the data table.
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Relationships');
		        data.addColumn('number', '#');
		        data.addRows([
		          ['numberASes',  <s:property value="asTracerouteStat.numberASes"  escapeJavaScript="true" />],
		          ['numberIXPs', <s:property value="asTracerouteStat.numberIXPs"  escapeJavaScript="true" />],
		          ['numberASesInIXPs', <s:property value="asTracerouteStat.numberASesInIXPs"  escapeJavaScript="true" />]
		        ]);
		
		        // Set chart options
		        var options = {'title':'AS Types',
		                       'width':400,
		                       'height':300};
		
		        // Instantiate and draw our chart, passing in some options.
		        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div21'));
		        chart.draw(data, options);
		      }
		    </script>  
    	</section>
    
    </div><!-- End container -->
	
	<%@ include file="footer.jsp" %>

</body>
</html>
