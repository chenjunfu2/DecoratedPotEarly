package chenjunfu2.decoratedpotearly;

import chenjunfu2.decoratedpotearly.registry.ModBlocks;
import chenjunfu2.decoratedpotearly.registry.ModParticles;
import chenjunfu2.decoratedpotearly.registry.ModSoundEvents;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecoratedPotEarly implements ModInitializer
{
	public static final String MOD_ID = "decoratedpotearly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		ModBlocks.registerBlocks();
		ModParticles.registerParticles();
		ModSoundEvents.registerSounds();
	}
}