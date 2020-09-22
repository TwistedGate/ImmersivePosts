package twistedgate.immersiveposts.common.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import twistedgate.immersiveposts.IPOContent;

public class PostBaseTileEntity extends IPOTileEntityBase{
	
	@Nonnull
	protected ItemStack stack=ItemStack.EMPTY;
	
	public PostBaseTileEntity(){
		super(IPOContent.TE_POSTBASE);
	}
	
	public ItemStack getStack(){
		return this.stack;
	}
	
	public void setStack(ItemStack stack){
		if(stack==null){
			stack=ItemStack.EMPTY;
			
		}else if(stack.getItem() instanceof BlockItem){
			this.stack=stack;
			markDirty();
			requestModelDataUpdate();
			
		}
	}
	
	public void reset(){
		this.stack=ItemStack.EMPTY;
		markDirty();
	}
	
	@Override
	protected CompoundNBT writeCustom(CompoundNBT compound){
		compound.put("stack", this.stack.serializeNBT());
		return compound;
	}
	
	@Override
	protected void readCustom(CompoundNBT compound){
		this.stack=ItemStack.read(compound.getCompound("stack"));
		requestModelDataUpdate();
	}
}
