package dev.bluenightfury46.pumpkin.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.awt.*;

public class config{

    public static int y;
    public static int x;
    public static String txt;
    public static String priorColour;

    public config(int xvalue, int yvalue, String txtvalue, String prior){
        x = xvalue;
        y = yvalue;
        txt = txtvalue;
        priorColour = prior;

    }
}
