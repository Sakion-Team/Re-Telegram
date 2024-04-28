package nep.timeline.re_telegram.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nep.timeline.re_telegram.Utils;

public class ClassUtils {
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static List<Class<?>> getClassesInJar(File file) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();

        URLClassLoader clsloader = new URLClassLoader(new URL[]{ file.toURI().toURL() }, getClassLoader());

        try (JarFile jarFile = new JarFile(file))
        {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                String entName = entry.getName().replace("/", ".");

                if (entName.endsWith(".class")) {
                    Class<?> cls = clsloader.loadClass(entName.substring(0, entName.length() - 6));
                    classes.add(cls);
                }
            }

            clsloader.close();

            return classes;
        }
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class") || file.isDirectory()));

        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (!packageName.isEmpty())
                    className = packageName + "." + className;
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (!packagePath.isEmpty())
                    subPackagePath = packagePath + "/" + subPackagePath;
                String subPackageName = fileName;
                if (!packageName.isEmpty())
                    subPackageName = packageName + "." + subPackageName;
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            Utils.log(e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }
}
