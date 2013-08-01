package edu.upf.nets.mercury.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
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
    

    //@Ignore
	@Test
	public void caller() throws InterruptedException, IOException{
		String[] domains = {"http://www.orange.es","http://www.upf.edu","http://www.apple.com"};
		for (String domain : domains) {
			log.info("Execution time for "+domain+": "+ executeAsynCall(domain) +" ms");
		}
		
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
					System.out.println(requestDomain.getRequestLine()+ " cancelled");
					// Decrement the counter.
					counterMainPage.countDown();
				}
				@Override
				public void completed(HttpResponse response) {
					// TODO Auto-generated method stub
//					System.out.println(requestDomain.getRequestLine() + "->"
//							+ response.getStatusLine() + " ("
//							+ response.getEntity().getContentLength()
//							+ " bytes received)");
					// Decrement the counter.
					counterMainPage.countDown();
				}
				@Override
				public void failed(Exception exception) {
					// TODO Auto-generated method stub
//					System.out.println(requestDomain.getRequestLine() + "->"+ exception);
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
//				System.out.println("Request for: " + url);
				// Execute the request asynchronously.
				httpClient.execute(request, new FutureCallback<HttpResponse>() {
					@Override
					public void cancelled() {
						// TODO Auto-generated method stub
//						System.out.println(request.getRequestLine()+ " cancelled");
						// Decrement the counter.
						counterResources.countDown();
					}
					@Override
					public void completed(HttpResponse response) {
						// TODO Auto-generated method stub
//						System.out.println(request.getRequestLine() + "->"
//								+ response.getStatusLine() + " ("
//								+ response.getEntity().getContentLength()
//								+ " bytes received)");
						// Decrement the counter.
						counterResources.countDown();
					}
					@Override
					public void failed(Exception exception) {
						// TODO Auto-generated method stub
//						System.out.println(request.getRequestLine() + "->"+ exception);
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
