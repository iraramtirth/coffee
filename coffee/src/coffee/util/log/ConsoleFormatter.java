package coffee.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Print a brief summary of the LogRecord in a human readable
 * format.  The summary will typically be 1 or 2 lines.
 *
 * @version %I%, %G%
 * @since 1.4
 */

public class ConsoleFormatter extends Formatter {

    private Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private MessageFormat formatter;

    private String lineSeparator = (String) java.security.AccessController.doPrivileged(
               new sun.security.action.GetPropertyAction("line.separator"));

    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
	StringBuffer sb = new StringBuffer();
	
	/**########### 时间  ##########**/
	// Minimize memory allocations here.
	dat.setTime(record.getMillis());

	StringBuffer text = new StringBuffer();
	if (formatter == null) {
	    formatter = new MessageFormat(format);
	}
	formatter.format(new Object[]{dat}, text, null);
	sb.append(text);
	
	//类名
	sb.append(record.getLoggerName());
	//
	sb.append(lineSeparator);
	String message = formatMessage(record);
	sb.append(record.getLevel().getLocalizedName());
	sb.append(": ");
	sb.append(message);
	sb.append(lineSeparator);
	if (record.getThrown() != null) {
	    try {
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        record.getThrown().printStackTrace(pw);
	        pw.close();
		sb.append(sw.toString());
	    } catch (Exception ex) {
	    }
	}
	return sb.toString();
    }
    
    public static void main(String[] args) {
        try {
        	Class<?> clz = ClassLoader.getSystemClassLoader().loadClass("coffee.common.log.ConsoleFormatter");
			Formatter fmt = (Formatter) clz.newInstance();
			System.out.println(fmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

