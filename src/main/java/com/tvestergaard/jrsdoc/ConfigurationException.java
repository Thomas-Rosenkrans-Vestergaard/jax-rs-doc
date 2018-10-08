package com.tvestergaard.jrsdoc;

public class ConfigurationException extends DocumentorException
{
    public ConfigurationException(String message)
    {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
