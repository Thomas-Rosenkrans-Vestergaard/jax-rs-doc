package com.tvestergaard.jrsdoc;

public class DocumentorException extends Throwable
{

    private final String    message;
    private final Throwable cause;

    public DocumentorException(final String message)
    {
        this(message, null);
    }

    public DocumentorException(final String message, final Throwable cause)
    {
        this.message = message;
        this.cause = cause;
    }

    @Override public String getMessage()
    {
        return this.message;
    }

    @Override public Throwable getCause()
    {
        return this.cause;
    }
}
