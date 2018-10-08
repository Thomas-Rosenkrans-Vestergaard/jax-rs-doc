package com.tvestergaard.jrsdoc.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvestergaard.jrsdoc.annotations.Description;
import com.tvestergaard.jrsdoc.annotations.Name;
import com.tvestergaard.jrsdoc.annotations.Params;
import com.tvestergaard.jrsdoc.annotations.Returns;
import com.tvestergaard.jrsdoc.out.ProgressWriter;

import javax.ws.rs.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DirectorySourceCodeParser implements SourceCodeParser
{

    private final File           directory;
    private final String         packagePrefix;
    private final ProgressWriter writer;

    public DirectorySourceCodeParser(File directory, String packagePrefix, ProgressWriter writer)
    {
        this.directory = directory;
        this.packagePrefix = packagePrefix;
        this.writer = writer;
    }

    @Override public SourceCode parse() throws ParsingException
    {
        try {

            ClassLoader           classLoader = createClassLoader(directory);
            List<Class>           classes     = findClasses(directory, classLoader, packagePrefix);
            List<ResourceContext> resources   = new ArrayList<>();

            for (Class c : classes) {
                if (c.getAnnotation(Path.class) != null) {
                    writer.notice("Found resource %s.", c.getName());
                    resources.add(createResource(c));
                }
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(resources));

        } catch (ClassNotFoundException | IOException e) {
            throw new ParsingException("Could not load target directory", e);
        }

        return null;
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
            writer.warning("No @Description in class %s.", c.getName());
            return null;
        }

        return annotation.value();
    }

    private String getDescription(Class c, Method m)
    {
        Description annotation = (Description) m.getAnnotation(Description.class);
        if (annotation == null) {
            writer.warning("No @Description on method %s in class %s.", m.getName(), c.getName());
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
            writer.error("No MIME types on @Produces on method %s in class %s.", m.getName(), c.getName());
        if (values.length > 1)
            writer.warning("Two or more MIME types on @Produces on method %s in class %s.", m.getName(), c.getName());

        return produces.value()[0];
    }

    private String getConsumes(Class c, Method m)
    {
        Consumes produces = (Consumes) m.getAnnotation(Consumes.class);
        if (produces == null)
            return null;

        String[] values = produces.value();
        if (values.length < 1)
            writer.error("No MIME types on @Consumes on method %s in class %s.", m.getName(), c.getName());
        if (values.length > 1)
            writer.warning("Two or more MIME types on @Consumes on method %s in class %s.", m.getName(), c.getName());

        return produces.value()[0];
    }

    private List<ParameterContext> getParameters(Method method)
    {
        Params annotation = (Params) method.getAnnotation(Params.class);

        if (annotation == null)
            return new ArrayList<>();

        return Arrays.asList(annotation.value())
                     .stream()
                     .map(param -> new MutableParameterContext(param.value()))
                     .collect(Collectors.toList());
    }

    private ReturnsContext getReturnValue(Class c, Method m)
    {
        Returns annotation = (Returns) m.getAnnotation(Returns.class);
        if (annotation == null) {
            writer.warning(
                    "No @Returns on method %s in class %s.",
                    m.getName(),
                    c.getName());
            return null;
        }

        String description = annotation.description();
        if (description.isEmpty())
            writer.warning("Empty description in @Returns on method %s in class %s.", m.getName(), c.getName());

        return new MutableReturnsContext(getType(annotation, c, m), description);
    }

    private Type getType(Returns annotation, Class c, Method m)
    {
        Class key  = annotation.key();
        Class val  = annotation.val();
        Class list = annotation.val();

        // The return type is a list
        if (!list.equals(Object.class)) {

            // Notify user of unused attribute key
            if (!key.equals(Object.class)) {
                writer.warning("Unused attribute 'key' on @Returns on method %s in class %s.", m.getName(), c.getName());
                writer.notice("The @Return value of method %s in class %s was inferred as list.", m.getName(), c.getName());
            }

            // Notify user of unused attribute val
            if (!val.equals(Object.class)) {
                writer.warning("Unused attribute 'val' on @Returns on method %s in class %s.", m.getName(), c.getName());
                writer.notice("The @Return value of method %s in class %s was inferred as list.", m.getName(), c.getName());
            }

            return Type.listOf(list);
        }

        // The returns type is a map
        if (!key.equals(Object.class)) {

            // Notify user of missing attribute val
            if (val.equals(Object.class)) {
                writer.warning("No attribute val on @Returns on method %s in class %s.", m.getName(), c.getName());
            }

            return Type.mapOf(key, val);
        }

        // The return type is a unit
        if (!val.equals(Object.class)) {
            return Type.unitOf(val);
        }

        writer.warning("No attribute val on @Returns on method %s in class %s.", m.getName(), c.getName());
        return Type.unitOf(null);
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

    private static ClassLoader createClassLoader(File directory)
            throws ClassNotFoundException, IOException, ParsingException
    {
        URL   url  = directory.toURI().toURL();
        URL[] urls = new URL[]{url};

        return new URLClassLoader(urls);
    }

    /**
     * Returns all the classes from the .class files in the provided source directory, with the provided package
     * prefix, or any subpackages. The classes are loaded using the provided class loader.
     *
     * @param directory     The directory to search for class files in.
     * @param classLoader   The class loader used to load the found classes.
     * @param packagePrefix The package search criteria. For a class to be returned, they must have this package prefix.
     * @return The found classes.
     * @throws ParsingException When an exception occurs.
     */
    private List<Class> findClasses(File directory, ClassLoader classLoader, String packagePrefix) throws
                                                                                                   ParsingException
    {
        List<Class> result = new ArrayList<>();
        findClasses(directory, classLoader, packagePrefix, result, "");
        return result;
    }

    private void findClasses(File directory,
                             ClassLoader classLoader,
                             String packagePrefix,
                             List<Class> result,
                             String currentPackage)
            throws ParsingException
    {
        if (!packagePrefix.startsWith(currentPackage) && !currentPackage.startsWith(packagePrefix) && !packagePrefix.equals(currentPackage))
            return;

        if (!directory.exists())
            throw new ParsingException("The provided source direction does not exist.");

        try {
            File[] files = directory.listFiles();
            for (File file : files) {

                String fileName = file.getName();

                if (file.isDirectory()) {
                    if (file.getName().contains("."))
                        throw new ParsingException("Directories cannot contain periods.");
                    String nextPackage = (currentPackage.isEmpty() ? "" : currentPackage + ".") + fileName;
                    findClasses(file, classLoader, packagePrefix, result, nextPackage);
                }

                if (file.getName().endsWith(".class")) {
                    Class loaded = classLoader.loadClass(currentPackage + '.' + removeClassSuffix(fileName));
                    if (loaded != null)
                        result.add(loaded);
                }
            }
        } catch (Exception e) {
            throw new ParsingException("Could not locate class.", e);
        }
    }

    @Produces
    private String removeClassSuffix(String fileName)
    {
        return fileName.substring(0, fileName.length() - 6);
    }
}
