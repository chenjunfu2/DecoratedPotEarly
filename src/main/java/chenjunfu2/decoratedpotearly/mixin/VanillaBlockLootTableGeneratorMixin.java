package chenjunfu2.decoratedpotearly.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.predicate.StatePredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VanillaBlockLootTableGenerator.class)
public abstract class VanillaBlockLootTableGeneratorMixin
{
	@WrapOperation
	(
		method = "decoratedPotDrops",
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;",
			ordinal = 0
		)
	)
	LootPoolEntry.Builder decoratedPotDropsWrapOperation0(LeafEntry.Builder instance, LootCondition.Builder builder, Operation<LootPoolEntry.Builder> original, @Local Block block)
	{
		//丢弃原先的builder，替换为自定义builder
		return original.call(instance,
			BlockStatePropertyLootCondition
			.builder(block)
			.properties
			(
				StatePredicate.Builder
				.create()
				.exactMatch(DecoratedPotBlockAccessor.getCRACKED(), true)
			)
		);
	}
	
	@WrapOperation
	(
		method = "decoratedPotDrops",
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;",
			ordinal = 1
		)
	)
	LootPoolEntry.Builder decoratedPotDropsWrapOperation1(LeafEntry.Builder instance, LootCondition.Builder builder, Operation<LootPoolEntry.Builder> original)
	{
		return instance;//跳过第二个调用
	}

}
