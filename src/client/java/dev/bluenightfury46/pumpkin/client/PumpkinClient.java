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
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ObjectUtils;
import org.lwjgl.glfw.GLFW;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
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
    public static String priorColourString = "literal{white}";


    public static Screen screen = new Menu(Text.of("Title"));
    public static KeyBinding bind;


    @Override
    public void onInitializeClient() {

        //LoadConfig();

        client = MinecraftClient.getInstance();

        ColourMapInit();


        bind = KeyBindingHelper.registerKeyBinding(new KeyBinding("watermark.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "watermark.keys"));

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if(bind.isPressed()){
                minecraftClient.setScreen(screen);
            }
        });


        HudRenderCallback.EVENT.register((drawContext, counter)->{

            String value = txt;

            try {
                if(txt.contains("{")&&txt.contains("}")) {

                    value = txt.replace("{fps}", (client.getCurrentFps() + ""));
                    if(value.contains("{health}")) {
                        value = value.replace("{health}", ("" + client.player.getHealth()));
                    }
                    if(value.contains("{date}") || value.contains("{time}")) {
                        LocalDateTime t = LocalDateTime.now();
                        value = value.replace("{date}", (t.getDayOfMonth() + "/" + t.getMonthValue() + "/" + t.getYear()));
                        value = value.replace("{time}", (t.getHour() + ":" + t.getMinute()));
                    }
                    if(value.contains("{username}")) {
                        value = value.replace("{username}", ("" + client.player.getName().getString()));
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
        PumpkinClient.colourMap.put("literal{blue}", Color.BLUE);
        PumpkinClient.colourMap.put("literal{red}", Color.RED);
        PumpkinClient.colourMap.put("literal{green}", Color.GREEN);
        PumpkinClient.colourMap.put("literal{yellow}", Color.YELLOW);
        PumpkinClient.colourMap.put("literal{white}", Color.WHITE);

        colourMapIndex.put("literal{white}", 0);
        colourMapIndex.put("literal{blue}", 1);
        colourMapIndex.put("literal{red}", 2);
        colourMapIndex.put("literal{green}", 3);
        colourMapIndex.put("literal{yellow}", 4);
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
            LoggerFactory.getLogger("pumpkin").error("Failed to parse JSON data from config file: " + e);
        }  catch (IOException e) {
            LoggerFactory.getLogger("pumpkin").error("Failed to find JSON file " + e);
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
           LoggerFactory.getLogger("pumpkin").error("Failed to parse JSON data from config file: " + e);
       } catch(FileNotFoundException e){
           LoggerFactory.getLogger("pumpkin").error("Failed to find JSON file " + e);
       } catch (IOException e) {
           LoggerFactory.getLogger("pumpkin").error("Failed to find JSON file " + e);
       }

   }

}
