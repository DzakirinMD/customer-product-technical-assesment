package net.dzakirin.common.logging;

public class Logger {
    private final org.slf4j.Logger sllogger;

    public Logger(org.slf4j.Logger sllogger) {
        this.sllogger = sllogger;
    }

    public void info(String msg) {
        sllogger.info(msg);
    }

    public void error(String msg) {
        sllogger.error(msg);
    }

    public void error(String msg, Throwable t) {
        sllogger.error(msg, t);
    }


    // Add other logging methods (debug, warn, error) as needed
}
