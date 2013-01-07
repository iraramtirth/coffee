package coffee.util.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * java.util.logging.Logger的日志级别：
 * 
 * SEVERE (highest value) > WARNING > INFO > CONFIG > FINE > FINER > FINEST
 * (lowest value)
 * 
 * @author coffee 20122012-12-17下午9:53:39
 */
public class Log {

	/**
	 * 日志是否开启的总开关
	 */
	private static final boolean OPEN = true;

	/**
	 * 该代码是否在web工程中运行
	 */
	private static final boolean IS_WEB = true;

	static LogManager logManager;

	private Log() {
	}

	/**
	 * 初始化Logger相关的配置 主要包括，读取指定指定目录下的logger.properties文件 创建logManager
	 */
	public static void initLogManager() {
		logManager = LogManager.getLogManager();
		// 重新读取配置文件
		// ClassLoaderLogManager logManager = new ClassLoaderLogManager();
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(Log.class.getResource("/").getPath()
					+ "conf/logging.properties");
			logManager.readConfiguration(inStream);

			String logPath = logManager
					.getProperty("java.util.logging.FileHandler.pattern");
			// 如果不是以%开头,则手动创建file文件
			// 否则会报如下：java.io.IOException: Couldn't get lock for
			// c:/xx/java%u.log类似的错误
			if (logPath.startsWith("%") == false) {
				File logFile = new File(logPath);
				// 注意不能指定c:/log.file 即win下四个硬盘的根目录下不能存放
				if (logFile.getParentFile().exists() == false) {
					logFile.getParentFile().mkdirs();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*************************************************************************/

	private static Logger handleLogger(Class<?> clazz) {
		if (logManager == null) {
			initLogManager();
		}
		Logger logger = java.util.logging.Logger.getLogger(clazz.getName());

		if (IS_WEB) {
			while (logger != null) {
				Handler[] handlers = logger.getHandlers();
				if (handlers != null) {
					for (Handler handler : handlers) {
						String className = handler.getClass().getName();
						String formatter = logManager.getProperty(className
								+ ".formatter");
						// 如果指定属性propName的值与formatter不一致，则需要重置(因为tomcat中SystemClassLoader无法加载logger.properties中配置的自定义类)
						if (formatter != null
								&& formatter.equals(handler.getFormatter()
										.getClass().getName()) == false) {
							handler.setFormatter(LogProps.getFormatterProperty(
									className + ".formatter",
									new SimpleFormatter()));
						}
					}
				}
				// 递归遍历父级 *******MARK*******
				logger = logger.getParent();
			}
		}
		// 重置该变量，改变量在MARK处被赋值
		logger = java.util.logging.Logger.getLogger(clazz.getName());
		if (containFileHandler(logger.getHandlers()) == false) {
			logger.addHandler(new coffee.util.log.FileHandler());
		}
		return logger;
	}

	private static boolean containFileHandler(Handler[] handlers) {
		for (Handler handler : handlers) {
			if (handler instanceof coffee.util.log.FileHandler) {
				return true;
			}
		}
		return false;
	}

	public static void info(Object thiz, String msg) {
		if (OPEN) {
			if (thiz instanceof Class) {
				handleLogger((Class<?>) thiz).info(msg);
			} else {
				handleLogger(thiz.getClass()).info(msg);
			}
		}
	}

	public static void fine(Object thiz, String msg) {
		if (OPEN) {
			handleLogger(thiz.getClass()).fine(msg);
		}
	}

	public static void warn(Object thiz, String msg) {
		if (OPEN) {
			if (thiz instanceof Class) {
				handleLogger((Class<?>) thiz).warning(msg);
			} else {
				handleLogger(thiz.getClass()).warning(msg);
			}
		}
	}

	public static void error(Object thiz, String msg) {
		if (OPEN) {
			handleLogger(thiz.getClass()).severe(msg);
		}
	}
	
	public static void error(Object thiz, String msg, Exception e) {
		if (OPEN) {
			handleLogger(thiz.getClass()).severe(msg);
		}
	}

	static class LogProps {

		public static String getProperty(String name) {
			if (logManager == null) {
				return null;
			}
			return logManager.getProperty(name);
		};

		static String getStringProperty(String name, String defaultValue) {
			String val = getProperty(name);
			if (val == null) {
				return defaultValue;
			}
			return val.trim();
		}

		static int getIntProperty(String name, int defaultValue) {
			String val = getProperty(name);
			if (val == null) {
				return defaultValue;
			}
			try {
				return Integer.parseInt(val.trim());
			} catch (Exception ex) {
				return defaultValue;
			}
		}

		static boolean getBooleanProperty(String name, boolean defaultValue) {
			String val = getProperty(name);
			if (val == null) {
				return defaultValue;
			}
			val = val.toLowerCase();
			if (val.equals("true") || val.equals("1")) {
				return true;
			} else if (val.equals("false") || val.equals("0")) {
				return false;
			}
			return defaultValue;
		}

		static Level getLevelProperty(String name, Level defaultValue) {
			String val = getProperty(name);
			if (val == null) {
				return defaultValue;
			}
			try {
				return Level.parse(val.trim());
			} catch (Exception ex) {
				return defaultValue;
			}
		}

		static Filter getFilterProperty(String name, Filter defaultValue) {
			String val = getProperty(name);
			try {
				if (val != null) {
					Class<?> clz = Class.forName(val);
					return (Filter) clz.newInstance();
				}
			} catch (Exception ex) {
			}
			return defaultValue;
		}

		static Formatter getFormatterProperty(String name,
				Formatter defaultValue) {
			String val = getProperty(name);
			try {
				if (val != null) {
					Class<?> clz = Class.forName(val);
					return (Formatter) clz.newInstance();
				}
			} catch (Exception ex) {
			}
			return defaultValue;
		}

	}

}
