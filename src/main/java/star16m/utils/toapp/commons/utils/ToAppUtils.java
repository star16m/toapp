package star16m.utils.toapp.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ToAppUtils {
    private ToAppUtils() {
    }
    public static String getString(String string) {
    	return getString(string, "");
    }
    public static String getString(String string, String defaultString) {
    	return isNotEmpty(string) ? string : defaultString;
    }
    public static boolean isNotEmpty(String string) {
    	return StringUtils.isNotEmpty(string);
    }
    public static boolean isEmpty(String string) {
    	return StringUtils.isEmpty(string);
    }
    public static String replaceGroup(String orgString, String patternString) {
        return replaceGroup(orgString, patternString, 1);
    }
    public static String replaceGroup(String orgString, String patternString, int groupId) {
        String replaceString = new String(orgString);
        if (!isEmpty(patternString)) {
            Pattern p = Pattern.compile(patternString);
            Matcher m = p.matcher(replaceString);
            if (m.find() && m.groupCount() > 0) {
                replaceString = m.group(groupId);
            }
        }
        return replaceString;
    }
    public static String replace(String orgString, String searchString, String replaceString) {
    	return StringUtils.replace(orgString, searchString, replaceString);
    }
    
}