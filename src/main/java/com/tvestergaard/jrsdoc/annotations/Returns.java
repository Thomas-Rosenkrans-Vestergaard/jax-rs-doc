package com.tvestergaard.jrsdoc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface Returns
{

    Class value() default Object.class;

    Class key() default Object.class;

    Class list() default Object.class;

    String description() default "";
}
