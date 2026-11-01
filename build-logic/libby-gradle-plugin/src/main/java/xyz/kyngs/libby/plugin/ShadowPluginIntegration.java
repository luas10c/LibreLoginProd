/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package xyz.kyngs.libby.plugin;

import com.github.jengelman.gradle.plugins.shadow.relocation.Relocator;
import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator;
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import org.gradle.api.Project;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ShadowPluginIntegration {
    @SuppressWarnings("unchecked")
    protected static List<LibbyTask.Relocation> extractShadowJarRelocations(Project project) {
        var task = project.getTasks().withType(ShadowJar.class).named("shadowJar").get();

        var relocations = new ArrayList<LibbyTask.Relocation>();

        Iterable<?> relocatorsIterable = null;
        try {
            Method getRelocatorsMethod = task.getClass().getMethod("getRelocators");
            Object relocatorsObj = getRelocatorsMethod.invoke(task);
            if (relocatorsObj instanceof org.gradle.api.provider.Provider<?> provider) {
                relocatorsIterable = (Iterable<?>) provider.get();
            } else if (relocatorsObj instanceof Iterable<?> iter) {
                relocatorsIterable = iter;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get relocators from shadowJar task", e);
        }

        if (relocatorsIterable == null) {
            return relocations;
        }

        for (Object relocator : relocatorsIterable) {
            if (relocator instanceof SimpleRelocator) {
                String pattern = getFieldOrMethod(relocator, "pattern", "getPattern", "getPattern$com_gradleup_shadow_shadow");
                String shadedPattern = getFieldOrMethod(relocator, "shadedPattern", "getShadedPattern", "getShadedPattern$com_gradleup_shadow_shadow");
                relocations.add(new LibbyTask.Relocation(pattern, shadedPattern));
            }
        }

        return relocations;
    }

    private static String getFieldOrMethod(Object target, String fieldName, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                Method method = target.getClass().getMethod(methodName);
                method.setAccessible(true);
                return (String) method.invoke(target);
            } catch (Exception ignored) {
            }
        }
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException("Could not read " + fieldName + " from " + target.getClass().getName(), e);
        }
    }
}
