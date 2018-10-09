package com.tvestergaard.jrsdoc.gathering;

import com.tvestergaard.jrsdoc.DocumentorException;

public class ClassGatheringException extends DocumentorException
{

    public ClassGatheringException(String message)
    {
        super(message);
    }

    public ClassGatheringException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
