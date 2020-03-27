package star16m.utils.toapp;

import java.time.format.DateTimeFormatter;

public class ToAppConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final String SEARCH_KEYWORD = "[KEYWORD]";

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko):";
    public static final Long TARGET_DATE_RANGE = 60L;
    public static final Long MESSAGE_AVAILABLE_DAY = 3L;

}
