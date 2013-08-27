package edu.upf.nets.mercury.applet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import netscape.javascript.JSObject;

public class TracerouteLatencyApplet extends JApplet implements ActionListener {

	private static final Logger log = Logger
			.getLogger(TracerouteLatencyApplet.class.getName());
	private static final long serialVersionUID = 1L;
	String locale = "";
	JTextField tfLocale;
	JTextArea taURLs;
	JButton bTraceroute;
	JButton bURLs;
	JTextArea taTraceroute;
	JTextArea taLatency;
	JTabbedPane tabbedPane;
	JTable tableLatency;
	DefaultTableModel model;

	public void init() {
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Traceroute", getTraceroutePanel());
		tabbedPane.addTab("Latency", getLatencyPanel());
		this.add(tabbedPane);
		this.setSize(640, 640);
	}
	
	private JPanel getTraceroutePanel(){
		locale = this.getLocale().getCountry();
		tfLocale = new JTextField(locale);
		taURLs = new JTextArea();
		JScrollPane spURLs = new JScrollPane(taURLs);
		spURLs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spURLs.setPreferredSize(new Dimension(480, 160));
		spURLs.setBorder(BorderFactory.createTitledBorder("URLs"));
		bURLs = new JButton("get URLs");
		bURLs.addActionListener(this);
		bTraceroute = new JButton("execute Traceroute!");
		bTraceroute.addActionListener(this);
		taTraceroute = new JTextArea();
		JScrollPane sp = new JScrollPane(taTraceroute);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setPreferredSize(new Dimension(480, 320));
		sp.setBorder(BorderFactory.createTitledBorder("Output"));

		JPanel myPanel = new JPanel();
		myPanel.add(tfLocale);
		myPanel.add(spURLs);
		myPanel.add(bURLs);
		myPanel.add(bTraceroute);
		myPanel.add(sp);
		
		return myPanel;
	}

	private String getURLs(){
	
		String URLs = "";
		try{
			//Connect to the Web Service to download a list of urls for a selected country
			locale = tfLocale.getText();
			log.info("Getting URLs for locale: "+locale);
			URLs = "google.com\nfacebook.com\ntwitter.com\namazon.com\nyahoo.com\n";
		} catch(Exception e){
			URLs = "google.com\nfacebook.com\ntwitter.com\namazon.com\nyahoo.com\n";
		}
		
		return URLs;
	}
	
	
	private JPanel getLatencyPanel(){
		
		model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[]{"destination","latency [ms]","average [ms]","samples"});
		model.addRow(new String[]{"hola","adios","hello","bye"});
		tableLatency = new JTable(model);
		//tableLatency.update(tableLatency.getGraphics());
		JScrollPane spLatency = new JScrollPane(tableLatency);
		spLatency.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spLatency.setPreferredSize(new Dimension(480, 320));
		spLatency.setBorder(BorderFactory.createTitledBorder("Output"));

		JPanel myPanel = new JPanel();
		myPanel.add(spLatency);
		
		return myPanel;
	}
	
	public void exec(String[] command) {

		// JSObject win = (JSObject) JSObject.getWindow(this);
		// String jsOutput =
		// "var output = document.createElement('div');output.innerHTML = \"<h3>Output</h3>\";document.body.appendChild(output);";
		// win.eval(jsOutput);
		try {

			ProcessBuilder builder = new ProcessBuilder(command);
			final Process process = builder.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			List<String> lines = new ArrayList<String>();
			
			while ((line = br.readLine()) != null) {
				log.info(line);
				// jsOutput =
				// "var hop = document.createElement('div');hop.innerHTML = \""+line+"<br />\";document.body.appendChild(hop);";
				// win.eval(jsOutput);
				taTraceroute.append(line + "\n");
				taTraceroute.update(taTraceroute.getGraphics());
				
				lines.add(line);
			}

			int retval = process.waitFor();
			log.info("Retval: " + retval);

			// Patterns
			String patternHop = "(^\\s+\\d{1,2}|^\\d{1,2})";
			String patternIp = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";

			Matcher matcherReply;
			String firstLine;
			String traceroute = "{\"destination\":\"" + command[6]
					+ "\",\"hops\":[]}";
			// traceroute.setDestination(command[1]);
			// When traceroute process finishes, we analyze the output line per
			// line
			if (retval == 0) {
				// We obtain the destination IP from the first line
				if ((firstLine = lines.get(0)) != null) {
					// Workaround for Windows. Data starts at second line
					if ((firstLine = lines.get(0)).equals("")) {
						if ((firstLine = lines.get(1)) != null) {
							if ((matcherReply = Pattern.compile(patternIp)
									.matcher(firstLine)).find()) {
								firstLine = matcherReply.group(0);
							}
						}
					} else {
						if ((matcherReply = Pattern.compile(patternIp).matcher(
								firstLine)).find()) {
							firstLine = matcherReply.group(0);
						}
					}
				}
				// We process each hop
				String hops = "";
				for (String lineAux : lines) {
					String hop;
					if ((matcherReply = Pattern.compile(patternHop).matcher(
							lineAux)).find()) {
						String hopId = matcherReply.group(0).trim();
						// hop.setId(matcherReply.group(0).trim()); //We add the
						// hop Id
						if ((matcherReply = Pattern.compile(patternIp).matcher(
								lineAux)).find()) {
							hop = "{\"id\":\"" + hopId + "\",\"ip\":\""
									+ matcherReply.group(0) + "\" }";
							// hop.setIp(matcherReply.group(0)); //We add the
							// hop Ip
						} else {
							hop = "{\"id\":\"" + hopId
									+ "\",\"ip\":\"destination unreachable\" }";
							// hop.setIp("host unreachable"); //We add the hop
							// Ip unreachable
						}
						// traceroute.addHops(hop);
						if (hops.equals("")) {
							hops = hop;
						} else {
							hops = hops + "," + hop;
						}
					}
				}
				traceroute = "{\"destination\":\"" + command[6]
						+ "\",\"ip\":\"" + firstLine + "\",\"hops\":[" + hops
						+ "]}";
			}

			// JSObject doc = (JSObject) win.getMember("document");
			// JSObject loc = (JSObject) doc.getMember("location");
			// String hostname = (String) loc.getMember("hostname");
			// String port = (String) loc.getMember("port");

			// Now we upload the traceroute to the server
			String hostname = InetAddress.getLocalHost().getHostName();
			String port = "80";
			String result = postData(hostname, port, traceroute);
			taTraceroute.append(result + "\n\n");

			// Getting the Traceroute Object. Be careful cause JavaScript has
			// problems with \n character
			// String jsTraceroute =
			// "var traceroute = document.createElement('div');traceroute.innerHTML = \"<h3>Server response</h3> <p>"+result+"</p>\";document.body.appendChild(traceroute);";
			// jsTraceroute = jsTraceroute.replaceAll("\n", "<br/>");
			// win.eval(jsTraceroute);

			// Here we have to add code to allow user to signup!

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public String postData(String hostname, String port, String data) {

		String result;
		String request = "http://" + hostname + ":" + port
				+ "/mercury/api/traceroute/uploadTrace";
		try {
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);

			connection.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			int status = connection.getResponseCode();
			log.info("Status: " + status);
			if ((status == 200) || (status == 201)) {
				log.info("Added traceroute data!");
				result = "Added traceroute data! Thanks for participating.";
			} else {
				log.info("Problems adding entry. Server response status: "
						+ status);
				result = "Problems adding traceroute data. Try again later. Server response status: "
						+ status;
			}

			connection.disconnect();

		} catch (Exception e) {
			result = "Problems adding entry. i.e. Server not reachable";
		}
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == bTraceroute) {
			// We collect the destinations target from the textBox
			String[] destinations = taURLs.getText().split("\n");
			for (String destination : destinations) {
				traceroute(destination);
				String latency;
				try {
					latency = String.valueOf(executeAsynCall("http://"+destination));
				} catch (Exception e1) {
					e1.printStackTrace();
					latency = "*";
				} 
				model.addRow(new String[]{destination, latency ,"*","*"});
				
			}
		} else if (e.getSource() == bURLs){
			taURLs.setText(getURLs());
		}
	}
	
	private void traceroute(String destination){
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("windows") != -1) {
			// -d to avoid name resolution
			String[] command = { "tracert", "-d", destination }; 
			exec(command);
		} else if (osName.indexOf("mac os x") != -1) {
			// -n to avoid name resolution, -w to set waittime, -m to set the max TTL
			String[] command = { "traceroute", "-n", "-w", "1", "-m", "30", destination };
			exec(command);
		} else {
			// -n to avoid name resolution
			String[] command = { "traceroute", "-n", destination }; 
			exec(command);
		}
		log.info(osName);
	}
	
	private long executeAsynCall(String domain) throws InterruptedException, IOException{
		
		List<String> resources = new ArrayList<String>();
        
        print("Fetching %s...", domain);


        Document doc = Jsoup.connect(domain).get();
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if(src.tagName().equals("img")){
                resources.add(src.attr("abs:src"));
            }else{
                resources.add(src.attr("abs:src"));
            }
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            resources.add(link.attr("abs:href"));
        }
        print("Number of resources: (%d)",resources.size());
		
		
		
		RequestConfig requestConfig;
		CloseableHttpAsyncClient httpClient;

		// Create the connection manager.
		PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
				new DefaultConnectingIOReactor());
		connectionManager.setMaxTotal(256);
		connectionManager.setDefaultMaxPerRoute(6);
		// Create the request configuration.
		requestConfig = RequestConfig.custom().setSocketTimeout(3000)
				.setConnectTimeout(3000).build();
		// Create the HTTP client.
		httpClient = HttpAsyncClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig).build();

		// Start the HTTP client.
		// create an array of URIs to perform GETs on
        String[] urls = resources.toArray(new String[resources.size()]);
		//String[] urls = new String[] { "http://www.google.com/","http://www.microsoft.com/", "http://www.apple.com/" };

		httpClient.start();
		
		
		long finish = 0;
		long start = 0;
		try {
			start = new Date().getTime();
			

			/* Request main page. */
			// Create a counter.
			final CountDownLatch counterMainPage = new CountDownLatch(1);
			// Create a new HTTP get request.
			final HttpGet requestDomain = new HttpGet(domain);
			// Show a message.
//			System.out.println("Request for main domain: " + domain);
			// Execute the request asynchronously.
			httpClient.execute(requestDomain, new FutureCallback<HttpResponse>() {
				@Override
				public void cancelled() {
					// TODO Auto-generated method stub
//					print(requestDomain.getRequestLine()+ " cancelled");
					// Decrement the counter.
					counterMainPage.countDown();
				}
				@Override
				public void completed(HttpResponse response) {
					// TODO Auto-generated method stub
//					print(requestDomain.getRequestLine() + "->"
//							+ response.getStatusLine() + " ("
//							+ response.getEntity().getContentLength()
//							+ " bytes received)");
					// Decrement the counter.
					counterMainPage.countDown();
				}
				@Override
				public void failed(Exception exception) {
					// TODO Auto-generated method stub
//					print(requestDomain.getRequestLine() + "->"+ exception);
					// Decrement the counter.
					counterMainPage.countDown();
				}
			});
			// Wait for the counter to complete.
			counterMainPage.await();
			
			
			/* Request resources. */
			// Create a counter.
			final CountDownLatch counterResources = new CountDownLatch(urls.length);
			// For each URL in the URLs list.
			for (final String url : urls) {
				// Create a new HTTP get request.
				final HttpGet request = new HttpGet(url);
				// Show a message.
//				print("Request for: " + url);
				// Execute the request asynchronously.
				httpClient.execute(request, new FutureCallback<HttpResponse>() {
					@Override
					public void cancelled() {
						// TODO Auto-generated method stub
//						print(request.getRequestLine()+ " cancelled");
						// Decrement the counter.
						counterResources.countDown();
					}
					@Override
					public void completed(HttpResponse response) {
						// TODO Auto-generated method stub
//						print(request.getRequestLine() + "->"
//								+ response.getStatusLine() + " ("
//								+ response.getEntity().getContentLength()
//								+ " bytes received)");
						// Decrement the counter.
						counterResources.countDown();
					}
					@Override
					public void failed(Exception exception) {
						// TODO Auto-generated method stub
//						print(request.getRequestLine() + "->"+ exception);
						// Decrement the counter.
						counterResources.countDown();
					}
				});
			}
			// Wait for the counter to complete.
			counterResources.await();
			
			finish = new Date().getTime();
			//log.info("Total time: "+ (finish-start));
		} finally {
			// Stop the HTTP client.
			httpClient.close();
			
		}
		return (finish-start);
	}
    
    
    private void print(String msg, Object... args) {
        log.info(String.format(msg, args));
    }
}
