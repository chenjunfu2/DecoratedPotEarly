package chenjunfu2.decoratedpotearly.client;

import chenjunfu2.decoratedpotearly.client.registry.DustPlumeParticle;
import net.fabricmc.api.ClientModInitializer;

public class DecoratedPotEarlyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient()
	{
		ParticleFactoryRegistry.getInstance().register(
			YourMod.DUST_PLUME_PARTICLE,          // 你自己的 ParticleType
			DustPlumeParticle.Factory::new        // 直接复用的原版工厂
		);
	
	}
}