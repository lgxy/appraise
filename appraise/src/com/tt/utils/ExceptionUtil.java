package com.tt.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	public static String getStackTrace(Throwable e) {
		try {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer, true));
			return writer.toString();
		} catch (Throwable e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
	}
}
