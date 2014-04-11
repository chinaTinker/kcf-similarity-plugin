package com.kcf.util;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-10
 */
public class PropertyUtil {
    private static final ESLogger logger = ESLoggerFactory.getLogger("PropertyUtil");

    private static Properties prop = new Properties();

    static {
        Environment env = new Environment();
        String configPath = env.configFile().getPath();

        File configfile = new File(configPath + "/kcfSimilarity.conf");
        if(configfile.exists()){
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(configfile);
                prop.load(fin);
            } catch (Exception e) {
                logger.error("load config file failed ", e);
            } finally {
                if(fin != null) try {
                    fin.close();
                } catch (IOException e) {
                    logger.error("close config file inputStream failed", e);
                }
            }
        }
    }

    public static float getFloat(String key, float defaultval) {
        String valStr = prop.getProperty(key);
        if(valStr != null){
            try {
                defaultval = Float.valueOf(valStr);
            }catch (Exception e){
                logger.error("get float value failed, key = {}", e, key);
            }
        }

        return defaultval;
    }
}
