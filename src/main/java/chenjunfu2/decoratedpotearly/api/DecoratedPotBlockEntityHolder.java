package chenjunfu2.decoratedpotearly.api;

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
}
