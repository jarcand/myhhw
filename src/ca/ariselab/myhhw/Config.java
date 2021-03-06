/*
 * Project:     MyRobots.com integration for ARISE Human Hamster Wheel 
 * Authors:     Jeffrey Arcand <jeffrey.arcand@ariselab.ca>
 * File:        ca/ariselab/myhhw/Config.java
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

public final class Config {
	
	public static String SERVER = "https://bots.myrobots.com";
	public static String WRITE_API_KEY = "";
	
	/** Cannot instanciate this class. */
	private Config() {
	}
}

