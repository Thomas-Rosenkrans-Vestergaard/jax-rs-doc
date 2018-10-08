package com.tvestergaard.jrsdoc.parsing;

import java.util.List;

public interface ResourceContext
{

    /**
     * Returns the name of the resource.
     *
     * @return The name of the resource.
     */
    String getName();

    /**
     * Returns the description of the resource.
     *
     * @return The description of the resource.
     */
    String getDescription();

    /**
     * Returns the path to the resource.
     *
     * @return The path to the resource.
     */
    String getPath();

    /**
     * Returns the endpoints in the resource.
     *
     * @return The endpoints in the resource.
     */
    List<EndpointContext> getEndpoints();
}
