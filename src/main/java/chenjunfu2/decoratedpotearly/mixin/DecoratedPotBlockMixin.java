package chenjunfu2.decoratedpotearly.mixin;

import chenjunfu2.decoratedpotearly.api.DecoratedPotBlockEntityHolder;
import chenjunfu2.decoratedpotearly.registry.ModParticles;
import chenjunfu2.decoratedpotearly.registry.ModSoundEvents;
import chenjunfu2.decoratedpotearly.registry.ModWobbleType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BlockWithEntity//继承基类用于super方法
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
				//这里是ItemUse路径
				ItemStack playerStack = player.getMainHandStack();//只看主手，副手无反应
				
				ItemStack itemStack = ((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$getStack();
				if (!playerStack.isEmpty() && (itemStack.isEmpty() || ItemStack.canCombine(itemStack, playerStack) && itemStack.getCount() < itemStack.getMaxCount()))
				{
					//动画
					((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$wobble(ModWobbleType.POSITIVE);
					player.incrementStat(Stats.USED.getOrCreateStat(playerStack.getItem()));
					
					//物品交互
					float f;
					if (((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$isEmpty())
					{
						ItemStack itemStack2 = !player.isCreative() ? playerStack.split(1) : playerStack.copyWithCount(1);
						((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$setStack(itemStack2);
						f = (float)itemStack2.getCount() / itemStack2.getMaxCount();
					}
					else
					{
						if (!player.isCreative())
						{
							playerStack.decrement(1);
						}
						itemStack.increment(1);
						f = (float)itemStack.getCount() / itemStack.getMaxCount();
					}
					
					//声音
					world.playSound(null, pos, ModSoundEvents.BLOCK_DECORATED_POT_INSERT, SoundCategory.BLOCKS, 1.0F, 0.7F + 0.5F * f);
					
					//粒子效果
					if (world instanceof ServerWorld serverWorld)
					{
						serverWorld.spawnParticles(ModParticles.DUST_PLUME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 7, 0.0, 0.0, 0.0, 0.0);
					}
					
					//更新
					decoratedPotBlockEntity.markDirty();
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					return ActionResult.SUCCESS;
				}
				else
				{
					//这里走正常方块use路径
					world.playSound(null, pos, ModSoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					((DecoratedPotBlockEntityHolder)decoratedPotBlockEntity).decoratedpotearly$wobble(ModWobbleType.NEGATIVE);
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					
					return ActionResult.SUCCESS;//并总是返回成功
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
	
	//比较器方法
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	
	//NBT修复
	@ModifyReturnValue(
		method = "getDroppedStacks",
		at = @At(
			value = "RETURN"
		)
	)
	private List<ItemStack> modifyDroppedStacks(List<ItemStack> original)
	{
		//已在合成配方中mixin去掉id，并且正常掉落不会附加id（和潜影盒不同），所以无需处理id
		
		//不为空并且仅含1物品才处理
		if (original == null || original.size() != 1)
		{
			return original;
		}

		//拿到物品
		var item = original.get(0);
		if (item == null)
		{
			return original;
		}

		//获取掉落物的itemTag
		var itemTag = item.getNbt();

		//必须存在且类型正确
		if(itemTag == null || !itemTag.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
		{
			return original;
		}

		//获取方块实体tag
		var tagBETag = itemTag.getCompound("BlockEntityTag");

		do
		{
			//然后检查是否全部由brick组成，是的话直接删除
			if(!tagBETag.contains("sherds",NbtElement.LIST_TYPE))
			{
				break;
			}

			var sherds = (NbtList)tagBETag.get("sherds");
			if(sherds.size() != 4 || sherds.getHeldType() != NbtElement.STRING_TYPE)
			{
				break;
			}

			//确定4个都是brick
			boolean bIsAllBrick = true;
			for(var it : sherds)
			{
				if(!((NbtString)it).asString().equals("minecraft:brick"))
				{
					bIsAllBrick = false;
					break;
				}
			}

			//是就删除
			if(bIsAllBrick)
			{
				tagBETag.remove("sherds");
			}

		}while(false);

		//如果移除后啥tagBETag都没了，那么把BlockEntityTag也删除，不要留下空的BlockEntityTag
		if(tagBETag.isEmpty())
		{
			itemTag.remove("BlockEntityTag");
		}

		//如果移除BlockEntityTag之后itemTag里啥都没了，那么把itemTag设为null，不要留下空（但是非null）的itemTag
		if(itemTag.isEmpty())
		{
			item.setNbt(null);//需要设置item内部的itemTag，而不是单纯的赋值itemTag为null
		}

		return original;
	}

}