/*
 * Project:     MyRobots.com integration for ARISE Human Hamster Wheel 
 * Authors:     Jeffrey Arcand <jeffrey.arcand@ariselab.ca>
 * File:        ca/ariselab/myhhw/Report.java
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

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Report {
	
	/** The period of time to use in the report. */
	private static final int REPORT_PERIOD_MINS = 60;
	
	/** The list of rides done during the period. */
	private List<Ride> rides = new LinkedList<Ride>();
	
	/** The number tesla strikes during the report period. */
	private int teslaStrikes;
	
	/** The number of calories burnt during the report period. */
	private float totalCalories;
	
	/** The usage rate of the HHW during the report period. */
	private float usageRate;
	
	/** Whether or not the HHW is usually open at the report time. */
	private boolean open;
	
	/** The system's uptime in minutes. */
	private int uptime;
	
	/**
	 * The rate of successfull completion of rides during the report period.
	 */
	private float completionRate;
	
	/** Create a new report system. */
	public Report() {
	}
	
	/**
	 * Add the provided ride to the report's data.
	 * @param r The ride to add.
	 */
	public void addRide(Ride r) {
		rides.add(r);
	}
	
	/**
	 * @return The number of rides in the latest data.
	 */
	public int count() {
		return rides.size();
	}
	
	/**
	 * Prune the data to remove rides that aren't included in the report's
	 * reporting period.
	 */
	public void removeOld() {
		Calendar old = Calendar.getInstance();
		old.setTime(new Date());
		old.add(Calendar.MINUTE, -REPORT_PERIOD_MINS);
		long oldTS = old.getTime().getTime();
		
		synchronized (rides) {
			Iterator<Ride> it = rides.iterator();
			while (it.hasNext()) {
				Ride r = it.next();
				long rideTS = r.getStopped().getTime();
				if (rideTS < oldTS) {
					it.remove();
				}
			}
		}
	}
	
	/**
	 * Update the report outputs based on the report's data.
	 */
	public void update() {

		synchronized (rides) {
			teslaStrikes = 0;
			totalCalories = 0;
	
			int totalDurations = 0;
			int completeRides = 0;
	
			Iterator<Ride> it = rides.iterator();
			while (it.hasNext()) {
				Ride r = it.next();
		
				totalDurations += r.getDuration();
				teslaStrikes += r.getTesla() ? 1 : 0;
				totalCalories += r.getCalories();
				completeRides += r.getCalories() == 0 ? 0 : 1;
			}
	
			usageRate = totalDurations / 3600.0f;
			completionRate = completeRides / (count() + 0.0001f);
			open = Utils.getHHWOpen();
			uptime = Utils.getSystemUptime();
		}
	}
	
	/**
	 * @return Generate a header for the text representation of the report.
	 */
	public String getHeaders() {
		return "Rides\tTesla\tCals\tUsage\tCompl\tOpen\tUptime";
	}
	
	/**
	 * @return A string representation of this report outputs.
	 */
	public String toString() {
		synchronized (rides) {
			return count()
			  + "\t" + teslaStrikes
			  + "\t" + Math.round(totalCalories)
			  + "\t" + Math.round((usageRate * 1000))
			  + "\t" + Math.round((completionRate * 1000))
			  + "\t" + open
			  + "\t" + uptime;
		}
	}
	
	/** @return The number of rides in the report period. */
	public int getRides() {
		synchronized (rides) {
			return count();
		}
	}
	
	/** @return The number of tesla strikes in the report period. */
	public int getTeslaStrikes() {
		synchronized (rides) {
			return teslaStrikes;
		}
	}
	
	/** @return The total calories burnt in the report period. */
	public int getTotalCalories() {
		synchronized (rides) {
			return Math.round(totalCalories);
		}
	}
	
	/** @return The usage rate of the HHW during the report period. */
	public float getUsageRate() {
		synchronized (rides) {
			return usageRate;
		}
	}
	
	/**
	 * @return The rate of successfull ride completion during the report
	 *    period.
	 */
	public float getCompletionRate() {
		synchronized (rides) {
			return completionRate;
		}
	}
	
	/** @return Whether or not the HHW is usually open at this time. */
	public boolean getOpen() {
		synchronized (rides) {
			return open;
		}
	}
	
	/** @return The system uptime in minutes. */
	public int getUptime() {
		synchronized (rides) {
			return uptime;
		}
	}
}

