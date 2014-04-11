package com.kcf.analysis;


import com.kcf.util.PropertyUtil;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-9
 *
 * key word dictionary
 */
public class Dictionary {
    private static final ESLogger logger = ESLoggerFactory.getLogger("Dictionary");

    /**
     * kcf defined natures and set the specific weight value
     *
     * types:  condition, drug, treatment, hospital
     */
    private enum NATURES{
        CONDITION("kcf_defined_condition"),
        DRUG("kcf_defined_drug"),
        TREATMENT("kcf_defined_treatment"),
        HOSPITAL("kcf_defined_hospital"),
        LAB("kcf_defined_lab"),
        TEST("kcf_defined_test"),
        DEFAULT("kcf_defined");

        private String nature;
        private float weight = 1.0f;

        private NATURES(String nature){
            this.nature = nature;
        }

        public String getNature() {
            return nature;
        }


        public void setWeight(float weight){
            this.weight = weight;
        }

        public float getWeight(){
            return this.weight;
        }

        public static float getWeight(String nature){
            for(NATURES n : NATURES.values()){
                if(n.getNature().equals(nature)) {
                    return n.getWeight();
                }
            }

            return 1.0f;
        }

        public static void init(){
            for(NATURES n : NATURES.values()){
                String key = n.name().toLowerCase();
                float  weight = PropertyUtil.getFloat(key, 10f);

                n.setWeight(weight);

                logger.info("init the nature[{}] weight: {}", key, weight);
            }
        }
    }

    private static boolean isInit = false;

    /**
     * kcf key words container
     */
    private static final Map<String, KeyWord> keyWords = new HashMap<String, KeyWord>();


    /**
     * full fill the keyWords container
     */
    public static void init(Settings settings){
        if(!isInit) {
            Environment env = new Environment(settings);
            String dicPath = env.configFile().getPath() + "/kcf";

            logger.info("kcf dic files dir --> {}", dicPath);

            File file = new File(dicPath);
            if(file.isDirectory()){
                File[] files = file.listFiles(new FilenameFilter(){

                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".dic");
                    }
                });


                if(files != null && files.length > 0){
                    for(File crrFile : files){
                        loadFile(crrFile);
                    }
                }
            }else if(file.isFile()){
                loadFile(file);
            }

            NATURES.init();
            isInit = true;
        }
    }

    /** load key words into memory */
    private static void loadFile(File file){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = reader.readLine()) != null){
                String[] strs = line.split("\t");
                String name = strs[0].trim();

                String nature =  NATURES.DEFAULT.getNature();
                if(strs.length > 1){
                    nature = strs[1].trim();
                }

                int frequency = 1000;
                if(strs.length > 2){
                    frequency = Integer.valueOf(strs[2].trim());
                }

                KeyWord word = new KeyWord(name, nature, frequency);

                keyWords.put(name, word);
            }

            logger.info("read dic file[{}] successfully", file);
        } catch (FileNotFoundException e) {
            logger.error("file[{}] not find", e, file);
        } catch (IOException e) {
            logger.error("read file[{}] failed", e, file);
        } finally {
            if(reader != null) try {
                reader.close();
            } catch (IOException e) {
                logger.error("close file reader failed", e);
            }
        }
    }

    /**
     * get the given word`s weight.
     *
     * if the given word is defined in dictionary,
     * give the specific weight value.
     *
     * @param word
     * @return
     */
    public static float getWeight(String word) {

        float weight = 1.0f;

        KeyWord keyWord = keyWords.get(word);

        if(keyWord != null){
            weight = NATURES.getWeight(keyWord.getNature());
        }

        return  weight;
    }
}


