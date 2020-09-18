package twistedgate.immersiveposts.common.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import twistedgate.immersiveposts.IPOContent;

public class PostBaseTileEntity extends IPOTileEntityBase{
	
	@Nonnull
	public ItemStack stack=ItemStack.EMPTY;
	
	public PostBaseTileEntity(){
		super(IPOContent.TE_POSTBASE);
		reset();
	}
	
	public void reset(){
		this.stack=ItemStack.EMPTY;
	}
	
	@Override
	protected CompoundNBT writeCustom(CompoundNBT compound){
		compound.put("stack", this.stack.serializeNBT());
		return compound;
	}
	
	@Override
	protected void readCustom(CompoundNBT compound){
		this.stack=ItemStack.read(compound.getCompound("stack"));
	}
}
