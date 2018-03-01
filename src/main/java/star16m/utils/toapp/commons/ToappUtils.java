package star16m.utils.toapp.commons;

import org.thymeleaf.util.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToappUtils {
    private ToappUtils() {
    }

    public static String replaceGroup(String orgString, String patternString) {
        return replaceGroup(orgString, patternString, 1);
    }

    public static String replaceGroup(String orgString, String patternString, int groupId) {
        String replaceString = new String(orgString);
        if (!StringUtils.isEmpty(patternString)) {
            Pattern p = Pattern.compile(patternString);
            Matcher m = p.matcher(replaceString);
            if (m.find()) {
                replaceString = m.group(groupId);
            }
        }
        return replaceString;
    }
}