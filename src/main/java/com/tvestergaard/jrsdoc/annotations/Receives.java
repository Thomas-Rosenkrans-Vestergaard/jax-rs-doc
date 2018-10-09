package com.tvestergaard.jrsdoc.annotations;

public @interface Receives
{
    Class val() default Object.class;

    Class key() default Object.class;

    Class list() default Object.class;

    String description() default "";
}
