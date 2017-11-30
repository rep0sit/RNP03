package mainClasses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Constants;

final class Helpers {
	private Helpers() {}
	
	
	private static Pattern namePattern = Pattern.compile(Constants.nameRegex);
	
	
	public static boolean validateName(String name) {
		
		Matcher pM = namePattern.matcher(name);
		return name.length() >= Constants.USERNAME_MIN_LEN && 
				name.length() <= Constants.USERNAME_MAX_LEN && 
				pM.matches(); 
	}
	
	

}
