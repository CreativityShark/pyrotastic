package net.creativityshark.pyrotastic;

import net.creativityshark.pyrotastic.common.blocks.PyrotasticBlocks;
import net.creativityshark.pyrotastic.common.entities.PyrotasticEntities;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PyrotasticMod implements ModInitializer {
	/* looking through my source code, are you?
	well, go on ahead
	but if you're looking through this as reference for your own mod
	good luck, because I don't even know how half of this works
	 */
	public static final String MOD_ID = "pyrotastic";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		PyrotasticBlocks.registerModBlocks();
		PyrotasticEntities.registerModEntities();
	}
}
