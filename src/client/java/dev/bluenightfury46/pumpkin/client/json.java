package dev.bluenightfury46.pumpkin.client;

import com.google.gson.*;

import java.lang.reflect.Type;

public class json implements JsonSerializer<config>, JsonDeserializer<config>{
    @Override
    public config deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {


        JsonObject obj = jsonElement.getAsJsonObject();
        return new config(obj.get("x").getAsInt(), obj.get("y").getAsInt(), obj.get("txt").getAsString(), obj.get("colour").getAsString());


    }

    @Override
    public JsonElement serialize(config config, Type type, JsonSerializationContext jsonSerializationContext) throws JsonParseException{

        JsonObject object = new JsonObject();


        object.addProperty("x", config.x);
        object.addProperty("y", config.y);
        object.addProperty("txt", config.txt);
        object.addProperty("colour", config.priorColour);

        return object;


    }
}
