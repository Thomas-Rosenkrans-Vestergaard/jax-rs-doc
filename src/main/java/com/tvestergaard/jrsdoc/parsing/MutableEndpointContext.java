package com.tvestergaard.jrsdoc.parsing;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class MutableEndpointContext implements EndpointContext
{

    private String                 name;
    private String                 description;
    private MethodContext          method;
    private String                 path;
    private String                 produces;
    private String                 consumes;
    private List<ParameterContext> parameters = new ArrayList<>();
    private ReturnsContext         returnValue;

    public MutableEndpointContext()
    {

    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setMethod(MethodContext method)
    {
        this.method = method;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setProduces(String produces)
    {
        this.produces = produces;
    }

    public void setConsumes(String consumes)
    {
        this.consumes = consumes;
    }

    public void setParameters(List<ParameterContext> parameters)
    {
        this.parameters = parameters;
    }

    public void addParameter(ParameterContext parameter)
    {
        this.parameters.add(parameter);
    }

    public void setReturnValue(ReturnsContext returnValue)
    {
        this.returnValue = returnValue;
    }

    /**
     * Returns the name of the endpoint.
     *
     * @return The name of the endpoint.
     */
    @Override public String getName()
    {
        return name;
    }

    /**
     * Returns the description of the endpoint.
     *
     * @return The description of the endpoint.
     */
    @Override public String getDescription()
    {
        return description;
    }

    /**
     * Returns the method of the endpoint.
     *
     * @return The method of the endpoint.
     */
    @Override public MethodContext getMethod()
    {
        return method;
    }

    /**
     * Returns the path to the endpoint.
     *
     * @return The path to the endpoint.
     */
    @Override public String getPath()
    {
        return path;
    }

    /**
     * Returns the MIME type of the body produced by the endpoint.
     *
     * @return the MIME type of the body produced by the endpoint.
     */
    @Override public String getProduces()
    {
        return produces;
    }

    /**
     * Returns the MIME type of the body consumed by the endpoint.
     *
     * @return the MIME type of the body consumed by the endpoint.
     */
    @Override public String getConsumes()
    {
        return consumes;
    }

    /**
     * Returns the documentation of the parameters on the endpoint.
     *
     * @return The documentation of the parameters on the endpoint.
     */
    @Override public List<ParameterContext> getParameters()
    {
        return parameters;
    }

    /**
     * Returns the documentation of the return value on the endpoint.
     *
     * @return the documentation of the return value on the endpoint.
     */
    @Override public ReturnsContext getReturnValue()
    {
        return returnValue;
    }
}
