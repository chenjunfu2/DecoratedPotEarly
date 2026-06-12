package chenjunfu2.decoratedpotearly.mixin;

import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.state.property.BooleanProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DecoratedPotBlock.class)
public interface DecoratedPotBlockAccessor
{
	@Accessor("CRACKED")
	static BooleanProperty getCRACKED()
	{
		throw new AssertionError();//Mixin自动替换
	}
}
