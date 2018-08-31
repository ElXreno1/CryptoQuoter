package com.finplant.cryptoquoter;


public class App {
    public static void main( String[] args )   {

        try
        {
            Configuration.init();
        }
        catch (Exception ex)
        {
            System.out.println( ex.getMessage());

        }
        System.out.println( "Hello World!" );
    }
}
