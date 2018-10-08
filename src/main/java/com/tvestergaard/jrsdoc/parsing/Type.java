package com.tvestergaard.jrsdoc.parsing;

public class Type
{
    public final Class   v;
    public final boolean isBasic;
    public final boolean isList;
    public final boolean isMap;
    public final Class   k;

    private Type(Class v, boolean isBasic, boolean isList, boolean isMap, Class k)
    {
        this.v = v;
        this.isBasic = isBasic;
        this.isList = isList;
        this.isMap = isMap;
        this.k = k;
    }


    public static Type unitOf(Class v)
    {
        return new Type(v, true, false, false, null);
    }

    public static Type listOf(Class v)
    {
        return new Type(v, false, true, false, null);
    }

    public static Type mapOf(Class k, Class v)
    {
        return new Type(v, false, false, true, k);
    }
}
