package org.hyunggi.mygardenbe.docs.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.restdocs.snippet.Attributes.Attribute;

public abstract class RestDocsUtil {
    private RestDocsUtil() {
        // do nothing, Utility class
    }

    public static Attribute field(
            final String key,
            final String value) {
        return new Attribute(key, value);
    }

    public static String allEnumString(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

}
