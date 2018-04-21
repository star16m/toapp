package star16m.utils.toapp.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import star16m.utils.toapp.ToAppConstants;

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

    public static DateTime getDateTime(String orgString) {
        if (isEmpty(orgString)) {
            return null;
        }
        DateTime dateTime = null;
        if (orgString.matches("\\d{6,8}")) {
            String tmpString = replaceGroup(orgString, "(\\d{6,8})");
            if (tmpString.length() == 6) {
                tmpString = "20" + tmpString;
            }
            dateTime = DateTime.parse(tmpString, ToAppConstants.DATE_TIME_FORMATTER);
        } else if (orgString.matches("\\d{4}\\.\\d{1,2}\\.\\d{1,2}")) {
            dateTime = new DateTime(DateTimeFormat.forPattern("yyyy.MM.dd").parseDateTime(replaceGroup(orgString, "(\\d{4}\\.\\d{1,2}\\.\\d{1,2})")));
        } else if (orgString.matches("\\d{2}/\\d{2}")) {
            String tmpString = replaceGroup(orgString, "(\\d{2}/\\d{2})");
            dateTime = new DateTime(DateTimeFormat.forPattern("MM/dd").parseDateTime(tmpString));
        } else if (orgString.matches("\\d{2}\\.\\d{2}")) {
            dateTime = new DateTime(DateTimeFormat.forPattern("MM.dd").parseDateTime(replaceGroup(orgString, "(\\d{2}\\.\\d{2})")));
        } else if (orgString.matches("오늘")) {
            dateTime = new DateTime();
        }
        return dateTime;
    }
}