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
	
	@Nonnull
	public ItemStack getStack(){
		return this.stack;
	}
	
	public void setStack(ItemStack stack){
		ItemStack last=this.stack;
		if(stack==null){
			this.stack=ItemStack.EMPTY;
			
		}else if(stack.getItem() instanceof BlockItem){
			this.stack=stack;
		}
		
		if(this.stack!=last){
			markDirty();
			getWorldNonnull().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
		}
	}
	
	public void reset(){
		ItemStack last=this.stack;
		this.stack=ItemStack.EMPTY;
		
		if(this.stack!=last){
			markDirty();
			getWorldNonnull().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
		}
	}
	
	@Override
	protected CompoundNBT writeCustom(CompoundNBT compound){
		compound.put("stack", this.stack.serializeNBT());
		return compound;
	}
	
	@Override
	protected void readCustom(CompoundNBT compound){
		ItemStack last=this.stack;
		this.stack=ItemStack.read(compound.getCompound("stack"));
		
		if(this.stack!=last && getWorld()!=null){
			getWorld().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
		}
	}
}
