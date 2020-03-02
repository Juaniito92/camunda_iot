package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class Utils {

    private final static String ENV_PROPERTIES_FILE_NAME = "env.properties";

    public static String getProperty(String property) {
        String value = System.getProperty(property);
        if (!StringUtils.isNotEmpty(value)) {
            value = System.getenv(property);
        }
        if (!StringUtils.isNotEmpty(value)) {
            Properties prop = new Properties();
            InputStream input = null;
            try {
                input = Utils.class.getClassLoader().getResourceAsStream(ENV_PROPERTIES_FILE_NAME);
                prop.load(input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            value = prop.getProperty(property);
        }
        return value;
    }

}
