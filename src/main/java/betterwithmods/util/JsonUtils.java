package betterwithmods.util;

import betterwithmods.BWMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.JsonContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final JsonContext BWM_CONTEXT = new JsonContext(BWMod.MODID);

    public static JsonObject[] readerFile(File file) {
        Path path = file.toPath();
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
            return net.minecraft.util.JsonUtils.fromJson(GSON, reader, JsonObject[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(File file, JsonArray array) {
        Writer writer;
        try {
            writer = new FileWriter(file);
            GSON.toJson(array, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonObject fromStack(ItemStack stack) {
        JsonObject object = new JsonObject();

        object.addProperty("item", stack.getItem().getRegistryName().toString());
        object.addProperty("data", stack.getMetadata());
        return object;
    }

    public static JsonObject fromOre(String ore) {
        JsonObject object = new JsonObject();
        object.addProperty("type", "forge:ore_dict");
        object.addProperty("ore", ore);
        return object;
    }
}
