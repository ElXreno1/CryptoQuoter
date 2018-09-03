package com.finplant.cryptoquoter;

import com.finplant.cryptoquoter.Model.Configuration.Yamlconfig;
import com.finplant.cryptoquoter.Service.Encoder;
import org.jasypt.util.text.StrongTextEncryptor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.Map;

public class App {

    private static String fileName = "settings-cryptoquoter.yml";


    public static void main( String[] args )   {

        if (args.length == 1) {
            if (args[0].equals("-p"))
                pass();
            else if (args[0].equals("-h"))
                help();
            return;
        }

        Yaml yaml = new Yaml();
        Yamlconfig yamlConfig;

        try
        {
            InputStream file = new FileInputStream(fileName);
            yamlConfig = yaml.loadAs(file,Yamlconfig.class);
        }
        catch (Exception ex)
        {
            System.out.println("Error reading settings file " + fileName);
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            return;
        }

        System.out.println( "Connection string: " + yamlConfig.db );
    }

    private static void pass() {
        System.out.print( "Please, input password to get encoded password for database: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String rawPass = null;
        try
        {
            rawPass = br.readLine();
        }
        catch (IOException ex)
        {
            System.out.println( "Input is incorrect. " + ex.getMessage());
        }

        String encPass = Encoder.INSTANCE.Encode(rawPass);
        System.out.println("Encoded password: " + encPass);
    }

    private  static void help() {
        System.out.println( "Usage of cryptoquoter:\n");
        System.out.println( "\tcryptoquoter.jar\n\t\tgetting quotes for exchanges\n");
        System.out.println( "\tcryptoquoter.jar -p\n\t\tmaking password for config\n");
        System.out.println( "\tcryptoquoter.jar -h\n\t\tshowing that help\n");
    }
}
