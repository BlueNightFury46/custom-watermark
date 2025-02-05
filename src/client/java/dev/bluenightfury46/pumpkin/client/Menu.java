package dev.bluenightfury46.pumpkin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.mojang.datafixers.DSL.func;

@Environment(EnvType.CLIENT)

public class Menu extends Screen {
    protected Menu(Text title) {
        super(title);
    }

    public static TextFieldWidget x_pos;
    public static TextFieldWidget y_pos;
    public static TextFieldWidget width;
    public static TextFieldWidget height;
    public static TextFieldWidget text;
    public static ButtonWidget apply;
    public static CyclingButtonWidget colour;

    public static TextWidget x_txt;
    public static TextWidget y_txt;
    public static TextWidget w_txt;
    public static TextWidget h_txt;

    public static TextWidget txt_txt;

    @Override
    protected void init() {

        List<Text> cycle_v = new ArrayList<>();
        cycle_v.add(Text.of("white"));
        cycle_v.add(Text.of("blue"));
        cycle_v.add(Text.of("red"));
        cycle_v.add(Text.of("green"));
        cycle_v.add(Text.of("yellow"));

        Function<Text, Text> func = val -> val;

        colour = new CyclingButtonWidget.Builder<Text>(func).values(cycle_v).initially(cycle_v.get(PumpkinClient.colourMapIndex.get(PumpkinClient.priorColourString))).build(175, 90-25+15, 120, 20, Text.of("colour"));



        apply = ButtonWidget.builder(Text.of("apply changes"), button -> {PumpkinClient.ApplyChanges();}).dimensions(185, 140-10+15, 100, 20).build();

        x_pos = new TextFieldWidget(textRenderer, 130, 110-10+15, 40, 20, Text.of(""));
        x_pos.setText(String.valueOf(PumpkinClient.x));
        x_txt = new TextWidget(x_pos.getX(), x_pos.getY()-15, x_pos.getWidth(), x_pos.getHeight(), Text.of("x value"), textRenderer);


        y_pos = new TextFieldWidget(textRenderer, 175, 110-10+15, 40, 20, Text.of(""));
        y_pos.setText(String.valueOf(PumpkinClient.y));
        y_txt = new TextWidget(y_pos.getX(), y_pos.getY()-15, y_pos.getWidth(), y_pos.getHeight(), Text.of("y value"), textRenderer);


        width = new TextFieldWidget(textRenderer, 10, 100-10+20, 40, 20, Text.of(""));
        width.setText("0");
        w_txt = new TextWidget(width.getWidth(), width.getHeight(), width.getX()-5, width.getY()-15, Text.of("width"), textRenderer);

        height = new TextFieldWidget(textRenderer, 50, 100-10+15, 40, 20, Text.of(""));
        height.setText("0");
        h_txt = new TextWidget(height.getWidth(), height.getHeight(), height.getX()-5, height.getY()-15, Text.of("width"), textRenderer);


        text = new TextFieldWidget(textRenderer, 220, 110-10+15, 120, 20, Text.of(""));
        text.setText(PumpkinClient.txt);
        text.setMaxLength(500);
        txt_txt = new TextWidget(text.getX()-10, text.getY()-15, 100, 20, Text.of("watermark-text"),  textRenderer);









        addDrawableChild(colour);

        addDrawableChild(apply);
        addDrawableChild(text);

        addDrawableChild(x_pos);
        addDrawableChild(y_pos);
     //   addDrawableChild(width);
     //   addDrawableChild(height);


        addDrawableChild(y_txt);
        addDrawableChild(x_txt);
        addDrawableChild(txt_txt);
       // addDrawableChild(w_txt);
     //   addDrawableChild(h_txt);





    }
}
