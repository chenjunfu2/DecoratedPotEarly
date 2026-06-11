package chenjunfu2.decoratedpotearly.mixin;

import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ActionResult.class)
public interface ActionResultAccessor
{
	@Invoker("<init>")
	ActionResult callConstructor(String name, int ordinal);
	
	@Accessor(value = "field_5813", remap = false)
	static ActionResult[] getValues() {
		throw new AssertionError();
	}
	
	@Accessor(value = "field_5813", remap = false)
	static void setValues(ActionResult[] values) {
		throw new AssertionError();
	}
}
