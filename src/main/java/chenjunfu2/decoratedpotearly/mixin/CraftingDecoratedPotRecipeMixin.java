package chenjunfu2.decoratedpotearly.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingDecoratedPotRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingDecoratedPotRecipe.class)
public abstract class CraftingDecoratedPotRecipeMixin
{
	@ModifyReturnValue
	(
		method = "getPotStackWith",
		at = @At(value = "RETURN")
	)
	private static ItemStack getPotStackWithModifyReturnValue(ItemStack original)
	{
		var itemNbt = original.getNbt();
		if(itemNbt != null)
		{
			itemNbt.remove("id");
		}
		
		return original;
	}
}
