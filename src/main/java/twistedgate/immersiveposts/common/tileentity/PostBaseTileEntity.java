package twistedgate.immersiveposts.common.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Lazy;
import twistedgate.immersiveposts.IPOContent;

public class PostBaseTileEntity extends IPOTileEntityBase{
	protected static final Lazy<BlockState> EMPTY=Lazy.of(()->Blocks.AIR.getDefaultState());
	
	@Nonnull
	protected ItemStack stack=ItemStack.EMPTY;
	@Nonnull
	protected Lazy<BlockState> coverstate=EMPTY;
	
	public PostBaseTileEntity(){
		super(IPOContent.TE_POSTBASE);
	}
	
	@Nonnull
	public ItemStack getStack(){
		return this.stack;
	}
	
	@Nonnull
	public BlockState getCoverState(){
		if(this.stack.isEmpty()){
			return EMPTY.get();
		}
		return this.coverstate.get();
	}
	
	/**
	 * Set's the stack to be used for the cover. Automaticly causes a blockupdate to itself.
	 * 
	 * @param stack The stack to be used, requires the item to be a instance of {@link BlockItem}.
	 * @return The previous stack (may be {@link ItemStack#EMPTY})
	 */
	public ItemStack setStack(ItemStack stack){
		return setStack(stack, true);
	}
	
	/**
	 * Set's the stack to be used for the cover. Requires the item to be a instance of {@link BlockItem}!
	 * 
	 * @param stack The stack to be used, requires the item to be a instance of {@link BlockItem}.
	 * @param notifySelf If the TE should blockupdate itself
	 * @return The previous stack (may be {@link ItemStack#EMPTY})
	 */
	public ItemStack setStack(ItemStack stack, boolean notifySelf){
		ItemStack last=this.stack;
		if(stack==null || stack.isEmpty()){
			this.stack=ItemStack.EMPTY;
			
		}else if(stack.getItem() instanceof BlockItem){
			this.stack=stack;
		}
		boolean changed=this.stack != last;
		
		
		if(changed){
			updateLazy(changed);
			
			markDirty();
			
			if(notifySelf){
				getWorldNonnull().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
			}
		}
		
		return last;
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
		boolean changed=this.stack != last;
		
		updateLazy(changed);
		
		if(changed && getWorld() != null){
			getWorld().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
		}
	}
	
	protected void updateLazy(boolean changed){
		if(!changed) return;
		
		if(this.stack == null || this.stack.isEmpty()){
			this.coverstate=EMPTY;
		}else{
			this.coverstate=Lazy.of(() -> {
				if(this.stack.getItem() instanceof BlockItem){
					return ((BlockItem)this.stack.getItem()).getBlock().getDefaultState();
				}
				
				return EMPTY.get();
			});
		}
	}
}
