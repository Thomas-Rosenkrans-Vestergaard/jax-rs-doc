package com.tvestergaard.jrsdoc.ir;

import java.util.ArrayList;
import java.util.List;

public class MutableResourceContext implements ResourceContext
{

    private       String                name;
    private       String                description;
    private       String                path;
    private final List<EndpointContext> endpoints;

    public MutableResourceContext()
    {
        this.endpoints = new ArrayList<>();
    }

    public MutableResourceContext(String name, String description)
    {
        this(name, description, new ArrayList<>());
    }

    public MutableResourceContext(String name, String description, List<EndpointContext> endpoints)
    {
        this.name = name;
        this.description = description;
        this.endpoints = endpoints;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * Adds the provided endpoint to the resource.
     *
     * @param endpoint The endpoint to add to the resource.
     * @return this
     */
    public MutableResourceContext addEndpoint(EndpointContext endpoint)
    {
        this.endpoints.add(endpoint);

        return this;
    }

    /**
     * Returns the name of the resource.
     *
     * @return The name of the resource.
     */
    @Override public String getName()
    {
        return name;
    }

    /**
     * Returns the path to the resource.
     *
     * @return The path to the resource.
     */
    @Override public String getPath()
    {
        return path;
    }

    /**
     * Returns the description of the resource.
     *
     * @return The description of the resource.
     */
    @Override public String getDescription()
    {
        return description;
    }

    /**
     * Returns the endpoints in the resource.
     *
     * @return The endpoints in the resource.
     */
    @Override public List<EndpointContext> getEndpoints()
    {
        return this.endpoints;
    }
}
