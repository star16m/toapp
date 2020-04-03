package star16m.utils.toapp.commons.utils;

import org.apache.commons.lang3.StringUtils;
import star16m.utils.toapp.ToAppConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
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

    public static LocalDate getDateTime(String orgString) {
        if (isEmpty(orgString)) {
            return null;
        }
        LocalDate today = LocalDate.now();
        LocalDate dateTime = null;
        if (orgString.matches("\\d{6,8}")) {
            String tmpString = replaceGroup(orgString, "(\\d{6,8})");
            if (tmpString.length() == 6) {
                tmpString = "20" + tmpString;
            }
            dateTime = LocalDate.parse(tmpString, ToAppConstants.DATE_TIME_FORMATTER);
        } else if (orgString.matches("\\d{4}\\.\\d{1,2}\\.\\d{1,2}")) {
            dateTime = LocalDate.parse(replaceGroup(orgString, "(\\d{4}\\.\\d{1,2}\\.\\d{1,2})"), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        } else if (orgString.matches("\\d{2}/\\d{2}")) {
            String tmpString = replaceGroup(orgString, "(\\d{2}/\\d{2})");
            dateTime = LocalDate.parse(today.getYear() + "/" + tmpString, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            if (today.isBefore(dateTime)) {
                dateTime = LocalDate.parse(today.minusYears(1).getYear() + "/" + tmpString, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            }
        } else if (orgString.matches("\\d{2}\\.\\d{2}")) {
            dateTime = LocalDate.parse(today.getYear() + "." + replaceGroup(orgString, "(\\d{2}\\.\\d{2})"), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            if (today.isBefore(dateTime)) {
                dateTime = LocalDate.parse(today.minusYears(1).getYear() + "." + replaceGroup(orgString, "(\\d{2}\\.\\d{2})"), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            }
        } else if (orgString.matches("\\d{2}\\-\\d{2}")) {
            dateTime = LocalDate.parse(today.getYear() + "-" + replaceGroup(orgString, "(\\d{2}\\-\\d{2})"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (today.isBefore(dateTime)) {
                dateTime = LocalDate.parse(today.minusYears(1).getYear() + "-" + replaceGroup(orgString, "(\\d{2}\\-\\d{2})"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        } else if (orgString.matches("오늘") || orgString.matches("\\d{1,2}:\\d{1,2}")) {
            dateTime = LocalDate.now();
        } else if (orgString.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[\\+\\-]\\d{2}:\\d{2}")) {
            dateTime = LocalDate.parse(replaceGroup(orgString, "(\\d{4}\\-\\d{1,2}\\-\\d{1,2})"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return dateTime;
    }

    public static boolean isTargetDate(LocalDate date) {
//        return date.isBefore(LocalDate.now().minusDays(ToAppConstants.TARGET_DATE_RANGE));
        return true;
    }
}