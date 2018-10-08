package com.tvestergaard.jrsdoc.parsing;

public class MutableParameterContext implements ParameterContext
{

    private String description;

    public MutableParameterContext()
    {
    }

    public MutableParameterContext(String description)
    {
        this.description = description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override public String getDescription()
    {
        return description;
    }
}
