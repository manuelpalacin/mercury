/* 
 * Copyright (C) 2013 Alex Bikfalvi
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package edu.upf.nets.mercury.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.maxmind.geoip.LookupService;

import edu.upf.nets.mercury.pojo.maxmind.GeoIpData;
import edu.upf.nets.mercury.pojo.maxmind.GeoIpType;
import edu.upf.nets.mercury.task.TaskProcessorImpl;

/**
 * A class representing a specific GeoIP database
 * @author Alex
 *
 */

@Service("geoIpDatabase")
public class GeoIpDatabase{
	
	private static final Logger log = Logger.getLogger(GeoIpDatabase.class.getName());
	
	@Autowired
    ApplicationContext context;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	
	private LookupService service;
	private String urlString;
	
	public LookupService getService() {
		return this.service;
	}
	
	public void execute() {

		
		//LOAD properties
		if(null == urlString){
			try {
				Properties prop = new Properties();
				prop.load(context.getResource(
						"classpath:maxmind/cityIPv4.properties").getInputStream());
				urlString = prop.getProperty("urlCityIpv4Default");
			} catch (IOException e) {
				e.printStackTrace();
				log.info("Properties file failed to load");
			}
		}
		
		// Try load the lookup service from the most recent file (database).
		if (!this.load()) {
			// If the load fails, perform an update and save.
			this.update();
		}
	}
	
	public boolean load() {
		
		//Go to mongo to find the last maxmind file.
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "timestamp"));
		query.addCriteria(Criteria.where("timestamp").gt(cal.getTime()) );
		GeoIpData geoIpData = mongoTemplate.findOne(query, GeoIpData.class);
		
		// If there is no data, return false. 
		if (null == geoIpData) return false;
		
		// Save data to temporary file and load the new service.
		try {
			this.process(geoIpData.getData());
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}
	
	//public void save() {
	//}
	

	
	/**
	 * Updates the current database using data from the specified URL. The download is performed
	 * asynchronously. 
	 */
	public void update() {
		try{
			// Create the URL for this request.
			URL url = new URL(urlString);
			// Create a new connection for the given URL.
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			try{
				// Execute the request and get the input stream.
				InputStream inputStream = connection.getInputStream();
				// Set the response data.				
				byte[] data = IOUtils.toByteArray(inputStream);
				// Try and process the data.
				this.process(data);
				
				// Save the data to Mongo.
				GeoIpData geoIpData = new GeoIpData();
				geoIpData.setData(data);
				geoIpData.setTimestamp(new Date());
				geoIpData.setType(GeoIpType.CityIpv4);
				mongoTemplate.save(geoIpData);
			}
			catch(IOException e) {	
				//Put a log
			}
			finally {
				// Close the connection.
				connection.disconnect();
			}
		}
		catch(IOException e) {
		}
	}

	
	/**
	 * Processes the GZIP data and loads it into the current database.
	 * @param data The GeoIP data.
	 */
	private void process(byte[] data) throws IOException {
		// Create an input stream from the byte array data.
		InputStream inputStream = new ByteArrayInputStream(data);
		
			// Create a GZIP input stream.
			GZIPInputStream zipStream = new GZIPInputStream(inputStream);
			// Create a temporary file for this database.
			File file = File.createTempFile( String.valueOf((new Date()).getTime()) , ".tmp");
			file.deleteOnExit();
			// Copy the data from.
			IOUtils.copy(zipStream, new FileOutputStream(file));
			// Close the file opened by the current service.
			if (null != this.service) {
				this.service.close();
			}
			// Location service.
			this.service = new LookupService(file);

	}
}
