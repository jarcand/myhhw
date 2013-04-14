/*
 * Project:     MyRobots.com integration for ARISE Human Hamster Wheel 
 * Authors:     Jeffrey Arcand <jeffrey.arcand@ariselab.ca>
 * File:        ca/ariselab/myhhw/Reporter.java
 * Date:        Sat 2013-04-13
 * Copyright:   Copyright (c) 2013 by Jeffrey Arcand.  All rights reserved.
 * License:     GNU GPL v3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ca.ariselab.myhhw;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporter {
	
	/** The delay to wait between updates. */
	private static final int SLEEP_DELAY = 30000;
	
	private static SimpleDateFormat sdf
	  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Report rep;
	
	/**
	 * Create a new Reporter worker that will periodically sends the
	 * Report's output to MyRobots.
	 * @param rep The Report to use as the source.
	 */
	public Reporter(Report rep) {
		this.rep = rep;
		
		System.out.println("Timestamp\t\t" + rep.getHeaders()
		  + "\tUpdated");
		
		while (true) {
			rep.removeOld();
			rep.update();
			String url = genURL();
			
			System.out.println(sdf.format(new Date()) + "\t"
			  + rep + "\t" + updateMyRobots(url));
			
			try {
				Thread.sleep(SLEEP_DELAY);
			} catch (InterruptedException e) {
			}
		}
	}
	
	/**
	 * Generate the URL to send the data to MyRobots.
	 * @return The URL to use.
	 */
	private String genURL() {
		return String.format(
		  "%s/update?key=%s&field1=%d&field2=%d&field3=%d&"
		  + "field4=%d&field5=%d&field6=%d&field7=%d&field8=%d",
		  Config.SERVER,
		  Config.WRITE_API_KEY,
		  rep.getRides(),
		  rep.getTeslaStrikes(),
		  rep.getTotalCalories(),
		  Math.round(1000 * rep.getUsageRate()),
		  Math.round(1000 * rep.getCompletionRate()),
		  rep.getOpen() ? 1 : 0,
		  rep.getUptime(),
		  0);
	}
	
	/**
	 * Make an HTTP request for the provided URL and get the result.
	 * @param urlStr The URL to request.
	 * @return Whether the update was successfull or not.
	 */
	private static boolean updateMyRobots(String urlStr) {
		
		try {
			// Make the HTTP request
			URL url = new URL(urlStr);
			HttpURLConnection http =
			  (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.connect();
			
			// Get the response code
			int code = http.getResponseCode();
			if (code != 200) {
				return false;
			}
			
			// Get the response message
			InputStream in = http.getInputStream();
			BufferedReader br = new BufferedReader(
			  new InputStreamReader(in, "UTF-8"));
			String response = br.readLine();
			br.close();
			
			// Verify success
			return !"0".equals(response);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

