package chenjunfu2.decoratedpotearly.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
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
		var itemTag = original.getNbt();
		if(itemTag == null || ! itemTag.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
		{
			return original;
		}
		
		//获取方块实体tag
		var tagBETag = itemTag.getCompound("BlockEntityTag");
		
		//移除无用id
		tagBETag.remove("id");
		
		//因为总是有其它tag，不做后续处理
		
		return original;
	}
}
