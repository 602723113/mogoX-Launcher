package cc.zoyn.mogox.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * log信息工具类
 */
public class LogUtils {

    private static Log log = null;
    private static LogUtils instance = null;

    public static <T> LogUtils getInstance(Class<T> entityClass) {
        if (instance == null) {
            instance = new LogUtils();
        }
        log = LogFactory.getLog(entityClass);
        return instance;
    }

    public static LogUtils getInstance(String name) {
        if (instance == null) {
            instance = new LogUtils();
        }
        log = LogFactory.getLog(name);
        return instance;
    }

    public static LogUtils getInstance() {
        if (instance == null) {
            instance = new LogUtils();
        }
        log = LogFactory.getLog(LogUtils.class);
        return instance;
    }

    public void info(String msg) {
        log.info(msg);
    }

    public void error(String msg) {
        log.error(msg);
    }

    public void warn(String msg) {
        log.warn(msg);
    }

}
