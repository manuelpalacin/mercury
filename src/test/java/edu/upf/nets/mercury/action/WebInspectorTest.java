package edu.upf.nets.mercury.action;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.client.config.RequestConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class WebInspectorTest {

	private static final Logger log = Logger.getLogger(WebInspectorTest.class.getName());

	@Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Ignore
	@Test
	public void executeWebInspectorTest() throws IOException{
		log.info("executeWebInspectorTest!");
		List<String> resources = new ArrayList<String>();
    	
        String url = "http://www.orange.es";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();

        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img")){

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
        long totalTime = 0;
     
        Set<String> servers = new HashSet<String>();
        for (String resource : resources) {
        	URL urlResource = new URL(resource);
			servers.add(InetAddress.getByName(urlResource.getHost()).getHostAddress());
		}
        log.info("Total time: "+totalTime);
        
        
        print("Number of servers: (%d)",servers.size());
        for (String server : servers) {
			log.info("Server: "+server);
		}
		
        
        /*Calling the resources*/
        resources.add(url); //We add the original site
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
    	// Increase max total connection to 256 
    	cm.setMaxTotal(256); 
    	// Increase default max connection per route to 6 
    	cm.setDefaultMaxPerRoute(6);

        HttpClient httpclient = new DefaultHttpClient(cm);
        

        
        try {
            // create an array of URIs to perform GETs on
            String[] urisToGet = resources.toArray(new String[resources.size()]);

            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            for (int i = 0; i < threads.length; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                threads[i] = new GetThread(httpclient, httpget, i + 1);
            }

            // start the threads
            long start = new Date().getTime();
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
            }

            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }
            //end
            long end = new Date().getTime();
            System.out.println("TOTAL TIME: "+ (end-start) +" ms");

        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        

    	
    }
    
    private void print(String msg, Object... args) {
        log.info(String.format(msg, args));
    }
    

    /**
     * A thread that performs a GET.
     */
    class GetThread extends Thread {

        private final HttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;

        public GetThread(HttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {

            //System.out.println(id + " - about to get something from " + httpget.getURI());

            try {

                // execute the method
                HttpResponse response = httpClient.execute(httpget, context);

                //System.out.println(id + " - get executed");
                // get the response body as an array of bytes
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    //System.out.println(id + " - " + bytes.length + " bytes read");
                }

            } catch (Exception e) {
                httpget.abort();
                System.out.println(id + " - error: " + e);
            }
        }

    }

    
    @Ignore
	@Test
    public void executeCalls() throws Exception {
		
		List<String> resources = new ArrayList<String>();
    	
        String url = "http://www.orange.es";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();

        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img")){

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
		
		
		
		//START HTTP CLIENT
        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor());
		connectionManager.setMaxTotal(1);
		connectionManager.setDefaultMaxPerRoute(1);
        final RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(3000)
            .setConnectTimeout(3000).build();
        final CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build();

        httpclient.start();
        try {
        	
        	// create an array of URIs to perform GETs on
            String[] urisToGet = resources.toArray(new String[resources.size()]);
            // create a thread for each URI
            HttpGet[] requests = new HttpGet[urisToGet.length];
            for (int i = 0; i < requests.length; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                requests[i] = httpget;
            }
        	
        	
//            final HttpGet[] requests = new HttpGet[] {
//                    new HttpGet("http://www.apache.org/"),
//                    new HttpGet("https://www.verisign.com/"),
//                    new HttpGet("http://www.google.com/")
//            };
            final CountDownLatch latch = new CountDownLatch(requests.length);
            for (final HttpGet request: requests) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {

                    public void completed(final HttpResponse response) {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                    }

                    public void failed(final Exception ex) {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + ex);
                    }

                    public void cancelled() {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + " cancelled");
                    }

                });
            }
            latch.await();
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }


    //@Ignore
	@Test
    public void executeAsynCall() throws InterruptedException, IOException{
		
		List<String> resources = new ArrayList<String>();
        String domain = "http://www.orange.es";
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
		connectionManager.setMaxTotal(10);
		connectionManager.setDefaultMaxPerRoute(1);
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

		try {
			// Create a counter.
			final CountDownLatch counter = new CountDownLatch(urls.length);
			// For each URL in the URLs list.
			for (final String url : urls) {
				// Create a new HTTP get request.
				final HttpGet request = new HttpGet(url);
				// Show a message.
				System.out.println("Reqquest for: " + url);
				// Execute the request asynchronously.
				httpClient.execute(request, new FutureCallback<HttpResponse>() {

					@Override
					public void cancelled() {
						// TODO Auto-generated method stub
						System.out.println(request.getRequestLine()
								+ " cancelled");
						// Decrement the counter.
						counter.countDown();
					}

					@Override
					public void completed(HttpResponse response) {
						// TODO Auto-generated method stub
						System.out.println(request.getRequestLine() + "->"
								+ response.getStatusLine() + " ("
								+ response.getEntity().getContentLength()
								+ " bytes received)");
						// Decrement the counter.
						counter.countDown();
					}

					@Override
					public void failed(Exception exception) {
						// TODO Auto-generated method stub
						System.out.println(request.getRequestLine() + "->"
								+ exception);
						// Decrement the counter.
						counter.countDown();
					}
				});
			}
			// Wait for the counter to complete.
			counter.await();
		} finally {
			// Stop the HTTP client.
			httpClient.close();
		}

	}
    

}
