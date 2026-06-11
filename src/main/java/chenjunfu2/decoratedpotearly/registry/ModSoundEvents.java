package chenjunfu2.decoratedpotearly.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents
{
	//只需要这两个，剩下的原版已经有了
	public static final SoundEvent BLOCK_DECORATED_POT_INSERT = register("block.decorated_pot.insert");
	public static final SoundEvent BLOCK_DECORATED_POT_INSERT_FAIL = register("block.decorated_pot.insert_fail");
	
	private static SoundEvent register(String name)
	{
		final Identifier id = new Identifier(name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}
	
	public static void registerSounds() {}
}
