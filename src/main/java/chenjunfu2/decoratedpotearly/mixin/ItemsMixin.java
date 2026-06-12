package chenjunfu2.decoratedpotearly.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Items.class)
public abstract class ItemsMixin
{
	@WrapOperation
	(
		method = "<clinit>",
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;",
			ordinal = 0//第0个就是：public static final Item DECORATED_POT = register(new BlockItem(Blocks.DECORATED_POT, new Item.Settings().maxCount(1)));
		)
	)
	private static Item.Settings modifyDecoratedPotStackSize(Item.Settings instance, int maxCount, Operation<Item.Settings> original)
	{
		//取消调用，回到64
		//original.call(instance, 64);
		return instance;
	}
}
