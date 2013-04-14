/*
 * Project:     MyRobots.com integration for ARISE Human Hamster Wheel 
 * Authors:     Jeffrey Arcand <jeffrey.arcand@ariselab.ca>
 * File:        ca/ariselab/myhhw/MyHHW.java
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

import java.io.File;
import java.io.IOException;

public class MyHHW {
	
	private static final int LOAD_DELAY = 2000;
	
	public static void main(String[] args) {
		
		// Verify the right amount of arguments
		if (args.length < 1) {
			System.err.println("Usage:\n"
			  + "\tjava ca.ariselab.myhhw.MyHHW <log filename>");
			System.exit(1);
		}
		
		// Parse the command line
		String filename = args[0];
		
		// Verify that the log file exists
		File f = new File(filename);
		if (!f.exists()) {
			System.err.println("ERROR: The specified file ("
			  + filename + ") does not exist.");
			System.exit(2);
		}
		
		// Create a report object
		final Report report = new Report();
		
		try {
			
			// Start tailing the specified log file and add ride
			// information to the report object
			LogTailer lt = new LogTailer(filename) {
				public void processRide(Ride r) {
					report.addRide(r);
				}
			};
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Wait a bit for the tailer to process the existing part of the
		// log file
		try {
			Thread.sleep(LOAD_DELAY);
		} catch (InterruptedException e) {
		}
		
		// Create a new reporter worker, that will send data to MyRobots
		new Reporter(report);
	}
}

