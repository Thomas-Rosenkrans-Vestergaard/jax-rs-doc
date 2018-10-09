package com.tvestergaard.jrsdoc.ir;

import java.util.List;

public interface IrGenerator
{

    /**
     * Returns an intermediate representation of resources and endpoints from some provided classes.
     *
     * @param classes The classes to generate the intermediate representation of.
     * @return The resources that could be found in the provided classes.
     */
    List<ResourceContext> from(List<Class> classes);
}
