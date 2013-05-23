package com.aldaviva.bunyan4log4j;

import java.lang.reflect.InvocationTargetException;




public class PidGetter {

	/*
	 * http://stackoverflow.com/a/12066696/979493
	 */
	public static Integer getPid() {
		Integer pid = -1;
		try {
			final java.lang.management.RuntimeMXBean runtime = java.lang.management.ManagementFactory.getRuntimeMXBean();
			final java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
			jvm.setAccessible(true);
			@SuppressWarnings("restriction")
			final sun.management.VMManagement mgmt = (sun.management.VMManagement) jvm.get(runtime);
			final java.lang.reflect.Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId", new Class[]{});
			pid_method.setAccessible(true);
			pid = (Integer) pid_method.invoke(mgmt, new Object[]{});
		} catch (final SecurityException e) {
		} catch (final NoSuchFieldException e) {
		} catch (final NoSuchMethodException e) {
		} catch (final IllegalArgumentException e) {
		} catch (final IllegalAccessException e) {
		} catch (final InvocationTargetException e) {
		}

		return pid;
	}

}
