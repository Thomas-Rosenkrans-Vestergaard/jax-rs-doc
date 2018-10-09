package com.tvestergaard.jrsdoc.ir;

import java.util.List;

public interface EndpointContext
{

    /**
     * Returns the name of the endpoint.
     *
     * @return The name of the endpoint.
     */
    String getName();

    /**
     * Returns the description of the endpoint.
     *
     * @return The description of the endpoint. {@code null} when no description was set on the endpoint.
     */
    String getDescription();

    /**
     * Returns the method of the endpoint.
     *
     * @return The method of the endpoint.
     */
    MethodContext getMethod();

    /**
     * Returns the path to the endpoint.
     *
     * @return The path to the endpoint.
     */
    String getPath();

    /**
     * Returns the MIME type of the body produced by the endpoint.
     *
     * @return the MIME type of the body produced by the endpoint.
     */
    String getProduces();

    /**
     * Returns the MIME type of the body consumed by the endpoint.
     *
     * @return the MIME type of the body consumed by the endpoint.
     */
    String getConsumes();

    /**
     * Returns the documentation of the parameters on the endpoint.
     *
     * @return The documentation of the parameters on the endpoint.
     */
    List<ParameterContext> getParameters();

    /**
     * Returns the documentation of the return value on the endpoint.
     *
     * @return the documentation of the return value on the endpoint.
     */
    ReturnsContext getReturnValue();
}
