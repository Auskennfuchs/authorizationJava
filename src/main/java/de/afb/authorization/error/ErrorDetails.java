package de.afb.authorization.error;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
    private StackTraceElement[] trace;

    public ErrorDetails(Date timestamp, String message, String details, StackTraceElement[] trace) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.trace = trace;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public StackTraceElement[] getTrace() {
        return trace;
    }
}
