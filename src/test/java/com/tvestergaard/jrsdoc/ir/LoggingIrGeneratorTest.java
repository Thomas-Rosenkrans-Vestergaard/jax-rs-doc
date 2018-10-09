package com.tvestergaard.jrsdoc.ir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvestergaard.jrsdoc.PrintStreamProgressWriter;
import com.tvestergaard.jrsdoc.ProgressWriter;
import com.tvestergaard.jrsdoc.testdata.ResourceA;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LoggingIrGeneratorTest
{

    @Test
    public void from()
    {
        ProgressWriter        progressWriter = new PrintStreamProgressWriter(System.out);
        LoggingIrGenerator    irGenerator    = new LoggingIrGenerator(progressWriter);
        List<ResourceContext> resources      = irGenerator.from(Arrays.asList(ResourceA.class));
        Gson                  gson           = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(resources));
    }
}