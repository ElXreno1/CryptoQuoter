package com.finplant.cryptoquoter;

import com.finplant.cryptoquoter.Model.Configuration.Yamlconfig;
import org.jasypt.util.text.StrongTextEncryptor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class App {

    private static String fileName = "/settings-cryptoquoter.yml";

    public static void main( String[] args )   {

        Yaml yaml = new Yaml();
        Yamlconfig yamlConfig = null;

        String myPassword = "A Strong Password";
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(myPassword);
        String pasEnc = textEncryptor.encrypt("pas");
        String plainText = textEncryptor.decrypt(pasEnc);


        try(InputStream in = App.class.getResourceAsStream(fileName))
        {
             yamlConfig = yaml.loadAs(in, Yamlconfig.class);
        }
        catch (Exception ex)
        {
            System.out.println("Error reading settings file " + fileName);
            System.out.println(ex.getMessage());
            return;

        }

        System.out.println( "Hello World!" );
    }
}
