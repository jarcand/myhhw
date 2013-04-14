/*
 * Project:     MyRobots.com integration for ARISE Human Hamster Wheel 
 * Authors:     Jeffrey Arcand <jeffrey.arcand@ariselab.ca>
 * File:        ca/ariselab/myhhw/Ride.java
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ride {
	
	/** The overhead time needed to start and stop a ride. */
	private static final int RIDE_INIT_SHUTDOWN_TIME = 10;
	
	/** The calorie calculation in the log file is incorrect. */
	private static final int CALORIE_ADJUST = 3;
	
	private static final SimpleDateFormat sdf
	  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Date rideStarted;
	private Date rideStopped;
	private int duration;
	private boolean tesla = false;
	private float calories = 0;
		
	/**
	 * Create a new Ride data structure from the provided lines.
	 * @param startedLine The line describing when the ride started.
	 * @param stoppedLine The line describing when the ride ended.
	 */
	public Ride(String startedLine, String stoppedLine)
	  throws IllegalArgumentException {
		
		// Verify the arguments are not null
		if (startedLine == null) {
			throw new IllegalArgumentException(
			  "The stopped line cannot be null.");
		} else if (stoppedLine == null) {
			throw new IllegalArgumentException(
			  "The stopped line cannot be null.");
		}
		
		// Parse the started timestamp
		try {
			rideStarted = sdf.parse(startedLine.substring(0, 19));
		} catch (ParseException e) {
			throw new IllegalArgumentException(
			  "Could not parse timestamp in started line: "
			   + startedLine.substring(0, 19));
		}
		
		// Parse the stopped timestamp
		try {
			rideStopped = sdf.parse(stoppedLine.substring(0, 19));
		} catch (ParseException e) {
			throw new IllegalArgumentException(
			  "Could not parse timestamp in stopped line: "
			   + stoppedLine.substring(0, 19));
		}
		
		// Calculate the duration
		duration = RIDE_INIT_SHUTDOWN_TIME + (int)
		  ((rideStopped.getTime() - rideStarted.getTime()) / 1000);
		
		// If the ride was completed properly, get additional details
		if (stoppedLine.indexOf("M_RIDE - Ride ended:") > 0) {
			
			// Break the line into parts
			String[] parts = stoppedLine.split(" ");
			if (parts.length == 20) {
			
				// Check if there was a tesla coil strike
				tesla = "true".equals(parts[18]);
		
				// Get the number of calories burnt
				calories = CALORIE_ADJUST
				  * Float.parseFloat(parts[14]);
			}
		}
	}
	
	/**
	 * @return A string representation of this Ride.
	 */
	public String toString() {
		return "Ride (started: " + sdf.format(rideStarted)
		  + ", stopped: " + sdf.format(rideStopped) + ", duration: "
		  + duration + ", tesla: " + tesla + ", calories: " + calories
		  + ")";
	}
	
	/** @return The ride's started date/time. */
	public Date getStarted() { return rideStarted; }
	
	/** @return The ride's stopped date/time. */
	public Date getStopped() { return rideStopped; }
	
	/** @return The ride's duration. */
	public int getDuration() { return duration; }
	
	/** @return The ride's tesla condition. */
	public boolean getTesla() { return tesla; }
	
	/** @return The ride's calories burnt. */
	public float getCalories() { return calories; }
}

