package com.tvestergaard.jrsdoc.parsing;

public interface SourceCodeParser
{

    /**
     *
     * @return
     * @throws ParsingException
     */
    SourceCode parse() throws ParsingException;
}
