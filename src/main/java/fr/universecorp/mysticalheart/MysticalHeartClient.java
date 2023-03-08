package fr.universecorp.mysticalheart;

import fr.universecorp.mysticalheart.Client.PlayerHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MysticalHeartClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new PlayerHUD());
    }
}
