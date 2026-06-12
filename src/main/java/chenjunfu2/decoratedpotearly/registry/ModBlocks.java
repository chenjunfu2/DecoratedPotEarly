package chenjunfu2.decoratedpotearly.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;

public class ModBlocks
{
	public static void registerBlocks()
	{
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.addAfter(Blocks.JUKEBOX, Blocks.DECORATED_POT));
	}
}
