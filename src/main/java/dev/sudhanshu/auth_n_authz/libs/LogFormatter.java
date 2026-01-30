package dev.sudhanshu.auth_n_authz.libs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        String dateTime = sdf.format(new Date(record.getMillis()));
        String level = record.getLevel().getName();
        String message = formatMessage(record);
        String threadName = Thread.currentThread().getName();
        String loggerName = record.getLoggerName();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s [%s] [Thread:%s] [Logger:%s]%n",
                level, message, dateTime, threadName, loggerName));

        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            sb.append(sw.toString());
        }

        return sb.toString();
    }
}
