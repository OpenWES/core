package com.openwes.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.openwes.core.annotation.CurrentClass;
import com.openwes.core.annotation.Implementation;
import com.openwes.core.utils.ClassUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.openwes.core.annotation.Inject;

/**
 *
 * @author xuanloc0511@gmail.com
 */
class IOCRegistry {

    private final Map<String, Class<?>> mapping = new HashMap<>();

    public void register(IOCScanConfig config) throws Exception {
        List<Pattern> includes = new ArrayList<>();
        if (config.getIncludes() != null && !config.getIncludes().isEmpty()) {
            config.getIncludes().forEach(includeStr -> {
                includes.add(Pattern.compile(includeStr));
            });
        }

        List<Pattern> excludes = new ArrayList<>();
        if (config.getExcludes() != null && !config.getExcludes().isEmpty()) {
            config.getExcludes().forEach(excludeStr -> {
                excludes.add(Pattern.compile(excludeStr));
            });
        }
        for (String packageName : config.getPackages()) {
            ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(Thread.currentThread().getContextClassLoader())
                    .getTopLevelClassesRecursive(packageName);
            for (ClassPath.ClassInfo ci : classInfos) {
                String clzzPath = ci.getName();
                if (!includes.isEmpty()) {
                    boolean isMatched = includes.stream()
                            .anyMatch((p) -> {
                                return p.matcher(clzzPath).matches();
                            });
                    if (!isMatched) {
                        continue;
                    }
                }

                if (!excludes.isEmpty()) {
                    boolean isMatched = excludes.stream()
                            .anyMatch((p) -> {
                                return p.matcher(clzzPath).matches();
                            });
                    if (isMatched) {
                        continue;
                    }
                }
                register(ci);
            }
        }
    }

    public void register(ClassPath.ClassInfo ci) throws Exception {
        Class clzz = ClassUtils.load(ci.getName());
        Annotation anno = clzz.getDeclaredAnnotation(Implementation.class);
        if (anno == null || !(anno instanceof Implementation)) {
            return;
        }
        String sourceName = ((Implementation) anno).of().getName();
        if (CurrentClass.class.getName().equals(sourceName)) {
            mapping.put(clzz.getName(), clzz);
        } else {
            mapping.put(sourceName, clzz);
        }
    }

    public <P extends Object, T extends P> T loadClass(Class<P> clzz) throws Exception {
        Class<?> c = mapping.get(clzz.getName());
        if (c == null) {
            c = clzz;
        }
        Constructor<?> contructor = c.getConstructor();
        contructor.setAccessible(true);
        Object obj = contructor.newInstance();

        Class<?> current = c;
        while (current.getSuperclass() != null) {
            Field[] fields = current.getDeclaredFields();
            for (Field field : fields) {
                Inject anno = field.getDeclaredAnnotation(Inject.class);
                if (anno == null) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, loadClass(field.getType()));
            }
            current = current.getSuperclass();
        }
        return (T) obj;
    }
}
