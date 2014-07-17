package com.aldaviva.bunyan4log4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.json.simple.JSONObject;

public class BunyanLayout extends Layout {

	private static final Integer BUNYAN_FORMAT_VERSION = 0;
	private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
	private static final TimeZone UTC = TimeZone.getTimeZone("utc");

	/**
	 * The name of the logger
	 *
	 * @example myserver
	 */
	private String name;
	private String hostname;
	private final Integer pid;
	private boolean src;

	public BunyanLayout() {
		src = false;
		name = "myserver";
		pid = PidGetter.getPid();

		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			hostname = "localhost";
		}
	}

	@Override
	public String format(final LoggingEvent event) {
		final Map<String, Object> record = new HashMap<String, Object>();

		record.put("name", name);
		record.put("hostname", hostname);
		record.put("pid", pid);
		record.put("v", BUNYAN_FORMAT_VERSION);
		record.put("level", BunyanLevels.getBunyanLevel(event.getLevel()));
		record.put("msg", event.getMessage());
		record.put("time", getDateString());
		if(src){
			final LocationInfo locationInfo = event.getLocationInformation();
			final Map<String, Object> locationInfoJson = new HashMap<String, Object>();
			locationInfoJson.put("file", locationInfo.getFileName());
			locationInfoJson.put("line", Integer.parseInt(locationInfo.getLineNumber()));
			locationInfoJson.put("func", locationInfo.getMethodName());
			record.put("src", locationInfoJson);
		}

		return JSONObject.toJSONString(record) + Layout.LINE_SEP;
	}

	@Override
	public boolean ignoresThrowable() {
		return false;
	}

	public void activateOptions() {
		// do nothing
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isSrc() {
		return src;
	}

	public void setSrc(final boolean src) {
		this.src = src;
	}

	private String getDateString(){
		final DateFormat iso8601 = new SimpleDateFormat(ISO8601_FORMAT);
		iso8601.setTimeZone(UTC);
		return iso8601.format(new Date());
	}
}
