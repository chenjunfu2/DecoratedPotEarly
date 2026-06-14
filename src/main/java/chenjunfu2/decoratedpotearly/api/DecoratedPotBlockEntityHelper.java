package chenjunfu2.decoratedpotearly.api;

import chenjunfu2.decoratedpotearly.data.DecoratedPotWobbleType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface DecoratedPotBlockEntityHelper
{
	@Unique
	ItemStack decoratedpotearly$getStack();
	
	@Unique
	void decoratedpotearly$setStack(ItemStack stack);
	
	@Unique
	boolean decoratedpotearly$isEmpty();
	
	@Unique
	void decoratedpotearly$wobble(DecoratedPotWobbleType wobbleType);
	
	@Unique
	long decoratedpotearly$getLastWobbleTime();
	
	@Unique
	void decoratedpotearly$setLastWobbleTime(long wobbleTime);
	
	@Unique
	DecoratedPotWobbleType decoratedpotearly$getLastWobbleType();
	
	@Unique
	void decoratedpotearly$setLastWobbleType(DecoratedPotWobbleType wobbleType);
}
