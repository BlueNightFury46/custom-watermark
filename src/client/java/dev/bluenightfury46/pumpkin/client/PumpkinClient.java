package dev.bluenightfury46.pumpkin.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.datafixer.fix.OptionsKeyLwjgl3Fix;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.lwjgl.glfw.GLFW;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Environment(EnvType.CLIENT)

//This class is called pumpkin, because I originally wanted to make a halloween mod, but changed my mind...

public class PumpkinClient implements ClientModInitializer {


    public static int x = 20;
    public static int y = 20;
  //  public static int width;
 //   public static int height;
    public static Color colour = Color.WHITE;
    public static String txt = "FPS: {fps}";

    public static MinecraftClient client;

    public static HashMap<String, Color> colourMap = new HashMap<>();
    public static HashMap<String, Integer> colourMapIndex = new HashMap<>();
    public static HashMap<Integer, String> invMapIndex = new HashMap<>();
    public static String priorColourString = "literal{white}";


    public static Screen screen = new Menu(Text.of("Title"));
    public static KeyBinding bind;

    public static int count;
    public static  boolean counting = false;
    public static boolean running = true;

    public static boolean devmode = true;

    final int index_max = 9;

    public static final String MOD_ID = "customwatermark";


    @Override
    public void onInitializeClient() {

        //LoadConfig();

        client = MinecraftClient.getInstance();

        ColourMapInit();


        bind = KeyBindingHelper.registerKeyBinding(new KeyBinding("watermark.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "watermark.keys"));

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if(bind.isPressed()){
                minecraftClient.setScreen(screen);
            }
        });



            KeyBinding cycle_colour = KeyBindingHelper.registerKeyBinding(new KeyBinding("watermark.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_SLASH, "watermark.keys"));


            ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {

                if(count > 5 && counting) {
                    running = true;
                    counting = false;

                    count = 0;

                }

              if(running){
                   if (cycle_colour.isPressed()) {

                       int n = colourMapIndex.get(priorColourString);
                       if (n + 1 > index_max) {
                           n = 0;
                       } else {
                           n++;
                       }

                       String newStr = invMapIndex.get(n);



                       priorColourString = newStr;
                       colour = colourMap.get(newStr);

                       running = false;
                       counting = true;


                   //    ApplyChanges();
                       SaveChanges();




                   }
               }
                if(counting){
                    count++;

                }


            });





        HudRenderCallback.EVENT.register((drawContext, counter)->{

            String value = txt;

            try {
                if(txt.contains("{")&&txt.contains("}")) {

                    value = txt.replace("{fps}", (client.getCurrentFps() + ""));

                        if (value.contains("{health}")) {
                            value = value.replace("{health}", ("" + (int)client.player.getHealth()));
                        }
                    if (value.contains("{hp}")) {
                        value = value.replace("{hp}", ("" + (int)client.player.getHealth()));
                    }
                    if(value.contains("{date}") || value.contains("{time}")) {
                        LocalDateTime t = LocalDateTime.now();
                        value = value.replace("{date}", (t.getDayOfMonth() + "/" + t.getMonthValue() + "/" + t.getYear()));
                        int time = t.getMinute(); if(time < 10) {
                            value = value.replace("{time}", (t.getHour() + ":" + "0" + time));
                        } else {
                            value = value.replace("{time}", (t.getHour() + ":" + time));
                        }
                    }
                    if(value.contains("{username}")) {
                        value = value.replace("{username}", ("" + client.player.getName().getString()));
                    }
                    if(value.contains("{player}")) {
                        value = value.replace("{player}", ("" + client.player.getName().getString()));
                    }

                        if (value.contains("{x}")) {
                            value = value.replace("{x}", ("" + (int) client.player.getX()));
                        }
                        if (value.contains("{y}")) {
                            value = value.replace("{y}", ("" + (int) client.player.getY()));
                        }
                        if (value.contains("{z}")) {
                            value = value.replace("{z}", ("" + (int) client.player.getZ()));
                        }


                }
            } catch(NullPointerException e){}


            try {
                drawContext.drawText(client.textRenderer, value, x, y, colour.getRGB(), false);
            } catch(NullPointerException e){

            }


        });


        LoadConfig();


    }

    public void ColourMapInit(){
        PumpkinClient.colourMap.put("literal{white}", Color.WHITE);
        PumpkinClient.colourMap.put("literal{blue}", new Color(40, 47, 238));
        PumpkinClient.colourMap.put("literal{red}", Color.RED);
        PumpkinClient.colourMap.put("literal{green}", new Color(40, 238, 81));
        PumpkinClient.colourMap.put("literal{yellow}", Color.YELLOW);
        PumpkinClient.colourMap.put("literal{orange}", new Color(249, 117, 38));
        PumpkinClient.colourMap.put("literal{pink}", new Color(240, 114, 208));
        PumpkinClient.colourMap.put("literal{pastel blue}", new Color(170,185,253));
        PumpkinClient.colourMap.put("literal{pastel red}", new Color(	249, 187, 187));
        PumpkinClient.colourMap.put("literal{pastel yellow}", new Color(240, 238, 174));
        PumpkinClient.colourMap.put("literal{pastel green}", new Color(187, 249, 198));
        PumpkinClient.colourMap.put("literal{pastel pink}", new Color(255, 193, 248));

        colourMapIndex.put("literal{white}", 0);
        colourMapIndex.put("literal{blue}", 1);
        colourMapIndex.put("literal{red}", 2);
        colourMapIndex.put("literal{green}", 3);
        colourMapIndex.put("literal{yellow}", 4);
        colourMapIndex.put("literal{orange}", 5);
        colourMapIndex.put("literal{pink}", 6);
        colourMapIndex.put("literal{pastel blue}", 7);
        colourMapIndex.put("literal{pastel red}", 8);
        colourMapIndex.put("literal{pastel yellow}", 9);
        colourMapIndex.put("literal{pastel green}", 10);
        colourMapIndex.put("literal{pastel pink}", 11);


        //INVERSE REGISTER
        invMapIndex.put(0, "literal{white}");
        invMapIndex.put(1, "literal{blue}");
        invMapIndex.put(2, "literal{red}");
        invMapIndex.put(3, "literal{green}");
        invMapIndex.put(4, "literal{yellow}");
        invMapIndex.put(5, "literal{orange}");
        invMapIndex.put(6, "literal{pink}");
        invMapIndex.put(7, "literal{pastel blue}");
        invMapIndex.put(8, "literal{pastel red}");
        invMapIndex.put(9, "literal{pastel yellow}");
        invMapIndex.put(10, "literal{pastel green}");
        invMapIndex.put(11, "literal{pastel pink}");
    }

   public static void LoadConfig(){


        try{

            File file = new File("config/custom-watermark.json");

            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(config.class, new json()).create();

            if(file.exists()){

                Scanner scanner = new Scanner(file);

                String fileData = "";

                while(scanner.hasNextLine()){
                    fileData+=scanner.nextLine();
                }

                scanner.close();

                config config_data = gson.fromJson(fileData, config.class);

                x = config_data.x;
                y = config_data.y;
                colour = colourMap.get(config_data.priorColour);
                priorColourString = config_data.priorColour;
                txt = config_data.txt;






            } else {





            }



        } catch(JsonParseException e){
            LoggerFactory.getLogger(MOD_ID).error("Failed to parse JSON data from config file: " + e);
        }  catch (IOException e) {
            LoggerFactory.getLogger(MOD_ID).error("Failed to find JSON file " + e);
        } catch(NullPointerException e){
             colour = Color.WHITE;
        }




   }
   public static void ApplyChanges(){

       try{colour = colourMap.get(Menu.colour.getValue().toString());if(colour==null){colour = Color.WHITE;}}catch(NullPointerException e){}
       try{priorColourString = Menu.colour.getValue().toString();}catch (NullPointerException e){priorColourString = "literal{white}";}

       try{ x = Integer.parseInt(Menu.x_pos.getText()); }catch(NullPointerException e){x = 10;}catch(NumberFormatException e){x = 10;}
       try{ y = Integer.parseInt(Menu.y_pos.getText()); }catch(NullPointerException e){y = 10;}catch(NumberFormatException e){y = 10;}
     //  try{ width = Integer.parseInt(Menu.width.getText()); }catch(NullPointerException e){width = 30;}catch(NumberFormatException e){width = 30;}
     //  try{ height = Integer.parseInt(Menu.height.getText()); }catch(NullPointerException e){height = 10;}catch(NumberFormatException e){height = 10;}

       try{ txt = Menu.text.getText();}catch (NullPointerException e){txt = "FPS: {fps}";}


       client.setScreen(null);

       try {

           config conf = new config(x, y, txt, priorColourString);

           Gson gson = new GsonBuilder().registerTypeAdapter(config.class, new json()).create();

           String json_string = gson.toJson(conf, config.class);

           File file = new File("config/custom-watermark.json");




           if(!file.exists()){
               file.createNewFile();
           }

           FileWriter json_config_file = new FileWriter(file);
           json_config_file.write(json_string);
           json_config_file.close();

       } catch(JsonParseException e){
           LoggerFactory.getLogger(MOD_ID).error("Failed to parse JSON data from config file: " + e);
       } catch(FileNotFoundException e){
           LoggerFactory.getLogger(MOD_ID).error("Failed to find JSON file " + e);
       } catch (IOException e) {
           LoggerFactory.getLogger(MOD_ID).error("Failed to find JSON file " + e);
       }

   }


    public static void SaveChanges(){

        try{if(colour==null){colour = Color.WHITE;}}catch(NullPointerException e){}
        try{if(priorColourString==null){priorColourString = "literal{white}";}}catch (NullPointerException e){priorColourString = "literal{white}";}

        try{int n = x;}catch(NullPointerException e){x = 10;}catch(NumberFormatException e){x = 10;}
        try{int val = y;}catch(NumberFormatException e){y = 10;}
        //  try{ width = Integer.parseInt(Menu.width.getText()); }catch(NullPointerException e){width = 30;}catch(NumberFormatException e){width = 30;}
        //  try{ height = Integer.parseInt(Menu.height.getText()); }catch(NullPointerException e){height = 10;}catch(NumberFormatException e){height = 10;}

        try{if(txt==null){txt = "FPS: {fps}";}}catch (NullPointerException e){txt = "FPS: {fps}";}



        try {

            config conf = new config(x, y, txt, priorColourString);

            Gson gson = new GsonBuilder().registerTypeAdapter(config.class, new json()).create();

            String json_string = gson.toJson(conf, config.class);

            File file = new File("config/custom-watermark.json");




            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter json_config_file = new FileWriter(file);
            json_config_file.write(json_string);
            json_config_file.close();

        } catch(JsonParseException e){
            LoggerFactory.getLogger(MOD_ID).error("Failed to parse JSON data from config file: " + e);
        } catch(FileNotFoundException e){
            LoggerFactory.getLogger(MOD_ID).error("Failed to find JSON file " + e);
        } catch (IOException e) {
            LoggerFactory.getLogger(MOD_ID).error("Failed to find JSON file " + e);
        }

    }



}
