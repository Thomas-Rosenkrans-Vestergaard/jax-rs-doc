package com.tvestergaard.jrsdoc.parsing;

import java.util.List;

public interface SourceCode
{

    /**
     * Returns a complete list of the resources in the source code.
     *
     * @return The complete list of the resources in the source code.
     * @see ResourceContext
     */
    List<ResourceContext> getResources();
}
