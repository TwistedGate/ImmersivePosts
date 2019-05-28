package twistedgate.immersiveposts.utils;

import java.util.Locale;

public class StringUtils{
	/** Converts the first character in a string to it's uppercase version. */
	public static final String upperCaseFirst(String s){
		return s.substring(0, 1).toUpperCase(Locale.ENGLISH)+(s.toLowerCase(Locale.ENGLISH).substring(1));
	}
}
