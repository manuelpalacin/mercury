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

package edu.upf.nets.mercury.pojo.maxmind;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A class representing the GeoIP data for a GeoIP database.
 * @author Alex
 *
 */
@Document(collection = "geoipdata")
public class GeoIpData {
	
	private String id;
	private Date timestamp;
	private byte[] data;
	private GeoIpType type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public GeoIpType getType() {
		return type;
	}
	public void setType(GeoIpType type) {
		this.type = type;
	}
	
	
}
