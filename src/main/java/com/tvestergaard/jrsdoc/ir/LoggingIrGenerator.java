package com.tvestergaard.jrsdoc.ir;

import com.tvestergaard.jrsdoc.ProgressWriter;
import com.tvestergaard.jrsdoc.annotations.*;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoggingIrGenerator implements IrGenerator
{

    private final ProgressWriter progress;

    public LoggingIrGenerator(ProgressWriter progressWriter)
    {
        this.progress = progressWriter;
    }

    @Override public List<ResourceContext> from(List<Class> classes)
    {
        List<ResourceContext> resources = new ArrayList<>();

        for (Class c : classes) {
            if (c.getAnnotation(Path.class) != null) {
                progress.info("Found resource %s.", c.getName());
                resources.add(createResource(c));
            }
        }

        return resources;
    }

    /**
     * Creates a resource context from the provided class.
     *
     * @param c The class from which to create the resource context.
     * @return The resource context created from the provided class.
     */
    private ResourceContext createResource(Class c)
    {
        MutableResourceContext resourceContext = new MutableResourceContext();

        resourceContext.setName(getName(c));
        resourceContext.setDescription(getDescription(c));
        resourceContext.setPath(getPath(c));
        for (EndpointContext endpointContext : getEndpoints(resourceContext, c))
            resourceContext.addEndpoint(endpointContext);

        return resourceContext;
    }

    private String getName(Class c)
    {
        Name annotation = (Name) c.getAnnotation(Name.class);
        if (annotation == null)
            return c.getSimpleName();

        return annotation.value();
    }

    private String getName(Method method)
    {
        Name annotation = (Name) method.getAnnotation(Name.class);
        if (annotation == null)
            return method.getName();

        return annotation.value();
    }

    private String getDescription(Class c)
    {
        Description annotation = (Description) c.getAnnotation(Description.class);
        if (annotation == null) {
            progress.warning("No @Description in class %s.", c.getName());
            return null;
        }

        return annotation.value();
    }

    private String getDescription(Class c, Method m)
    {
        Description annotation = (Description) m.getAnnotation(Description.class);
        if (annotation == null) {
            progress.warning("No @Description on method %s in class %s.", m.getName(), c.getName());
            return null;
        }

        return annotation.value();
    }

    private List<EndpointContext> getEndpoints(ResourceContext resourceContext, Class c)
    {
        List<EndpointContext> results = new ArrayList<>();
        for (Method m : c.getMethods()) {
            MethodContext methodContext = getMethodContext(m);
            if (methodContext != null) {
                EndpointContext endpointContext = getEndpoint(resourceContext, c, m);
                results.add(endpointContext);
            }
        }

        return results;
    }

    private EndpointContext getEndpoint(ResourceContext resourceContext, Class c, Method m)
    {
        MutableEndpointContext endpointContext = new MutableEndpointContext();
        endpointContext.setName(getName(m));
        endpointContext.setDescription(getDescription(c, m));
        endpointContext.setMethod(getMethodContext(m));
        endpointContext.setPath(getPath(resourceContext, m));
        endpointContext.setConsumes(getConsumes(c, m));
        endpointContext.setProduces(getProduces(c, m));
        endpointContext.setParameters(getParameters(m));
        endpointContext.setReturnValue(getReturnValue(c, m));

        return endpointContext;
    }

    private String getProduces(Class c, Method m)
    {
        Produces produces = (Produces) m.getAnnotation(Produces.class);
        if (produces == null)
            return null;

        String[] values = produces.value();
        if (values.length < 1)
            progress.error("No MIME types on @Produces on method %s in class %s.", m.getName(), c.getName());
        if (values.length > 1)
            progress.warning("Two or more MIME types on @Produces on method %s in class %s.", m.getName(), c.getName());

        return produces.value()[0];
    }

    private String getConsumes(Class c, Method m)
    {
        Consumes produces = (Consumes) m.getAnnotation(Consumes.class);
        if (produces == null)
            return null;

        String[] values = produces.value();
        if (values.length < 1)
            progress.error("No MIME types on @Consumes on method %s in class %s.", m.getName(), c.getName());
        if (values.length > 1)
            progress.warning("Two or more MIME types on @Consumes on method %s in class %s.", m.getName(), c.getName());

        return produces.value()[0];
    }

    private List<ParameterContext> getParameters(Method method)
    {
        Param  annotationParam  = (Param) method.getAnnotation(Param.class);
        Params annotationParams = (Params) method.getAnnotation(Params.class);


        if (annotationParam != null) {
            List<ParameterContext> parameters = new ArrayList<>();
            parameters.add(new MutableParameterContext(annotationParam.value()));
            return parameters;
        }

        if (annotationParams != null) {
            return Arrays.asList(annotationParams.value())
                         .stream()
                         .map(param -> new MutableParameterContext(param.value()))
                         .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private ReturnsContext getReturnValue(Class c, Method m)
    {
        Returns annotation = (Returns) m.getAnnotation(Returns.class);
        if (annotation == null) {
            progress.warning(
                    "No @Returns on method %s in class %s.",
                    m.getName(),
                    c.getName());
            return null;
        }

        String description = annotation.description();
        if (description.isEmpty())
            progress.warning("Empty description in @Returns on method %s in class %s.", m.getName(), c.getName());

        return new MutableReturnsContext(getType(annotation, c, m), description);
    }

    private StructureReference getType(Returns annotation, Class c, Method m)
    {
        Class key  = annotation.key();
        Class val  = annotation.value();
        Class list = annotation.list();

        // The return type is a list
        if (!list.equals(Object.class)) {

            // Notify user of unused attribute key
            if (!key.equals(Object.class)) {
                progress.warning("Unused attribute 'key' on @Returns on method %s in class %s.", m.getName(), c.getName());
                progress.info("The @Return value of method %s in class %s was inferred as list.", m.getName(), c.getName());
            }

            // Notify user of unused attribute val
            if (!val.equals(Object.class)) {
                progress.warning("Unused attribute 'val' on @Returns on method %s in class %s.", m.getName(), c.getName());
                progress.info("The @Return value of method %s in class %s was inferred as list.", m.getName(), c.getName());
            }

            return StructureReference.listOf(list);
        }

        // The returns type is a map
        if (!key.equals(Object.class)) {

            // Notify user of missing attribute val
            if (val.equals(Object.class)) {
                progress.warning("No attribute val on @Returns on method %s in class %s.", m.getName(), c.getName());
            }

            return StructureReference.mapOf(key, val);
        }

        // The return type is a unit
        if (!val.equals(Object.class)) {
            return StructureReference.unitOf(val);
        }

        progress.warning("No attribute val on @Returns on method %s in class %s.", m.getName(), c.getName());
        return StructureReference.unitOf(null);
    }


    private MethodContext getMethodContext(Method method)
    {
        if (method.getAnnotation(GET.class) != null)
            return MethodContext.GET;

        if (method.getAnnotation(POST.class) != null)
            return MethodContext.POST;

        if (method.getAnnotation(DELETE.class) != null)
            return MethodContext.DELETE;

        if (method.getAnnotation(PUT.class) != null)
            return MethodContext.OPTIONS;

        if (method.getAnnotation(OPTIONS.class) != null)
            return MethodContext.OPTIONS;

        return null;
    }

    private String getPath(Class c)
    {
        Path annotation = (Path) c.getAnnotation(Path.class);

        return annotation.value();
    }

    private String getPath(ResourceContext resourceContext, Method method)
    {
        Path annotation = (Path) method.getAnnotation(Path.class);
        if (annotation == null)
            return resourceContext.getPath();

        return String.format("%s/%s", resourceContext.getPath(), annotation.value());
    }
}
