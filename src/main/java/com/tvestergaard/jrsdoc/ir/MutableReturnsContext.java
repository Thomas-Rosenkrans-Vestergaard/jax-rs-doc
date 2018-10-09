package com.tvestergaard.jrsdoc.ir;

public class MutableReturnsContext implements ReturnsContext
{

    private StructureReference type;
    private String             description;

    public MutableReturnsContext()
    {
        this(null, null);
    }

    public MutableReturnsContext(StructureReference type, String description)
    {
        this.type = type;
        this.description = description;
    }

    public void setType(StructureReference type)
    {
        this.type = type;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override public StructureReference getType()
    {
        return type;
    }

    @Override public String getDescription()
    {
        return description;
    }
}
