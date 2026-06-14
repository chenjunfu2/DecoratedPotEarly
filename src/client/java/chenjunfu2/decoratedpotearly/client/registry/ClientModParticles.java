package chenjunfu2.decoratedpotearly.client.registry;

import chenjunfu2.decoratedpotearly.registry.ModParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ClientModParticles
{
	public static void registerParticles()
	{
		ParticleFactoryRegistry.getInstance().register(ModParticles.DUST_PLUME, ClientModDustPlumeParticle.Factory::new);
	}
}
