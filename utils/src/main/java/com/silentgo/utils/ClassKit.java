package com.silentgo.utils;

import com.alibaba.fastjson.JSONException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class ClassKit {

    private static String webRootPath;
    private static String rootClassPath;
    private static List<Class<? extends Annotation>> ignoreAnnoataion;

    static {
        ignoreAnnoataion = new ArrayList<>();
        ignoreAnnoataion.add(Documented.class);
        ignoreAnnoataion.add(Inherited.class);
        ignoreAnnoataion.add(Target.class);
        ignoreAnnoataion.add(Retention.class);
    }

    public static boolean isAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<Class<? extends Annotation>, Set<Class>> searchAnnotation(List<String> pkgs, List<String> jars) {
        List<String> classFileList = findFiles(getRootClassPath(), "*.class");
        classFileList.addAll(findjarFiles(getWebRootPath() + File.separator + "WEB-INF" + File.separator + "lib", jars));
        return putAll(classFileList, pkgs);
    }

    private static Map<Class<? extends Annotation>, Set<Class>> putAll(List<String> classFileList, List<String> pkgs) {
        Map<Class<? extends Annotation>, Set<Class>> classList = new HashMap<>();
        classFileList.forEach(classFile -> {
            if (valiPkg(pkgs, classFile)) {
                Class<?> classInFile = forName(classFile);
                Annotation[] annotations = classInFile.getAnnotations();
                for (Annotation ans :
                        annotations) {
                    if (ignoreAnnoataion.contains(ans.annotationType())) {
                        continue;
                    }
                    putClass(classList, ans.annotationType(), classInFile);
                }
            }
        });
        return classList;
    }


    private static void putClass(Map<Class<? extends Annotation>, Set<Class>> classSetMap, Class<? extends Annotation> clazz, Class putedClass) {
        if (!classSetMap.containsKey(clazz)) {
            classSetMap.put(clazz, new HashSet<>());
        }
        classSetMap.get(clazz).add(putedClass);
    }


    private static String detectWebRootPath() {
        try {
            String path = ClassKit.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWebRootPath() {
        if (webRootPath == null)
            webRootPath = detectWebRootPath();
        return webRootPath;
    }

    private static String getRootClassPath() {
        if (rootClassPath == null) {
            try {
                String path = ClassKit.class.getClassLoader().getResource("").toURI().getPath();
                rootClassPath = new File(path).getAbsolutePath();
            } catch (Exception e) {
                String path = ClassKit.class.getClassLoader().getResource("").getPath();
                rootClassPath = new File(path).getAbsolutePath();
            }
        }
        return rootClassPath;
    }

    /**
     * search
     *
     * @param baseDirName
     * @param targetFileName
     */
    private static List<String> findFiles(String baseDirName, String targetFileName) {

        List<String> classFiles = new ArrayList<>();
        String tempName = null;
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {

        } else {
            String[] filelist = baseDir.list();
            for (int i = 0; i < (filelist != null ? filelist.length : 0); i++) {
                File readfile = new File(baseDirName + File.separator + filelist[i]);
                if (readfile.isDirectory()) {
                    classFiles.addAll(findFiles(baseDirName + File.separator + filelist[i], targetFileName));
                } else {
                    tempName = readfile.getName();
                    if (wildcardMatch(targetFileName, tempName)) {
                        String classname;
                        String tem = readfile.getAbsoluteFile().toString().replaceAll("\\\\", "/");
                        classname = tem.substring(tem.indexOf("/classes") + "/classes".length() + 1,
                                tem.indexOf(".class"));
                        classFiles.add(classname.replaceAll("/", "."));
                    }
                }
            }
        }
        return classFiles;
    }

    private static boolean valiPkg(List<String> pkgs, String classFile) {
        for (String pkg : pkgs) {
            if (classFile.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }

    private static Map<Class<? extends Annotation>, Set<Class>> extraction(List<String> pkgs, List<String> classFileList, Class<? extends Annotation>... clazzes) {
        Map<Class<? extends Annotation>, Set<Class>> classList = new HashMap<>();
        for (Class<? extends Annotation> clazz : clazzes) {
            classList.put(clazz, new HashSet<>());
        }
        classFileList.forEach(classFile -> {
            if (valiPkg(pkgs, classFile)) {
                Class<?> classInFile = forName(classFile);
                for (Class<? extends Annotation> clazz : clazzes) {
                    if (classInFile.isAnnotationPresent(clazz)) {
                        classList.get(clazz).add(classInFile);
                    }
                }
            }
        });

        return classList;
    }


    private static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> findjarFiles(String baseDirName, final List<String> includeJars) {
        List<String> classFiles = new ArrayList<>();

        try {
            File baseDir = new File(baseDirName);
            if (!baseDir.exists() || !baseDir.isDirectory()) {

            } else {
                String[] filelist = baseDir.list((dir, name) -> includeJars.contains(name));
                for (int i = 0; i < (filelist != null ? filelist.length : 0); i++) {
                    JarFile localJarFile = new JarFile(new File(baseDirName + File.separator + filelist[i]));
                    Enumeration<JarEntry> entries = localJarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String entryName = jarEntry.getName();
                        if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                            String className = entryName.replaceAll("/", ".").substring(0, entryName.length() - 6);
                            classFiles.add(className);
                        }
                    }
                    localJarFile.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classFiles;

    }

    private static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                while (strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                strIndex++;
                if (strIndex > strLength) {
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return strIndex == strLength;
    }


    public static Class<?> getGenericClass(Object object, int index) {
        return getGenericClass(object.getClass(), index);
    }

    public static Class<?> getActualType(Type clz) {
        return getActualType(clz, 0);
    }

    public static Class<?> getActualType(Type clz, int index) {
        ParameterizedType type = (ParameterizedType) clz;
        return (Class<?>) type.getActualTypeArguments()[index];
    }

    public static Type[] getGenericClass(Class<?> clz) {
        ParameterizedType type = (ParameterizedType) clz.getGenericInterfaces()[0];
        return type.getActualTypeArguments();
    }

    public static Class<?> getGenericClass(Class<?> clz, int index) {
        return (Class<?>) getGenericClass(clz)[index];
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else {
            throw new JSONException("TODO");
        }
    }

    public static Collection createCollection(Type type) {
        Class<?> rawClass = getRawClass(type);

        Collection list;
        if (rawClass == AbstractCollection.class //
                || rawClass == Collection.class) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(HashSet.class)) {
            list = new HashSet();
        } else if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet();
        } else if (rawClass.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet();
        } else if (rawClass.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(EnumSet.class)) {
            Type itemType;
            if (type instanceof ParameterizedType) {
                itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                itemType = Object.class;
            }
            list = EnumSet.noneOf((Class<Enum>) itemType);
        } else {
            try {
                list = (Collection) rawClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("create instane error, class " + rawClass.getName());
            }
        }
        return list;
    }
}
