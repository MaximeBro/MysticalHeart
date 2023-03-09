package fr.universecorp.mysticalheart;

import fr.universecorp.mysticalheart.config.Configs;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysticalHeart implements ModInitializer {

	public static final String MODID = "mysticalheart";

	@Override
	public void onInitialize() {

		Configs.registerConfigs();

	}
}