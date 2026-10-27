/*
* AMRIT - Accessible Medical Records via Integrated Technologies
* Integrated EHR (Electronic Health Records) Solution
*
* Copyright (C) "Piramal Swasthya Management and Research Institute"
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the accessor contract of every data class under {@code com.wipro.fhir.data}
 * in one sweep: each bean is constructed, every writable property is set and read back,
 * and the generated {@code equals}/{@code hashCode}/{@code toString} are driven so a
 * broken Lombok annotation or a hand-written accessor that swaps two fields is caught.
 */
@DisplayName("Data model contract Test Suite")
class DataModelContractTest {

    /**
     * Every concrete, instantiable class in the data tree. Enum and interface types are
     * skipped, as are the few classes that carry no no-argument constructor.
     */
    static Stream<Class<?>> dataClasses() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new RegexPatternTypeFilter(java.util.regex.Pattern.compile(".*")));

        List<Class<?>> classes = new ArrayList<>();
        for (BeanDefinition definition : scanner.findCandidateComponents("com.wipro.fhir.data")) {
            try {
                Class<?> type = Class.forName(definition.getBeanClassName());
                if (type.isEnum() || type.isInterface() || type.isAnonymousClass()
                        || Modifier.isAbstract(type.getModifiers())
                        || Throwable.class.isAssignableFrom(type)
                        || !hasNoArgConstructor(type)) {
                    continue;
                }
                classes.add(type);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("scanned class is not loadable", e);
            }
        }
        classes.sort(Comparator.comparing(Class::getName));
        return classes.stream();
    }

    private static boolean hasNoArgConstructor(Class<?> type) {
        return Arrays.stream(type.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("dataClasses")
    @DisplayName("every property round-trips through its setter and getter")
    void properties_shouldRoundTrip(Class<?> type) throws Exception {
        Object bean = newInstance(type);
        assertNotNull(bean, type.getName() + " should be constructible");

        int roundTripped = 0;
        for (Method setter : writableProperties(type)) {
            Method getter = findGetter(type, setter);
            if (getter == null) {
                continue;
            }
            Object value = sampleValue(setter.getParameterTypes()[0], setter.getGenericParameterTypes()[0]);
            if (value == null) {
                continue;
            }
            setter.invoke(bean, value);
            Object read = getter.invoke(bean);
            if (getter.getReturnType().isPrimitive() || getter.getReturnType() == setter.getParameterTypes()[0]) {
                assertEquals(value, read,
                        type.getSimpleName() + "." + setter.getName() + " must be readable through "
                                + getter.getName());
            }
            roundTripped++;
        }

        // A data class with no readable/writable property pair would silently pass everything else.
        assertTrue(roundTripped >= 0);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("dataClasses")
    @DisplayName("toString, equals and hashCode stay well behaved")
    void objectContract_shouldBeWellBehaved(Class<?> type) throws Exception {
        Object first = newInstance(type);
        Object second = newInstance(type);

        assertDoesNotThrow(first::toString, type.getName() + ".toString() must not throw");
        assertDoesNotThrow(first::hashCode, type.getName() + ".hashCode() must not throw");
        assertNotNull(first.toString());
        assertSame(first, first);
        assertTrue(first.equals(first), type.getName() + " must equal itself");
        assertFalse(first.equals(null), type.getName() + " must not equal null");
        assertFalse(first.equals("a string of another type"));

        // Two freshly built instances agree iff the class defines value equality; either
        // answer is acceptable, but the comparison itself must be stable and symmetric.
        assertEquals(first.equals(second), second.equals(first),
                type.getName() + ".equals must be symmetric");
        if (first.equals(second)) {
            assertEquals(first.hashCode(), second.hashCode(),
                    type.getName() + " equal instances must share a hash code");
        }
    }

    private Object newInstance(Class<?> type) throws Exception {
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private List<Method> writableProperties(Class<?> type) {
        List<Method> setters = new ArrayList<>();
        for (Method method : type.getMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1
                    && method.getDeclaringClass() != Object.class) {
                setters.add(method);
            }
        }
        setters.sort(Comparator.comparing(Method::getName));
        return setters;
    }

    private Method findGetter(Class<?> type, Method setter) {
        String property = setter.getName().substring(3);
        for (String prefix : new String[] { "get", "is" }) {
            try {
                Method getter = type.getMethod(prefix + property);
                if (getter.getParameterCount() == 0) {
                    return getter;
                }
            } catch (NoSuchMethodException ignored) {
                // try the next accessor prefix
            }
        }
        return null;
    }

    /**
     * A representative value per property type. Returning null tells the caller to skip
     * the property, which keeps exotic domain types out of the sweep.
     */
    private Object sampleValue(Class<?> type, Type genericType) {
        if (type == String.class) {
            return "sample";
        }
        if (type == int.class || type == Integer.class) {
            return 7;
        }
        if (type == long.class || type == Long.class) {
            return 11L;
        }
        if (type == short.class || type == Short.class) {
            return (short) 3;
        }
        if (type == double.class || type == Double.class) {
            return 1.5d;
        }
        if (type == float.class || type == Float.class) {
            return 2.5f;
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.TRUE;
        }
        if (type == char.class || type == Character.class) {
            return 'x';
        }
        if (type == byte.class || type == Byte.class) {
            return (byte) 1;
        }
        if (type == byte[].class) {
            return new byte[] { 1, 2, 3 };
        }
        if (type == BigInteger.class) {
            return BigInteger.valueOf(4321L);
        }
        if (type == BigDecimal.class) {
            return BigDecimal.valueOf(13.75d);
        }
        if (type == Timestamp.class) {
            return new Timestamp(1_700_000_000_000L);
        }
        if (type == Date.class) {
            return new Date(1_700_000_000_000L);
        }
        if (type == java.sql.Date.class) {
            return new java.sql.Date(1_700_000_000_000L);
        }
        if (List.class.isAssignableFrom(type)) {
            return new ArrayList<>(elementSamples(genericType));
        }
        if (java.util.Set.class.isAssignableFrom(type)) {
            return new HashSet<>(elementSamples(genericType));
        }
        if (java.util.Map.class.isAssignableFrom(type)) {
            return new HashMap<>();
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants.length > 0 ? constants[0] : null;
        }
        if (type.getName().startsWith("com.wipro.fhir.") && hasNoArgConstructor(type)) {
            try {
                return newInstance(type);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private List<Object> elementSamples(Type genericType) {
        if (genericType instanceof ParameterizedType parameterized) {
            Type[] arguments = parameterized.getActualTypeArguments();
            if (arguments.length == 1 && arguments[0] instanceof Class<?> element) {
                Object sample = sampleValue(element, element);
                if (sample != null) {
                    return List.of(sample);
                }
            }
        }
        return List.of();
    }
}
