package net.dzakirin.common.logging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoggerFactory {

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(org.slf4j.LoggerFactory.getLogger(clazz));
    }
}
