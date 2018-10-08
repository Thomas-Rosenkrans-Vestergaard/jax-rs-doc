package com.tvestergaard.jrsdoc.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
@Repeatable(Params.class)
public @interface Param
{

    /**
     * The description of the parameter.
     */
    String value();
}
