package chenjunfu2.decoratedpotearly.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow
	protected ServerWorld world;//偷出来
	
	@ModifyVariable
	(
		method = "tryBreakBlock",
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V",
			shift = At.Shift.AFTER
		),
		ordinal = 0
	)
	private BlockState updateBlockState(BlockState original, @Local BlockPos pos)
	{
		return world.getBlockState(pos);
	}
}
