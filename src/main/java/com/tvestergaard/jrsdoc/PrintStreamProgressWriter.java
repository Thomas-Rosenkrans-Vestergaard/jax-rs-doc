package com.tvestergaard.jrsdoc;

import java.io.PrintStream;

public class PrintStreamProgressWriter implements ProgressWriter
{

    private final PrintStream output;

    public PrintStreamProgressWriter(PrintStream output)
    {
        this.output = output;
    }

    @Override public void warning(String message)
    {
        output.print("[WARNING] : ");
        output.print(message);
        output.print('\n');
    }

    @Override public void warning(String format, Object... parameters)
    {
        warning(String.format(format, parameters));
    }

    @Override public void info(String message)
    {
        output.print("[INFO]    : ");
        output.print(message);
        output.print('\n');
    }

    @Override public void info(String format, Object... parameters)
    {
        info(String.format(format, parameters));
    }


    @Override public void error(String message)
    {
        output.print("[ERROR]   : ");
        output.print(message);
        output.print('\n');
    }

    @Override public void error(String format, Object... parameters)
    {
        info(String.format(format, parameters));
    }
}
