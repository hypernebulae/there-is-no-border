package hypernebulae.tinb;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "there-is-no-border";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean farlands = true;
	public static String precision = "0.###";

	@Override
	public void onInitialize() {
		LOGGER.info("initializing there is no border - get ready to break the limits!");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
