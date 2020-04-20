package dao;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class ServerInterfaceTest {
    public static void main(String[] args) throws JSONException {
//        JSONObject jsonObject = ServerInterface.INSTANCE.getTimezone("PoLYmer", "6605a2073bfb4fdb9efdf98ab5c29e9a", "42.272099", "-71.812028");
//        System.out.println(jsonObject);
        String airplanes = ServerInterface.INSTANCE.getAirplanes("PoLYmer");
        System.out.println(airplanes);

    }

}