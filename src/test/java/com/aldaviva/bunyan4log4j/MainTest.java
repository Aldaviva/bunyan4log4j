package com.aldaviva.bunyan4log4j;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hamcrest.CustomMatcher;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Test;

public class MainTest {

	private static final String ISO8601_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}Z";
	private static Logger TEST_LOGGER;
	private static File LOG_FILE = new File("target/test/test.log");

	@Test
	public void doesLogContainCorrectData() throws IOException {
		TEST_LOGGER.info("hello world"); final long logLineNumber = 28; //line number of this line

		final List<String> logLines = getLogFileLines();
		assertEquals("should be exactly one log line", 1, logLines.size());
		final String logLine = logLines.get(0);
		System.out.println(LOG_FILE.getAbsolutePath()+" :\n"+logLine);

		final JSONObject logObject = (JSONObject) JSONValue.parse(logLine);
		assertEquals("version incorrect", 0L, logObject.get("v"));
		assertEquals("level incorrect", 30L, logObject.get("level"));
		assertEquals("name incorrect", "test", logObject.get("name"));
		assertThat("hostname missing", (String) logObject.get("hostname"), isA(String.class));
		assertThat("pid missing", (Long) logObject.get("pid"), isA(Long.class));
		assertEquals("message incorrect", "hello world", logObject.get("msg"));
		assertThat("time format incorrect", (String) logObject.get("time"), new RegexMatcher(ISO8601_PATTERN));
		
		final JSONObject src = (JSONObject) logObject.get("src");
		assertEquals("src file incorrect", "MainTest.java", src.get("file"));
		assertEquals("src line incorrect", logLineNumber, src.get("line"));
		assertEquals("src func incorrect", "doesLogContainCorrectData", src.get("func"));
	}

	@Before
	public void initLog4j() throws IOException {
		LOG_FILE.mkdirs();
		LOG_FILE.delete();
		LOG_FILE.createNewFile();
		LogManager.resetConfiguration();
		PropertyConfigurator.configure("src/test/resources/log4j.properties");
		TEST_LOGGER = Logger.getLogger(MainTest.class);
	}

	private static List<String> getLogFileLines() throws IOException {
		return Files.readAllLines(LOG_FILE.toPath(), StandardCharsets.UTF_8);
	}

	private static final class RegexMatcher extends CustomMatcher<String> {

		private final String pattern;

		public RegexMatcher(final String pattern) {
			super("matches(\"" + pattern.replaceAll("\\\\", "\\\\\\\\") + "\")");
			this.pattern = pattern;
		}

		public boolean matches(final Object item) {
			return (item instanceof String) && ((String) item).matches(pattern);
		}

	}
}
