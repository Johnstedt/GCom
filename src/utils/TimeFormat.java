package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {
	public static String getTimestamp() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());

	}
}
