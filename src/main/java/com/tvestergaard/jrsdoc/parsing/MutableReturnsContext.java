package com.tvestergaard.jrsdoc.parsing;

public class MutableReturnsContext implements ReturnsContext
{

    private Type   type;
    private String description;

    public MutableReturnsContext()
    {
        this(null, null);
    }

    public MutableReturnsContext(Type type, String description)
    {
        this.type = type;
        this.description = description;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override public Type getType()
    {
        return type;
    }

    @Override public String getDescription()
    {
        return description;
    }
}
