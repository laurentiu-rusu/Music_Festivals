package utils;

import java.util.Properties;

public class ApplicationContext {
    private static final Properties PROPERTIES= Config.getProperties();

    public static Properties getPROPERTIES() {
        return PROPERTIES;
    }
}