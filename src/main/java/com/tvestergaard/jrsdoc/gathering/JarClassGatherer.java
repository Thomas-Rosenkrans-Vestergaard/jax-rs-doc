package com.tvestergaard.jrsdoc.gathering;

import java.io.File;
import java.util.List;

public class JarClassGatherer implements ClassGatherer
{

    private final File jarFile;

    public JarClassGatherer(File jarFile)
    {
        this.jarFile = jarFile;
    }

    /**
     * Gathers classes from some source.
     *
     * @param packagePrefix The package the collected classes must be in.
     * @return The classes found in the source.
     * @throws ClassGatheringException When an exception occurs while gathering classes.
     */
    @Override public List<Class> getResources(String packagePrefix) throws ClassGatheringException
    {
        return null;
    }
}
