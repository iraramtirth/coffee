package coffee.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

/**
 * Format a LogRecord into a standard XML format.
 * <p>
 * The DTD specification is provided as Appendix A to the Java Logging APIs
 * specification.
 * <p>
 * The XMLFormatter can be used with arbitrary character encodings, but it is
 * recommended that it normally be used with UTF-8. The character encoding can
 * be set on the output Handler.
 * 
 * @version %I%, %G%
 * @since 1.4
 */

public class FileFormatter extends java.util.logging.Formatter {

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private Date dat = new Date();

	private String lineSeparator = (String) java.security.AccessController
			.doPrivileged(new sun.security.action.GetPropertyAction(
					"line.separator"));

	/**
	 * Format the given message to XML.
	 * 
	 * @param record
	 *            the log record to be formatted.
	 * @return a formatted log record
	 */
	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();

		/** ########### 时间 ########## **/
		dat.setTime(record.getMillis());

		sb.append(sdf.format(dat));
		sb.append(" ");

		// 类名
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
}
