package com.tvestergaard.jrsdoc;

public interface ProgressWriter
{

    void warning(String message);

    void warning(String format, Object... parameters);

    void info(String message);

    void info(String format, Object... parameters);

    void error(String message);

    void error(String format, Object... parameters);
}
