package fr.universecorp.mysticalheart.client;

import fr.universecorp.mysticalheart.config.Configs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String CATEGORY = "key.mysticalheart.category";
    public static final String DISPLAY_HUD = "key.mysticalheart.display_hud";

    public static KeyBinding displayKey;


    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(displayKey.wasPressed()) {
                Configs.CONFIG.writeValues(!Configs.HUD_DISPLAYED);
                String display = Configs.HUD_DISPLAYED ? " displayed !" : " no longer displayed.";
                client.player.sendMessage(Text.of("§u§bMysticalHeart §8>> §7hud" + display), true);
            }
        });


    }


    public static void registerKeys() {
        displayKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                DISPLAY_HUD, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, CATEGORY
                ));

        registerKeyInputs();
    }
}
