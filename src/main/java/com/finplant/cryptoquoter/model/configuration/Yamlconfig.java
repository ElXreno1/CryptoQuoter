package com.finplant.cryptoquoter.model.configuration;

import com.finplant.cryptoquoter.service.MainService;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * YamlConfig read config from yaml file, implements SingleTone
 */
public class Yamlconfig {
    private static final String fileName = "settings-cryptoquoter.yml";

    public Map<String,Map<String,String>> environment;
    public Db db;
    public Long flush_period_s;
    public List<Instrument> instruments;

    public static final Yamlconfig INSTANCE = getYamlconfig();

    private static Yamlconfig getYamlconfig() {
        Yaml yaml = new Yaml();
        Yamlconfig yamlConfig = null;

        try
        {
            InputStream file = new FileInputStream(fileName);
            yamlConfig = yaml.loadAs(file,Yamlconfig.class);
        }
        catch (IOException ex)
        {
            MainService.handleError("Error reading settings file " + fileName, ex);
        }
        return yamlConfig;
    }
}
