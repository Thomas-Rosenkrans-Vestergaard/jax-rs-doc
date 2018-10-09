package com.tvestergaard.jrsdoc.ir;

public class StructureReference
{
    public final String  v;
    public final boolean isBasic;
    public final boolean isList;
    public final boolean isMap;
    public final String  k;

    private StructureReference(Class v, boolean isBasic, boolean isList, boolean isMap, Class k)
    {
        this.v = v == null ? null : v.getName();
        this.isBasic = isBasic;
        this.isList = isList;
        this.isMap = isMap;
        this.k = k == null ? null : k.getName();
    }


    public static StructureReference unitOf(Class v)
    {
        return new StructureReference(v, true, false, false, null);
    }

    public static StructureReference listOf(Class v)
    {
        return new StructureReference(v, false, true, false, null);
    }

    public static StructureReference mapOf(Class k, Class v)
    {
        return new StructureReference(v, false, false, true, k);
    }
}
