package chenjunfu2.decoratedpotearly.client;

import chenjunfu2.decoratedpotearly.client.registry.ClientModParticles;
import net.fabricmc.api.ClientModInitializer;

public class DecoratedPotEarlyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient()
	{
		ClientModParticles.registerParticles();
	}
}