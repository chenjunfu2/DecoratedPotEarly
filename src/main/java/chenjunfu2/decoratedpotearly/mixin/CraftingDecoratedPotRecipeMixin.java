package chenjunfu2.decoratedpotearly.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingDecoratedPotRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingDecoratedPotRecipe.class)
public abstract class CraftingDecoratedPotRecipeMixin
{
	@Inject
	(
		method = "getPotStackWith",
		cancellable = true,
		at = @At
		(
			value = "INVOKE",
			shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;toNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
		)
	)
	private static void getPotStackWithInject(DecoratedPotBlockEntity.Sherds sherds, CallbackInfoReturnable<ItemStack> cir, @Local ItemStack itemStack)
	{
		if(sherds.back() == Items.BRICK &&
		   sherds.left() == Items.BRICK &&
		   sherds.right() == Items.BRICK &&
		   sherds.front() == Items.BRICK)
		{
			cir.setReturnValue(itemStack);
			cir.cancel();
		}
	}
	
	@WrapOperation
	(
		method = "getPotStackWith",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;setBlockEntityNbt(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/nbt/NbtCompound;)V")
	)
	private static void getPotStackWithWrapOperation(ItemStack stack, BlockEntityType<?> blockEntityType, NbtCompound tag, Operation<Void> original)
	{
		if (tag.isEmpty())
		{
			stack.removeSubNbt("BlockEntityTag");
		}
		else
		{
			stack.setSubNbt("BlockEntityTag", tag);
		}
	}
}
