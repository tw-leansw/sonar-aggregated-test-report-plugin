package com.thoughtworks.lean.sonar.aggreagtedreport.exception;

/**
 * Created by qmxie on 5/20/16.
 */
public class LeanPluginException extends RuntimeException {

    public LeanPluginException() {
    }

    public LeanPluginException(String message) {
        super(message);
    }

    public LeanPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LeanPluginException(Throwable cause) {
        super(cause);
    }

    public LeanPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
