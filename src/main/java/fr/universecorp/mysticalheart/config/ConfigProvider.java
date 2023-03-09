package fr.universecorp.mysticalheart.config;

import java.io.*;
import java.util.ArrayList;

public class ConfigProvider {

    public static ArrayList<String> getDefaultConfig() {
        ArrayList<String> lst = new ArrayList<String>();

        lst.add("# MysticalHeart config\n");
        lst.add("# This config only accepts HEX format for colors (example : 0xFEFEFE)\n");
        lst.add("# and integers for values (so don't use decimal !)\n");
        lst.add("# DISCLAIMER : DO NOT CHANGE THE KEY NAMES !\n");
        lst.add("\n");
        lst.add("# Color of the entity's name (default : 0x131313)\n");
        lst.add("name.color=0x131313\n");
        lst.add("\n");
        lst.add("# Color of the entity's armor (default : 0x22188D)\n");
        lst.add("armor.color=0x22188D\n");
        lst.add("\n");
        lst.add("# Color of the entity's health (default : 0x3D3D3D)\n");
        lst.add("health.color=0x3D3D3D\n");
        lst.add("\n");
        lst.add("# Max distance to display the hud (default : 50)\n");
        lst.add("hud.distance=50\n");


        return lst;
    }

    public static File createDefaultFile(ArrayList<String> lst, File configFile) {
        ArrayList<String> configs = ConfigProvider.getDefaultConfig();
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            for(String line : configs) {
                fileWriter.write(line);
            }
            fileWriter.close();
        } catch (IOException ignored) { }

        return configFile;
    }
}
