package chenjunfu2.decoratedpotearly.mixin;


import chenjunfu2.decoratedpotearly.api.DecoratedPotBlockEntityHelper;
import chenjunfu2.decoratedpotearly.data.DecoratedPotWobbleType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin extends BlockEntity implements DecoratedPotBlockEntityHelper, Inventory
{
	// super
	public DecoratedPotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	// Data
	@Unique
	public long decoratedpotearly$lastWobbleTime;
	
	@Unique
	public DecoratedPotWobbleType decoratedpotearly$lastWobbleType;
	
	@Unique
	private ItemStack decoratedpotearly$stack = ItemStack.EMPTY;
	
	// Override BlockEntity
	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		if (this.world != null && type == 1 && data >= 0 && data < DecoratedPotWobbleType.values().length)
		{
			this.decoratedpotearly$lastWobbleTime = this.world.getTime();
			this.decoratedpotearly$lastWobbleType = DecoratedPotWobbleType.values()[data];
			return true;
		}
		else
		{
			return super.onSyncedBlockEvent(type, data);
		}
	}
	
	// Mixin
	@Inject(method = "writeNbt", at = @At(value = "RETURN"))
	void writeNbtInject(NbtCompound nbt, CallbackInfo ci)
	{
		if (!this.decoratedpotearly$stack.isEmpty())
		{
			NbtCompound itemNBT = new NbtCompound();
			this.decoratedpotearly$stack.writeNbt(itemNBT);
			nbt.put("item", itemNBT);
		}
	}
	
	@Inject(method = "readNbt", at = @At(value = "RETURN"))
	void readNbtInject(NbtCompound nbt, CallbackInfo ci)
	{
		if (nbt.contains("item", NbtElement.COMPOUND_TYPE))
		{
			NbtCompound itemNBT = nbt.getCompound("item");
			this.decoratedpotearly$stack = ItemStack.fromNbt(itemNBT);
		}
	}
	
	// DecoratedPotBlockEntityHelper
	@Unique
	@Override
	public ItemStack decoratedpotearly$getStack()
	{
		return this.decoratedpotearly$stack;
	}

	@Unique
	@Override
	public void decoratedpotearly$setStack(ItemStack decoratedPotItemStack)
	{
		this.decoratedpotearly$stack = decoratedPotItemStack;
	}

	@Unique
	@Override
	public boolean decoratedpotearly$isEmpty()
	{
		return this.decoratedpotearly$stack.isEmpty();
	}
	
	@Unique
	@Override
	public void decoratedpotearly$wobble(DecoratedPotWobbleType wobbleType)
	{
		if (this.world != null && !this.world.isClient())
		{
			this.world.addSyncedBlockEvent(this.getPos(), this.getCachedState().getBlock(), 1, wobbleType.ordinal());
		}
	}
	
	@Unique
	@Override
	public long decoratedpotearly$getLastWobbleTime()
	{
		return decoratedpotearly$lastWobbleTime;
	}
	
	@Unique
	@Override
	public void decoratedpotearly$setLastWobbleTime(long wobbleTime)
	{
		this.decoratedpotearly$lastWobbleTime = wobbleTime;
	}
	
	@Unique
	@Override
	public DecoratedPotWobbleType decoratedpotearly$getLastWobbleType()
	{
		return decoratedpotearly$lastWobbleType;
	}
	
	@Unique
	@Override
	public void decoratedpotearly$setLastWobbleType(DecoratedPotWobbleType wobbleType)
	{
		this.decoratedpotearly$lastWobbleType = wobbleType;
	}
	
	//Inventory
	@Override
	public int size()
	{
		return 1;//只有一个
	}
	
	@Override
	public boolean isEmpty()
	{
		return this.decoratedpotearly$stack.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot)
	{
		if(slot != 0)
		{
			return ItemStack.EMPTY;
		}
		
		
		return this.decoratedpotearly$stack;
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		if(slot != 0 || amount <= 0 || this.decoratedpotearly$stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		return this.decoratedpotearly$stack.split(amount);
	}

	@Override
	public ItemStack removeStack(int slot)
	{
		if(slot != 0)
		{
			return ItemStack.EMPTY;
		}
		
		return this.decoratedpotearly$stack = ItemStack.EMPTY;
	}
	
	@Override
	public void setStack(int slot, ItemStack decoratedPotItemStack)
	{
		if(slot != 0)
		{
			return;
		}
		
		this.decoratedpotearly$stack = decoratedPotItemStack;
	}
	
	@Override
	@Intrinsic
	public void markDirty()
	{
		super.markDirty();
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return Inventory.canPlayerUse(((DecoratedPotBlockEntity)(Object)this),player);
	}
	
	@Override
	public void clear()
	{
		this.decoratedpotearly$stack = ItemStack.EMPTY;
	}
}
