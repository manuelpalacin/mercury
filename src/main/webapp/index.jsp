<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
    <link href="css/docs.css" rel="stylesheet">
    <link href="css/prettify.css" rel="stylesheet">
</head>

<body>
<%-- <h2>Hello World!</h2> --%>
<%-- <s:form action="helloWorld" method="POST" enctype="multipart/form-data"> --%>
<%-- 	<s:textfield name="username" label="Username"/> --%>
<%-- 	<s:textfield name="password" label="Password"/> --%>
<%-- 	<s:file name="photo" label="Select a File to upload" size="40" /> --%>
<%-- 	<s:submit /> --%>
<%-- </s:form> --%>


	<div class="navbar navbar-inverse navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="brand" href="./index.html">Mercury</a>
	          <div class="nav-collapse collapse">
	            <ul class="nav">
					<li class="active"><a href="index.jsp"> Home</a></li>
				    <li><a href="traceroute.jsp"> Participate!</a></li>
				    <li><a href="getLastTraceroutes"> View data</a></li>
				    <li><a href="mercury.html"> Platform information</a></li>
				    <li><a href="api.html"> API</a></li>
				    <li><a href="partners.html"> Partners</a></li>
				    <li><a href="about.html"> About</a></li>
	            </ul>
	          </div>
	        </div>
	      </div>
    </div>

	<header class="jumbotron subhead" id="overview">
	  <div class="container">
	    <h1>Mercury</h1>
	    <p class="lead">Inspecting the core of the Internet.</p>
	  </div>
	</header>

	<div class="container">
		
		    <!-- Docs nav
	    ================================================== -->
	    <div class="row">
		      <div class="span3 bs-docs-sidebar">
			        <ul class="nav nav-list bs-docs-sidenav">
			          <li><a href="index.jsp"><i class="icon-chevron-right"></i> Home</a></li>
			          <li><a href="traceroute.jsp"><i class="icon-chevron-right"></i> Participate!</a></li>
			          <li><a href="getLastTraceroutes"><i class="icon-chevron-right"></i> View data</a></li>
			          <li><a href="mercury.html"><i class="icon-chevron-right"></i> Platform information</a></li>
			          <li><a href="api.html"><i class="icon-chevron-right"></i> API</a></li>
			          <li><a href="partners.html"><i class="icon-chevron-right"></i> Partners</a></li>
			          <li><a href="about.html"><i class="icon-chevron-right"></i> About</a></li>
			        </ul>
		      </div>
		      
		      <div class="span9">
	
					<section>
						<div class="hero-unit">
						  <h1>Welcome to Mercury!</h1>
						  <p>Mercury is a web tool for inspecting Internet route statistics.</p>
						  <p>
						    <a class="btn btn-primary btn-large">
						      Learn more
						    </a>
						  </p>
						</div>
					</section>
				
				
					<section>
						<div class="row-fluid">
			            <ul class="thumbnails">
			              <li class="span4">
			                <div class="thumbnail">
			                  <img data-src="holder.js/300x200" alt="300x200" style="width: 300px; height: 200px;" src="images/world-home.png">
			                  <div class="caption">
			                    <h3>Participate!</h3>
			                    <p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
			                    <p><a href="traceroute.jsp" class="btn">Participate</a></p>
			                  </div>
			                </div>
			              </li>
			              <li class="span4">
			                <div class="thumbnail">
			                  <img data-src="holder.js/300x200" alt="300x200" style="width: 300px; height: 200px;" src="images/stats-home.png">
			                  <div class="caption">
			                    <h3>View data!</h3>
			                    <p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
			                    <p><a href="getLastTraceroutes" class="btn">View data</a></p>
			                  </div>
			                </div>
			              </li>
			              <li class="span4">
			                <div class="thumbnail">
			                  <img data-src="holder.js/300x200" alt="300x200" style="width: 300px; height: 200px;" src="images/cloud-home.png">
			                  <div class="caption">
			                    <h3>Develop!</h3>
			                    <p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
			                    <p><a href="api.html" class="btn">View API</a></p>
			                  </div>
			                </div>
			              </li>
			            </ul>
			          </div>
					</section>
			
			</div><!-- End span9 -->
		</div><!--End row  -->
	
	</div><!-- End container -->
	
	
	<!-- Footer
    ================================================== -->
    <footer class="footer">
      <div class="container">
        <p>Designed and built with all the love in the world by <a href="http://www.nets.upf.edu/node/314" target="_blank">Manuel Palacin</a>.</p>
        <p>Code licensed under <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank">Apache License v2.0</a>, documentation under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>

        <ul class="footer-links">
          <li><a href="http://www.upf.edu">Universitat Pompeu Fabra</a></li>
          <li class="muted">&middot;</li>
          <li><a href="http://nets.upf.edu">NeTS Research Group</a></li>
        </ul>
      </div>
    </footer>
	
	<script src="js/bootstrap-dropdown.js"></script>
	
</body>
</html>
