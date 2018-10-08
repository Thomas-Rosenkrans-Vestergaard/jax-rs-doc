package com.tvestergaard.jrsdoc;

import com.tvestergaard.jrsdoc.out.PrintStreamProgressWriter;
import com.tvestergaard.jrsdoc.out.ProgressWriter;
import com.tvestergaard.jrsdoc.parsing.DirectorySourceCodeParser;
import com.tvestergaard.jrsdoc.parsing.SourceCodeParser;

import java.io.File;

public class Main
{

    public static void main(String[] args) throws DocumentorException
    {
        if (args.length < 1)
            throw new IllegalArgumentException("You must provided the path to the code to document.");

        ProgressWriter writer = new PrintStreamProgressWriter(System.out);

        SourceCodeParser parser = new DirectorySourceCodeParser(
                new File(args[0]),
                args[1],
                writer
        );

        parser.parse();
    }
}
