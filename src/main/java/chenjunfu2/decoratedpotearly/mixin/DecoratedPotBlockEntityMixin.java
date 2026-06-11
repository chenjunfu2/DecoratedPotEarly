package chenjunfu2.decoratedpotearly.mixin;


import chenjunfu2.decoratedpotearly.api.DecoratedPotBlockEntityHolder;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin implements DecoratedPotBlockEntityHolder, Inventory
{
	// Data
	@Unique
	private ItemStack decoratedPotItemStack = ItemStack.EMPTY;
	
	// Mixin
	@Inject(method = "writeNbt", at = @At(value = "RETURN"))
	void writeNbtInject(NbtCompound nbt, CallbackInfo ci)
	{
		if (!this.decoratedPotItemStack.isEmpty())
		{
			NbtCompound itemNBT = new NbtCompound();
			this.decoratedPotItemStack.writeNbt(itemNBT);
			nbt.put("item", itemNBT);
		}
	}
	
	@Inject(method = "readNbt", at = @At(value = "RETURN"))
	void readNbtInject(NbtCompound nbt, CallbackInfo ci)
	{
		if (nbt.contains("item", NbtElement.COMPOUND_TYPE))
		{
			NbtCompound itemNBT = nbt.getCompound("item");
			this.decoratedPotItemStack = ItemStack.fromNbt(itemNBT);
		}
	}
	
	// DecoratedPotBlockEntityHolder
	@Unique
	@Override
	public ItemStack decoratedpotearly$getStack()
	{
		return this.decoratedPotItemStack;
	}

	@Unique
	@Override
	public void decoratedpotearly$setStack(ItemStack decoratedPotItemStack)
	{
		this.decoratedPotItemStack = decoratedPotItemStack;
	}

	@Unique
	@Override
	public boolean decoratedpotearly$isEmpty()
	{
		return this.decoratedPotItemStack.isEmpty();
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
		return this.decoratedPotItemStack.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot)
	{
		if(slot != 0)
		{
			return ItemStack.EMPTY;
		}
		
		
		return this.decoratedPotItemStack;
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		if(slot != 0 || amount <= 0 || this.decoratedPotItemStack.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		
		ItemStack retStack = this.decoratedPotItemStack.split(amount);
		
		//if (!this.decoratedPotItemStack.isEmpty())
		//{
		//	this.markDirty();
		//}

		return retStack;
	}

	@Override
	public ItemStack removeStack(int slot)
	{
		if(slot != 0)
		{
			return ItemStack.EMPTY;
		}
		
		//this.markDirty();
		return this.decoratedPotItemStack = ItemStack.EMPTY;
	}
	
	@Override
	public void setStack(int slot, ItemStack decoratedPotItemStack)
	{
		if(slot != 0)
		{
			return;
		}
		
		this.decoratedPotItemStack = decoratedPotItemStack;
	}
	
	//DecoratedPotBlockEntity以实现，默认自动覆写，无需重复调用，否则会导致java.lang.StackOverflowError
	//@Override
	//public void markDirty()
	//{
	//	((DecoratedPotBlockEntity)(Object)this).markDirty();
	//}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}
	
	@Override
	public void clear()
	{
		this.decoratedPotItemStack = ItemStack.EMPTY;
	}
}
