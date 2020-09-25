package twistedgate.immersiveposts.common.tileentity;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import twistedgate.immersiveposts.IPOContent;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;

public class PostBaseTileEntity extends IPOTileEntityBase{
	protected static final Lazy<BlockState> EMPTY=Lazy.of(()->Blocks.AIR.getDefaultState());
	
	@Nonnull
	protected ItemStack stack=ItemStack.EMPTY;
	@Nonnull
	protected Lazy<BlockState> coverstate=EMPTY;
	/** Horizontal Only */
	protected Direction facing=Direction.NORTH;
	
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
	
	public Direction getFacing(){
		return this.facing;
	}
	
	public void setFacing(Direction facing){
		if(Direction.Plane.HORIZONTAL.test(facing)){
			this.facing=facing;
		}
	}
	
	/**
	 * Set's the stack to be used for the cover. Automaticly causes a blockupdate to itself.
	 * 
	 * @param stack The stack to be used, requires the item to be a instance of {@link BlockItem}.
	 * @return true if it changed, false otherwise
	 */
	public boolean setStack(ItemStack stack){
		ItemStack last=this.stack;
		if(stack == null || stack.isEmpty()){
			this.stack=ItemStack.EMPTY;
			
		}else if(stack.getItem() instanceof BlockItem){
			this.stack=stack;
		}
		
		boolean changed=!ItemStack.areItemStacksEqual(this.stack, last);
		
		updateLazy(changed);
		
		if(changed){
			markDirty();
		}
		
		return changed;
	}
	
	@Override
	protected CompoundNBT writeCustom(CompoundNBT compound){
		compound.putString("facing", this.facing != null ? this.facing.getName2() : Direction.NORTH.getName2());
		compound.put("stack", this.stack.serializeNBT());
		return compound;
	}
	
	@Override
	protected void readCustom(CompoundNBT compound){
		this.facing=Direction.byName(compound.getString("facing"));
		ItemStack last=this.stack;
		this.stack=ItemStack.read(compound.getCompound("stack"));
		
		boolean changed=!ItemStack.areItemStacksEqual(this.stack, last);
		
		updateLazy(changed);
		
		if(changed && getWorld() != null){
			getWorld().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
		}
	}
	
	public boolean interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand){
		ItemStack held=player.getHeldItemMainhand();
		
		if(held==ItemStack.EMPTY){
			if(player.isSneaking() && !getStack().isEmpty()){
				if(!world.isRemote){
					setFacing(Direction.NORTH);
					Block.spawnAsEntity(world, pos, getStack());
					setStack(ItemStack.EMPTY);
					
					world.setBlockState(pos, state.with(BlockPostBase.HIDDEN, false));
				}
				
				return true;
			}
			
		}else if(held.getItem() instanceof BlockItem && isUsableCover(Block.getBlockFromItem(held.getItem()), world, pos)){
			if(getStack().isEmpty()){
				if(!world.isRemote){
					setFacing(player.getHorizontalFacing().getOpposite());
					
					ItemStack copy=held.copy();
					copy.setCount(1);
					setStack(copy);
					
					if(!player.isCreative()){
						held.shrink(1);
					}
					
					world.setBlockState(pos, state.with(BlockPostBase.HIDDEN, true).with(BlockPostBase.WATERLOGGED, false));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/** Used to check wether the base can hide within a certain block */
	private boolean isUsableCover(@Nonnull Block block, @Nonnull IBlockReader reader, @Nonnull BlockPos pos){
		BlockState state=block.getDefaultState();
		return block!=Blocks.AIR && state.isNormalCube(reader, pos) && state.isOpaqueCube(reader, pos);
	}
	
	protected void updateLazy(boolean changed){
		if(!changed) return;
		
		if(this.stack == null || this.stack.isEmpty()){
			this.coverstate=EMPTY;
		}else{
			this.coverstate=Lazy.of(()->{
				if(this.stack.getItem() instanceof BlockItem){
					BlockState state=((BlockItem)this.stack.getItem()).getBlock().getDefaultState();
					
					Optional<DirectionProperty> prop=state.getProperties().stream()
							.filter(p->p instanceof DirectionProperty && p.getName().equals("facing"))
							.map(p->(DirectionProperty)p)
							.filter(p->p.getAllowedValues().stream().allMatch(d->Direction.Plane.HORIZONTAL.test(d)))
							.findAny();
					
					if(prop.isPresent()){
						state=state.with(prop.get(), this.facing);
					}
					
					return state;
				}
				
				return EMPTY.get();
			});
		}
	}
}
