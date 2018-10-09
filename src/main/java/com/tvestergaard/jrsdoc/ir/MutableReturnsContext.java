package com.tvestergaard.jrsdoc.ir;

public class MutableReturnsContext implements ReturnsContext
{

    private StructureReference structureReference;
    private String             description;

    public MutableReturnsContext()
    {
        this(null, null);
    }

    public MutableReturnsContext(StructureReference structureReference, String description)
    {
        this.structureReference = structureReference;
        this.description = description;
    }

    public void setStructureReference(StructureReference structureReference)
    {
        this.structureReference = structureReference;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override public StructureReference getType()
    {
        return structureReference;
    }

    @Override public String getDescription()
    {
        return description;
    }
}
