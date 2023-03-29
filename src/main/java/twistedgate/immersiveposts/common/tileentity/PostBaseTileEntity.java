package twistedgate.immersiveposts.common.tileentity;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.common.util.Lazy;
import twistedgate.immersiveposts.common.IPOTileTypes;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;

public class PostBaseTileEntity extends IPOTileEntityBase{
	protected static final Lazy<BlockState> EMPTY = Lazy.of(Blocks.AIR::defaultBlockState);
	
	@Nonnull
	protected ItemStack stack = ItemStack.EMPTY;
	@Nonnull
	protected Lazy<BlockState> coverstate = EMPTY;
	/** Horizontal Only */
	protected Direction facing = Direction.NORTH;
	
	public PostBaseTileEntity(BlockPos pWorldPosition, BlockState pBlockState){
		super(IPOTileTypes.POST_BASE.get(), pWorldPosition, pBlockState);
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
			this.facing = facing;
			setChanged();
		}
	}
	
	/**
	 * Set's the stack to be used for the cover. Automaticly causes a blockupdate to itself.
	 * 
	 * @param stack The stack to be used, requires the item to be a instance of {@link BlockItem}.
	 * @return true if it changed, false otherwise
	 */
	public boolean setStack(ItemStack stack){
		ItemStack last = this.stack;
		if(stack == null || stack.isEmpty()){
			this.stack = ItemStack.EMPTY;
			
		}else if(stack.getItem() instanceof BlockItem){
			this.stack = stack;
		}
		
		boolean changed = !ItemStack.matches(this.stack, last);
		
		updateLazy(changed);
		
		if(changed){
			setChanged();
		}
		
		return changed;
	}
	
	@Override
	protected CompoundTag writeCustom(CompoundTag compound){
		compound.putString("facing", this.facing != null ? this.facing.getName() : Direction.NORTH.getName());
		compound.put("stack", this.stack.serializeNBT());
		return compound;
	}
	
	@Override
	protected void readCustom(CompoundTag compound){
		this.facing = Direction.byName(compound.getString("facing"));
		ItemStack last = this.stack;
		this.stack = ItemStack.of(compound.getCompound("stack"));
		
		boolean changed = !ItemStack.matches(this.stack, last);
		
		updateLazy(changed);
		
		if(changed && getLevel() != null){
			getLevel().sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
		}
	}
	
	public boolean interact(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand){
		ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
		
		if(held == ItemStack.EMPTY){
			if(player.isShiftKeyDown() && !getStack().isEmpty()){
				if(!world.isClientSide){
					setFacing(Direction.NORTH);
					Block.popResource(world, pos, getStack());
					setStack(ItemStack.EMPTY);
					
					world.setBlockAndUpdate(pos, state.setValue(PostBaseBlock.HIDDEN, false));
				}
				
				return true;
			}
			
		}else if(held.getItem() instanceof BlockItem && isUsableCover(Block.byItem(held.getItem()), world, pos)){
			if(getStack().isEmpty()){
				if(!world.isClientSide){
					setFacing(player.getDirection().getOpposite());
					
					ItemStack copy = held.copy();
					copy.setCount(1);
					setStack(copy);
					
					if(!player.isCreative()){
						held.shrink(1);
						if(held.isEmpty()){
							player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
						}
					}
					
					world.setBlockAndUpdate(pos, state.setValue(PostBaseBlock.HIDDEN, true).setValue(PostBaseBlock.WATERLOGGED, false));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/** Used to check wether the base can hide within a certain block */
	private boolean isUsableCover(@Nonnull Block block, @Nonnull BlockGetter reader, @Nonnull BlockPos pos){
		BlockState state = block.defaultBlockState();
		return block != Blocks.AIR && state.isRedstoneConductor(reader, pos) && state.isSolidRender(reader, pos);
	}
	
	protected void updateLazy(boolean changed){
		if(!changed)
			return;
		
		if(this.stack == null || this.stack.isEmpty()){
			this.coverstate = EMPTY;
		}else{
			this.coverstate = Lazy.of(() -> {
				if(this.stack.getItem() instanceof BlockItem){
					BlockState state = ((BlockItem) this.stack.getItem()).getBlock().defaultBlockState();
					
					Optional<DirectionProperty> prop = state.getProperties().stream()
							.filter(p -> p instanceof DirectionProperty && p.getName().equals("facing"))
							.map(p -> (DirectionProperty) p)
							.filter(p -> p.getPossibleValues().stream().allMatch(d -> Direction.Plane.HORIZONTAL.test(d)))
							.findAny();
					
					if(prop.isPresent()){
						state = state.setValue(prop.get(), this.facing);
					}
					
					return state;
				}
				
				return EMPTY.get();
			});
		}
	}
}
