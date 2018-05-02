package cc.zoyn.mogox.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * log信息工具类
 */
public class LogUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void info(String message) {
        LOGGER.info(message);
    }

}
