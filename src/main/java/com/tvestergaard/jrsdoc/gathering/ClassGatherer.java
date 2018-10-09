package com.tvestergaard.jrsdoc.gathering;

import java.util.List;

public interface ClassGatherer
{

    /**
     * Gathers classes from some source.
     *
     * @param packagePrefix The package the collected classes must be in.
     * @return The classes found in the source.
     * @throws ClassGatheringException When an exception occurs while gathering classes.
     */
    List<Class> getResources(String packagePrefix) throws ClassGatheringException;
}
