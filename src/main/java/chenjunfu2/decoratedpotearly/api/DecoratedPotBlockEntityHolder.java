package chenjunfu2.decoratedpotearly.api;

import chenjunfu2.decoratedpotearly.registry.ModWobbleType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface DecoratedPotBlockEntityHolder
{
	@Unique
	ItemStack decoratedpotearly$getStack();
	
	@Unique
	void decoratedpotearly$setStack(ItemStack stack);
	
	@Unique
	boolean decoratedpotearly$isEmpty();
	
	@Unique
	void decoratedpotearly$wobble(ModWobbleType wobbleType);
}
