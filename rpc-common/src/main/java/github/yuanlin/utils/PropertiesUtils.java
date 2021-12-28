package github.yuanlin.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * property 工具类
 *
 * @author yuanlin
 * @date 2021/12/28/15:23
 */
@Slf4j
public class PropertiesUtils {

    private static final Map<String, Properties> propertiesMap = new HashMap<>();

    public static Properties readPropertiesFile(String fileName) {
        if (propertiesMap.containsKey(fileName)) {
            return propertiesMap.get(fileName);
        }
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String rpcConfigPath = "";
        if (url != null) {
            rpcConfigPath =url.getPath() + fileName;
            log.debug("read properties-path: {}", rpcConfigPath);
        }
        Properties properties = null;
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(URLDecoder.decode(rpcConfigPath, "UTF-8")), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(reader);
        } catch (Exception e) {
            log.error("caught exception when read properties file [{}]", rpcConfigPath);
        }
        if (properties != null) {
            propertiesMap.put(fileName, properties);
        }
        return properties;
    }
}
