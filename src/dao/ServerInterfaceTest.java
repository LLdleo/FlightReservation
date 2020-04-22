package dao;

//import org.json.JSONException;
//import org.json.JSONObject;
import airplane.Airplane;
import airplane.Airplanes;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class ServerInterfaceTest {
    public static void main(String[] args){
//        JSONObject jsonObject = ServerInterface.INSTANCE.getTimezone("PoLYmer", "6605a2073bfb4fdb9efdf98ab5c29e9a", "42.272099", "-71.812028");
//        System.out.println(jsonObject);
        Airplanes airplanes = ServerInterface.INSTANCE.getAirplanes("PoLYmer");
        for(Airplane air:airplanes){
            System.out.println(air);
        }



    }

}