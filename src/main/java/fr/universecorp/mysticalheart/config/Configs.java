package fr.universecorp.mysticalheart.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

public class Configs {

    public static Config CONFIG;
    public static int NAME_COLOR;
    public static int ARMOR_COLOR;
    public static int HEALTH_COLOR;
    public static int HUD_DISTANCE;

    public static void registerConfigs() {
        CONFIG = new Config("MysticalHeart.cfg");
        NAME_COLOR = CONFIG.getNameColor();
        ARMOR_COLOR = CONFIG.getArmorColor();
        HEALTH_COLOR = CONFIG.getHealthColor();
        HUD_DISTANCE = CONFIG.getHudDistance();
    }

    public static class Config {

        private Path configPath;
        private File configFile;

        private int nameColor;
        private int armorColor;
        private int healthColor;
        private int hudDistance;

        public Config(String filename) {
            this.configPath = Path.of(FabricLoader.getInstance().getConfigDir() + "\\" + filename);
            this.configFile = this.configPath.toFile();
            this.createConfig();
            this.assignValues();
        }



        public void createConfig() {
            if(!this.configExists()) {
                try {
                    this.configFile.createNewFile();
                } catch (IOException e) { e.printStackTrace(); }
                ConfigProvider.createDefaultFile(ConfigProvider.getDefaultConfig(), this.configFile);
            }

            if(!this.isConfigValid()) {
                this.configFile.delete();
                try {
                    this.configFile.createNewFile();
                } catch (IOException e) { e.printStackTrace(); }
                ConfigProvider.createDefaultFile(ConfigProvider.getDefaultConfig(), this.configFile);
            }
        }

        public boolean configExists() {
            return this.configFile.exists();
        }

        public boolean isConfigValid() {
            boolean distance = false;
            int colors = 0;
            try {
                Scanner scanner = new Scanner(this.configFile);

                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.length() > 0 && line.charAt(0) == '#') { continue; }

                    if(line.contains("name.color") || line.contains("armor.color") || line.contains("health.color")) {
                        String data = line.substring(line.length()-8);
                        if(!data.contains("0x")) {
                            return false;
                        }
                        colors++;
                    }

                    if(line.contains("hud.distance")) {
                        String data = line.substring(line.length()-2);
                        if(!data.matches("[0-9]+")) {
                            return false;
                        }
                        distance = true;
                    }
                }

            } catch (FileNotFoundException exception) {
                return false;
            }

            return colors == 3 && distance;
        }


        public void assignValues() {
            this.nameColor = this.getValue("name.color", 6);
            this.armorColor = this.getValue("armor.color", 6);
            this.healthColor = this.getValue("health.color", 6);
            this.hudDistance = this.getValue("hud.distance", 2);
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

            } catch (FileNotFoundException ignored) { return 0; }

            return length == 2 ? Integer.parseInt(value) : (int) Long.parseLong(value, 16);
        }

        public int getNameColor() { return this.nameColor; }
        public int getArmorColor() { return this.armorColor; }
        public int getHealthColor() { return this.healthColor; }
        public int getHudDistance() { return this.hudDistance; }
    }

}
