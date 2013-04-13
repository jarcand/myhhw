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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Report {
	
	private List<Ride> rides = new LinkedList<Ride>();
	
	public Report() {
	}
	
	public void addRide(Ride r) {
		rides.add(r);
	}
	
	public int count() {
		return rides.size();
	}
	
/*	public void filter() {
		Iterator<Car> carsIterator = cars.iterator();
		while (carsIterator.hasNext()) {
			Car c = carsIterator.next();
			if (c.getCarColor() == Color.BLUE) {
				carsIterator.remove();
			}
		}
	}
*/	
	public String toString() {
		
		int totalDurations = 0;
		int totalTesla = 0;
		float totalCalories = 0;
		int incompleteRides = 0;
		
		Iterator<Ride> it = rides.iterator();
		while (it.hasNext()) {
			Ride r = it.next();
			
			totalDurations += r.getDuration();
			totalTesla += r.getTesla() ? 1 : 0;
			totalCalories += r.getCalories();
			incompleteRides += r.getCalories() == 0 ? 1 : 0;
		}
		
		String report = "Report: N/A\n";
		try {
			report = "Report:\n"
			  + "\tAll Ride Count: " + count() + "\n"
			  + "\tIncomplete Ride Count: " + incompleteRides + " (" + (incompleteRides * 100 / count()) + "%)\n"
			  + "\tTotal Duration: " + (totalDurations / 60) + " mins (" + (totalDurations / 36 / 8) + "%)\n"
			  + "\tTesla Count: " + totalTesla + " (" + (totalTesla * 100 / count()) + "%)\n"
			  + "\tTotal Calories: " + totalCalories + "\n";
		} catch (ArithmeticException e) {
		}
		return report;
	}
}

