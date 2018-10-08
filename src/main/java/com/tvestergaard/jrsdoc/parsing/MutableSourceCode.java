package com.tvestergaard.jrsdoc.parsing;

import java.util.ArrayList;
import java.util.List;

public class MutableSourceCode implements SourceCode
{

    private final List<ResourceContext> resources;

    public MutableSourceCode()
    {
        this(new ArrayList<>());
    }

    public MutableSourceCode(List<ResourceContext> resources)
    {
        this.resources = resources;
    }

    public MutableSourceCode addResource(ResourceContext resource)
    {
        this.resources.add(resource);

        return this;
    }

    /**
     * Returns a complete list of the resources in the source code.
     *
     * @return The complete list of the resources in the source code.
     * @see ResourceContext
     */
    @Override public List<ResourceContext> getResources()
    {
        return resources;
    }
}
