package fr.ig2i.wifind.core;

/**
 * Created by Thomas on 11/01/2016.
 */
public class API {

    public static String Env = "SharedConnection";

    public static String GetBaseURL() {
        switch(Env) {
            case "Home":
                return "http://192.168.1.24";
            case "SharedConnection":
                return "http://192.168.137.1:80";
            case "Internet":
            default:
                return "http://wifind.no-ip.org";
        }
    }

}
