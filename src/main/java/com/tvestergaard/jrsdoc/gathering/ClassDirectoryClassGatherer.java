package com.tvestergaard.jrsdoc.gathering;

import com.tvestergaard.jrsdoc.ProgressWriter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassDirectoryClassGatherer implements ClassGatherer
{

    /**
     * The directory to return classes from.
     */
    private final File directory;

    /**
     * The progress writer informing the user of the progress of the gathering.
     */
    private final ProgressWriter writer;

    /**
     * Creates a new {@link ClassDirectoryClassGatherer}.
     *
     * @param directory The directory to return classes from.
     * @param writer    The progress writer informing the user of the progress of the gathering.
     */
    public ClassDirectoryClassGatherer(File directory, ProgressWriter writer)
    {
        this.directory = directory;
        this.writer = writer;
    }

    /**
     * Gathers classes from some source.
     *
     * @param packagePrefix The package the collected classes must be in.
     * @return The classes found in the source.
     * @throws ClassGatheringException When an exception occurs while gathering classes.
     */
    @Override public List<Class> getResources(String packagePrefix) throws ClassGatheringException
    {
        ClassLoader classLoader = createClassLoader(directory);
        return findClasses(directory, classLoader, packagePrefix);
    }

    /**
     * Creates a new class loader from the .class files in the provided directory.
     *
     * @param directory The directory of the .class files to add to the class loader.
     * @return The class loader.
     * @throws ClassGatheringException When an exception occurs while creating the class loader.
     */
    private ClassLoader createClassLoader(File directory) throws ClassGatheringException
    {
        try {
            URL   url  = directory.toURI().toURL();
            URL[] urls = new URL[]{url};

            return new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            throw new ClassGatheringException("Provided directory path is malformed.", e);
        }
    }

    /**
     * Returns all the classes from the .class files in the provided source directory, with the provided package
     * prefix, or any subpackages. The classes are loaded using the provided class loader.
     *
     * @param directory     The directory to search for class files in.
     * @param classLoader   The class loader used to load the found classes.
     * @param packagePrefix The package search criteria. For a class to be returned, they must have this package prefix.
     * @return The found classes.
     * @throws ClassGatheringException When an exception occurs.
     */
    private List<Class> findClasses(File directory, ClassLoader classLoader, String packagePrefix) throws ClassGatheringException
    {
        List<Class> result = new ArrayList<>();
        findClasses(directory, classLoader, packagePrefix, result, "");
        return result;
    }

    private void findClasses(File directory,
            ClassLoader classLoader,
            String packagePrefix,
            List<Class> result,
            String currentPackage) throws ClassGatheringException
    {
        if (!packagePrefix.startsWith(currentPackage) && !currentPackage.startsWith(packagePrefix) && !packagePrefix.equals(currentPackage))
            return;

        if (!directory.exists())
            throw new ClassGatheringException("The provided source direction does not exist.");

        try {
            File[] files = directory.listFiles();
            for (File file : files) {

                String fileName = file.getName();

                if (file.isDirectory()) {
                    if (file.getName().contains("."))
                        throw new ClassGatheringException("Directories cannot contain periods.");
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
            throw new ClassGatheringException("Could not locate class.", e);
        }
    }

    /**
     * Takes the name of a .class file, returning the file name without the .class suffix.
     *
     * @param fileName The filename to return without the .class suffix.
     * @return The file name without the .class suffix.
     */
    private String removeClassSuffix(String fileName)
    {
        return fileName.substring(0, fileName.length() - 6);
    }
}
