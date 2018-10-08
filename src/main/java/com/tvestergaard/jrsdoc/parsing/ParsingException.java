package com.tvestergaard.jrsdoc.parsing;

import com.tvestergaard.jrsdoc.DocumentorException;
import com.tvestergaard.jrsdoc.annotations.Param;

public class ParsingException extends DocumentorException
{

    public ParsingException(String message)
    {
        super(message);
    }

    public ParsingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
