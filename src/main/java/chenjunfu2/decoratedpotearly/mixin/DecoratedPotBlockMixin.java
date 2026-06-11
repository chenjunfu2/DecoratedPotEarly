package chenjunfu2.decoratedpotearly.mixin;

import chenjunfu2.decoratedpotearly.api.DecoratedPotBlockEntityHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BlockWithEntity
{
	protected DecoratedPotBlockMixin(Settings settings)
	{
		super(settings);
	}
	
	//覆盖DecoratedPotBlock的基类onUse方法
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		//DecoratedPotEarly.LOGGER.info("onUse");
		if (world.getBlockEntity(pos) instanceof DecoratedPotBlockEntity decoratedPotBlockEntity)
		{
			if (world.isClient)
			{
				return ActionResult.SUCCESS;
			}
			else
			{
				ItemStack playerStack = player.getMainHandStack();//只看主手，副手无反应
				
				ItemStack itemStack = ((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$getStack();
				if (!playerStack.isEmpty() && (itemStack.isEmpty() || ItemStack.canCombine(itemStack, playerStack) && itemStack.getCount() < itemStack.getMaxCount()))
				{
					//decoratedPotBlockEntity.wobble(DecoratedPotBlockEntity.WobbleType.POSITIVE);
					player.incrementStat(Stats.USED.getOrCreateStat(playerStack.getItem()));
					//ItemStack itemStack2 = splitUnlessCreative(playerStack,1, player);
					
					//float f;
					if (((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$isEmpty())
					{
						ItemStack itemStack2 = playerStack.split(1);
						((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$setStack(itemStack2);
						//f = (float)itemStack2.getCount() / itemStack2.getMaxCount();
					}
					else
					{
						if (!player.isCreative())
						{
							playerStack.decrement(1);
						}
						itemStack.increment(1);
						//f = (float)itemStack.getCount() / itemStack.getMaxCount();
					}
					
					//world.playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT, SoundCategory.BLOCKS, 1.0F, 0.7F + 0.5F * f);
					
					//粒子效果先去掉，后面找替代
					//if (world instanceof ServerWorld serverWorld)
					//{
					//	serverWorld.spawnParticles(ParticleTypes.DUST_PLUME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 7, 0.0, 0.0, 0.0, 0.0);
					//}
					
					decoratedPotBlockEntity.markDirty();
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					return ActionResult.SUCCESS;
				}
				else
				{
					//return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
					return ActionResult.FAIL;
				}
			}
		}
		else
		{
			return ActionResult.PASS;
		}
	}
	
	//覆盖基类
	//内部物品掉落
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if (!state.isOf(newState.getBlock()))
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory)
			{
				ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
				world.updateComparators(pos, (DecoratedPotBlock)(Object)this);
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	//还差一个比较器方法
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

}