package com.aldaviva.bunyan4log4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;

public final class BunyanLevels {

	private static Map<Integer, Integer> levelMap = new HashMap<Integer, Integer>();
	static {
		levelMap.put(Level.FATAL_INT, 60);
		levelMap.put(Level.ERROR_INT, 50);
		levelMap.put(Level.WARN_INT, 40);
		levelMap.put(Level.INFO_INT, 30);
		levelMap.put(Level.DEBUG_INT, 20);
		levelMap.put(Level.TRACE_INT, 10);
		levelMap.put(Level.ALL_INT, 10);
	}

	private BunyanLevels(){}

	public static Integer getBunyanLevel(final Level level){
		Integer bunyanLevel = levelMap.get(level.toInt());
		if(bunyanLevel == null){
			bunyanLevel = levelMap.get(Level.ALL_INT);
		}

		return bunyanLevel;
	}

}
