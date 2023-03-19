package fr.universecorp.mysticalheart.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Configs {

    public static Config CONFIG;
    public static int NAME_COLOR;
    public static int ARMOR_COLOR;
    public static int HEALTH_COLOR;
    public static int HUD_DISTANCE;
    public static boolean HUD_DISPLAYED;

    public static void registerConfigs() {
        CONFIG = new Config("MysticalHeart.cfg");
        NAME_COLOR = CONFIG.getNameColor();
        ARMOR_COLOR = CONFIG.getArmorColor();
        HEALTH_COLOR = CONFIG.getHealthColor();
        HUD_DISTANCE = CONFIG.getHudDistance();
        HUD_DISPLAYED = CONFIG.getHudDisplayed();
    }

    public static class Config {

        private Path configPath;
        private File configFile;

        private int nameColor;
        private int armorColor;
        private int healthColor;
        private int hudDistance;
        private boolean hudDisplayed;

        public Config(String filename) {
            this.configPath = Path.of(FabricLoader.getInstance().getConfigDir() + "\\" + filename);
            this.configFile = this.configPath.toFile();
            this.createConfig();
            this.assignValues();
        }

        public void createConfig() {
            if(!this.configFile.exists()) {
                try {
                    this.configFile.createNewFile();
                } catch (IOException e) { e.printStackTrace(); }
                ConfigProvider.createDefaultFile(this.configFile);
            }
        }


        public void assignValues() {
            this.nameColor = this.getValue("name.color", 6);
            this.armorColor = this.getValue("armor.color", 6);
            this.healthColor = this.getValue("health.color", 6);
            this.hudDistance = this.getValue("hud.distance", 2);
            this.hudDisplayed = this.getValue("displayed");
        }

        public void writeValues(boolean displayed) {
            Configs.HUD_DISPLAYED = displayed;
            this.hudDisplayed = displayed;

            try {
                this.configFile.delete();
                this.configFile = this.configPath.toFile();
                this.configFile.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
            ConfigProvider.createNewFile(this.configFile, displayed);

        }


        public boolean getValue(String key) {

            String value = "";

            try {
                Scanner scanner = new Scanner(this.configFile);
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.contains(key) && line.length() >= 14) {

                        value = line.substring(10);
                        value.toLowerCase();
                    }
                }
                scanner.close();
            } catch (FileNotFoundException ignored) { }


            return value.equals("") || value.equals("false") ? false : value.equals("true");
        }

        public int getValue(String key, int length) {
            String value = "";
            try {
                Scanner scanner = new Scanner(this.configFile);
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.contains(key)) {
                        value = line.substring(line.length()-length);
                    }
                }
                scanner.close();

            } catch (FileNotFoundException ignored) { return 0; }

            return length == 2 ? Integer.parseInt(value) : (int) Long.parseLong(value, 16);
        }

        public int getNameColor() { return this.nameColor; }
        public int getArmorColor() { return this.armorColor; }
        public int getHealthColor() { return this.healthColor; }
        public int getHudDistance() { return this.hudDistance; }
        public boolean getHudDisplayed() { return this.hudDisplayed; }
    }

}
