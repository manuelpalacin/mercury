package edu.upf.nets.mercury.applet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


import netscape.javascript.JSObject;


public class TracerouteApplet extends JApplet implements ActionListener{
	
	private static final Logger log = Logger.getLogger(TracerouteApplet.class.getName());
	private static final long serialVersionUID = 1L;
	JButton b;
	JButton be;
	JTextField t;
	
	
	public void init() {
		
	     t = new JTextField(20);
	     b = new JButton("execute Traceroute!");
	     b.addActionListener(this);
	     be = new JButton("execute Express Traceroute!");
	     be.addActionListener(this);
	     
	     JPanel myPanel = new JPanel(); 
	     myPanel.add(t);
	     myPanel.add(b);
	     myPanel.add(be);
	     this.add(myPanel);
	     
		
	}

	public void exec(String[] command) {

		JSObject win = (JSObject) JSObject.getWindow(this);
		String jsOutput = "var output = document.createElement('div');output.innerHTML = \"<h3>Output</h3>\";document.body.appendChild(output);";
		win.eval(jsOutput);
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
		    	jsOutput = "var hop = document.createElement('div');hop.innerHTML = \""+line+"<br />\";document.body.appendChild(hop);";
		        win.eval(jsOutput);

		    	lines.add(line);
		    }
		    
		    int retval = process.waitFor();
			log.info("Retval: "+retval);
		    
			//Patterns
			String patternHop = "(^\\s+\\d{1,2}|^\\d{1,2})";
			String patternIp = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
			
			Matcher matcherReply;
			String firstLine;
			String traceroute = "{\"destination\":\""+command[2]+"\",\"hops\":[]}";
			//traceroute.setDestination(command[1]);
			//When traceroute process finishes, we analyze the output line per line
			if(retval==0){
				//We obtain the destination IP from the first line
				if ( (firstLine=lines.get(0)) != null ){
					//Workaround for Windows. Data starts at second line
					if ((firstLine=lines.get(0)).equals("")){
						if ( (firstLine=lines.get(1)) != null ){
							if ((matcherReply = Pattern.compile(patternIp).matcher(firstLine)).find()){
								firstLine = matcherReply.group(0);
							}
						}
					} else {
						if ((matcherReply = Pattern.compile(patternIp).matcher(firstLine)).find()){
							firstLine = matcherReply.group(0);
						}
					}
				}
				//We process each hop
				String hops = "";
				for (String lineAux : lines) {
					String hop;
					if ((matcherReply = Pattern.compile(patternHop).matcher(lineAux)).find()){
						String hopId = matcherReply.group(0).trim();
						//hop.setId(matcherReply.group(0).trim()); //We add the hop Id
						if ((matcherReply = Pattern.compile(patternIp).matcher(lineAux)).find()){
							hop = "{\"id\":\""+hopId+"\",\"ip\":\""+matcherReply.group(0)+"\" }";
							//hop.setIp(matcherReply.group(0)); //We add the hop Ip
						} else {
							hop = "{\"id\":\""+hopId+"\",\"ip\":\"destination unreachable\" }";
							//hop.setIp("host unreachable"); //We add the hop Ip unreachable
						}
						//traceroute.addHops(hop);
						if(hops.equals("")){
							hops = hop;
						} else {
							hops = hops+","+hop;
						}
					}
				}
				traceroute = "{\"destination\":\""+command[2]+"\",\"ip\":\""+firstLine+"\",\"hops\":["+hops+"]}";
			}
			
			
			JSObject doc = (JSObject) win.getMember("document");
	        JSObject loc = (JSObject) doc.getMember("location");
	        String hostname = (String) loc.getMember("hostname");
	        String port = (String) loc.getMember("port");

	        //Now we upload the traceroute to the server
	        String result = postData(hostname, port, traceroute);
	        
	        //Getting the Traceroute Object. Be careful cause JavaScript has problems with \n character
	        String jsTraceroute = "var traceroute = document.createElement('div');traceroute.innerHTML = \"<h3>Server response</h3> <p>"+result+"</p>\";document.body.appendChild(traceroute);";
	        jsTraceroute = jsTraceroute.replaceAll("\n", "<br/>");
	        win.eval(jsTraceroute);
			
	        
	        //Here we have to add code to allow user to signup!
	        

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public String postData(String hostname, String port, String data) throws Exception{
		
		String result;
		String request = "http://"+hostname+":"+port+"/mercury/api/traceroute/uploadTrace";
		URL url = new URL(request); 
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        
        DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
		wr.write(data.getBytes("UTF-8"));
	    wr.flush ();
	    wr.close ();

	    
		int status = connection.getResponseCode();
		log.info("Status: "+status);
		if ((status==200) || (status==201)){
			log.info("Added traceroute data!");
			result = "Added traceroute data! Thanks for participating.";
		} else {
			log.info("Problems adding entry. Server response status: "+status);
			result = "Problems adding traceroute data. Try again later. Server response status: "+status;
		}

		connection.disconnect();
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	     if (e.getSource() == b) { 
	    	 String destination = t.getText(); //We collect the destination target from the textBox
	    	 String osName = System.getProperty("os.name").toLowerCase();
	    	 if (osName.indexOf("windows") != -1) {
	    		 String[] command = { "tracert", destination }; 
	    		 exec(command);
	    	 } else if (osName.indexOf("mac os x") != -1) {
	    		 String[] command = { "traceroute", destination }; 
	    		 exec(command);
	    	 } else {
	    		 String[] command = { "traceroute", destination }; 
	    		 exec(command);	
	    	 }
	    	 log.info(osName);
	     }
		
	     if (e.getSource() == be) { 
	    	 String destination = t.getText(); //We collect the destination target from the textBox
	    	 String osName = System.getProperty("os.name").toLowerCase();
	    	 if (osName.indexOf("windows") != -1) {
	    		 String[] command = { "tracert", "-d", destination }; //-d to avoid name resolution
	    		 exec(command);
	    	 } else if (osName.indexOf("mac os x") != -1) {
	    		 String[] command = { "traceroute", "-n", destination }; //-n to avoid name resolution
	    		 exec(command);
	    	 } else {
	    		 String[] command = { "traceroute", "-n", destination }; //-n to avoid name resolution
	    		 exec(command);	
	    	 }
	    	 log.info(osName);
	     }
	}
}
