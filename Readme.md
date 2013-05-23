Output your Java logs in [Bunyan](https://github.com/trentm/node-bunyan) format using this [log4j](http://logging.apache.org/log4j/1.2/) layout.

## Installation

1. Make sure log4j is already working (JAR on classpath, `src/main/resources/log4j.properties` configured).
2. Add a dependency to your `pom.xml`

	```xml
    <dependencies>
		<dependency>
			<groupId>com.aldaviva.bunyan4log4j</groupId>
			<artifactId>bunyan4log4j</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	</dependency>
	```

3. In your `log4j.properties`, add a new appender (`ConsoleAppender`, `DailyRollingFileAppender`, etc.) and set it to use `BunyanLayout`.

	```properties
	log4j.appender.bunyan=org.apache.log4j.ConsoleAppender
	log4j.appender.bunyan.layout=com.aldaviva.bunyan4log4j.BunyanLayout
	```
	
	Set BunyanLayout [configuration](#configuration).
	
	```properties
	log4j.appender.bunyan.layout.name=myserver
	log4j.appender.bunyan.layout.src=false
	```
	
	Set some loggers to use the appender.

	```properties
	log4j.rootLogger=warn, bunyan
	log4j.logger.mypackage=info, bunyan
	```
	
## Configuration

Set configuration parameters with `log4j.appender.<appender>.layout.<key> = <value>`.

### name
*required string*

The name for this logger, which appears in the bunyan "name" field.

example: `myserver`

### src
*optional boolean, defaults to false*

Log records will contain file name, method name, and line number iff true.

**Warning:** this information takes time to collect, so maybe don't enable it in production deployments.

## Usage

Send logs to log4j normally.
```java
org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MyClass.class);
LOGGER.error("An error occurred.");
```

Or use [SLF4J](http://www.slf4j.org/) for better layer separation:
```java
org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MyClass.class);
LOGGER.error("An error occurred.");
```

Your logs will contain Bunyan JSON.

```json
{"v":0,"time":"2013-05-23T00:11Z","level":50,"name":"myserver","hostname":"Sigyn","pid":3572,"src": {"file":"MyClass.java","line":36,"func":"init"},"msg":"An error occurred."}
```