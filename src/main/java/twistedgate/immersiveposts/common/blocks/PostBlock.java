package twistedgate.immersiveposts.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.math.Vector3f;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.util.Utils;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.IPOTags;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumHTrussType;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * All-in-one package. Containing everything into one neat class is the best.
 * @author TwistedGate
 */
public class PostBlock extends GenericPostBlock implements IPostBlock, SimpleWaterloggedBlock{
	public static final DustParticleOptions URAN_PARTICLE = new DustParticleOptions(new Vector3f(0.0F, 1.0F, 0.0F), 1.0F);
	
	public static final VoxelShape POST_SHAPE = Shapes.box(0.3125, 0.0, 0.3125, 0.6875, 1.0, 0.6875);
	
	public static final VoxelShape LPARM_NORTH_BOUNDS = Shapes.box(0.3125, 0.25, 0.0, 0.6875, 0.75, 0.3125);
	public static final VoxelShape LPARM_SOUTH_BOUNDS = Shapes.box(0.3125, 0.25, 0.6875, 0.6875, 0.75, 1.0);
	public static final VoxelShape LPARM_EAST_BOUNDS = Shapes.box(0.6875, 0.25, 0.3125, 1.0, 0.75, 0.6875);
	public static final VoxelShape LPARM_WEST_BOUNDS = Shapes.box(0.0, 0.25, 0.3125, 0.3125, 0.75, 0.6875);
	
	// LPARM = (Little-)Post Arm
	public static final BooleanProperty LPARM_NORTH = BooleanProperty.create("parm_north");
	public static final BooleanProperty LPARM_EAST = BooleanProperty.create("parm_east");
	public static final BooleanProperty LPARM_SOUTH = BooleanProperty.create("parm_south");
	public static final BooleanProperty LPARM_WEST = BooleanProperty.create("parm_west");
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final EnumProperty<EnumPostType> TYPE = EnumProperty.create("type", EnumPostType.class);
	
	public static final EnumProperty<EnumFlipState> FLIPSTATE = EnumProperty.create("flipstate", EnumFlipState.class);
	
	public PostBlock(IPostMaterial material){
		super(material);
		
		registerDefaultState(getStateDefinition().any()
				.setValue(WATERLOGGED, false)
				.setValue(FACING, Direction.NORTH)
				.setValue(FLIPSTATE, EnumFlipState.UP)
				.setValue(TYPE, EnumPostType.POST_TOP)
				.setValue(LPARM_NORTH, false)
				.setValue(LPARM_EAST, false)
				.setValue(LPARM_SOUTH, false)
				.setValue(LPARM_WEST, false)
				);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder){
		builder.add(
				WATERLOGGED, FACING, FLIPSTATE, TYPE,
				LPARM_NORTH, LPARM_EAST, LPARM_SOUTH, LPARM_WEST
				);
	}
	
	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos){
		return 0;
	}
	
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos){
		return 1.0F;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos){
		return !state.getValue(WATERLOGGED);
	}
	
	@Override
	public FluidState getFluidState(BlockState state){
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getLevel().getFluidState(context.getClickedPos());
		
		return state.setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos){
		if(state.getValue(WATERLOGGED)){
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		
		if(state.getValue(TYPE).id() > 1){
			return state;
		}
		
		boolean b0 = canConnect(world, pos, Direction.NORTH);
		boolean b1 = canConnect(world, pos, Direction.EAST);
		boolean b2 = canConnect(world, pos, Direction.SOUTH);
		boolean b3 = canConnect(world, pos, Direction.WEST);
		
		return state.setValue(LPARM_NORTH, b0)
					.setValue(LPARM_EAST, b1)
					.setValue(LPARM_SOUTH, b2)
					.setValue(LPARM_WEST, b3);
	}
	
	@Override
	public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity){
		return true;
	}
	
	@Override
	public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face){
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand){
		if(getPostMaterial() == EnumPostMaterial.URANIUM){
			if(stateIn.getValue(TYPE) != EnumPostType.ARM && rand.nextFloat() < 0.125F){
				double x = pos.getX() + 0.375 + 0.25 * rand.nextDouble();
				double y = pos.getY() + rand.nextDouble();
				double z = pos.getZ() + 0.375 + 0.25 * rand.nextDouble();
				worldIn.addParticle(URAN_PARTICLE, x, y, z, 0.0, 0.0, 0.0);
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side){
		return false;
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player){
		return getPostMaterial().getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(BlockGetter world, BlockPos pos){
		return world.getBlockState(pos).getValue(TYPE).id() < 2;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit){
		if(!worldIn.isClientSide){
			ItemStack held = playerIn.getMainHandItem();
			if(IPostMaterial.isValidItem(held)){
				if(!held.sameItem(getPostMaterial().getItemStack())){
					playerIn.displayClientMessage(new TranslatableComponent("immersiveposts.expectedlocal", getPostMaterial().getItemStack().getHoverName()), true);
					return InteractionResult.SUCCESS;
				}
				
				for(int y = 0;y <= (worldIn.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - pos.getY());y++){
					BlockPos nPos = pos.offset(0, y, 0);
					
					BlockState nState = worldIn.getBlockState(nPos);
					if(nState.getBlock() instanceof PostBlock){
						EnumPostType type = nState.getValue(PostBlock.TYPE);
						if(!(type == EnumPostType.POST || type == EnumPostType.POST_TOP) && nState.getValue(PostBlock.FLIPSTATE) == EnumFlipState.DOWN){
							return InteractionResult.SUCCESS;
						}else{
							nState = worldIn.getBlockState(nPos.relative(Direction.UP));
							if(nState.getBlock() instanceof PostBlock){
								type = nState.getValue(PostBlock.TYPE);
								if(!(type == EnumPostType.POST || type == EnumPostType.POST_TOP)){
									return InteractionResult.SUCCESS;
								}
							}
						}
					}
					
					if(worldIn.isEmptyBlock(nPos) || worldIn.getBlockState(nPos).getBlock() == Blocks.WATER){
						BlockState fb = IPostMaterial.getPostState(held)
								.setValue(WATERLOGGED, worldIn.getBlockState(nPos).getBlock() == Blocks.WATER);
						
						if(fb != null && !playerIn.blockPosition().equals(nPos) && worldIn.setBlockAndUpdate(nPos, fb)){
							if(!playerIn.isCreative()){
								held.shrink(1);
							}
						}
						return InteractionResult.SUCCESS;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof PostBlock)){
						return InteractionResult.SUCCESS;
					}
				}
			}else if(Utils.isHammer(held)){
				if(playerIn.isShiftKeyDown()){
					switch(state.getValue(TYPE)){
						case POST: case POST_TOP:{
							Direction facing = hit.getDirection();
							if(!Direction.Plane.HORIZONTAL.test(facing)){
								break;
							}
							
							// Arm like Removal
							BlockPos p = pos.relative(facing);
							BlockState hstate = worldIn.getBlockState(p);
							if(hstate.getBlock() instanceof HorizontalTrussBlock){
								replaceSelf(hstate, worldIn, p);
								return InteractionResult.SUCCESS;
							}
							
							// Check first, do stuff later
							boolean success = false;
							int size = 0;
							for(;size <= IPOConfig.MAIN.maxTrussLength.get();size++){
								BlockPos nPos = pos.relative(facing, size + 1);
								BlockState nState = worldIn.getBlockState(nPos);
								
								if(!nState.isAir()){
									if(nState.getBlock() instanceof HorizontalTrussBlock){
										return InteractionResult.FAIL;
									}else if((nState.getBlock() instanceof PostBlock && nState.getValue(TYPE).id() <= 1)){
										if(this.getPostMaterial() != ((PostBlock) nState.getBlock()).getPostMaterial()){
											playerIn.displayClientMessage(new TranslatableComponent("immersiveposts.truss_notsametype"), true);
											return InteractionResult.FAIL;
										}
										
										success = true;
										break;
									}else if(nState.getBlock() != Blocks.WATER){
										break;
									}
								}
							}
							
							// Size check is just a fail-safe
							if(success && size >= 1){
								if(size == 1){
									BlockPos nPos = pos.relative(facing);
									BlockState nState = worldIn.getBlockState(nPos);
									BlockState hState = IPostMaterial.getTrussState(getPostMaterial())
											.setValue(HorizontalTrussBlock.FACING, facing)
											.setValue(WATERLOGGED, nState.getBlock() == Blocks.WATER);
									worldIn.setBlockAndUpdate(nPos, hState.updateShape(null, null, worldIn, nPos, null));
								}else{
									for(int i = 0;i < size;i++){
										BlockPos nPos = pos.relative(facing, i + 1);
										BlockState nState = worldIn.getBlockState(nPos);
										BlockState hState = IPostMaterial.getTrussState(getPostMaterial())
												.setValue(HorizontalTrussBlock.FACING, facing)
												.setValue(WATERLOGGED, nState.getBlock() == Blocks.WATER);
										
										if(i == 0){
											hState = hState.setValue(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_A);
										}else if(i == (size - 1)){
											if(i % 2 == 0){
												hState = hState.setValue(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_D_ODD);
											}else{
												hState = hState.setValue(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_D_EVEN);
											}
											
										}else{
											if(i % 2 == 0){
												hState = hState.setValue(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_C);
											}else{
												hState = hState.setValue(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_B);
											}
										}
										
										worldIn.setBlockAndUpdate(nPos, hState.updateShape(null, null, worldIn, nPos, null));
									}
								}
								
								return InteractionResult.SUCCESS;
							}else if(size == 0){
								playerIn.displayClientMessage(new TranslatableComponent("immersiveposts.truss_minimumdistance"), true);
							}else if(!success && size >= 1){
								playerIn.displayClientMessage(new TranslatableComponent("immersiveposts.truss_postnotfound"), true);
							}
							break;
						}
						default:
							break;
					}
				}else{
					switch(state.getValue(TYPE)){
						case POST: case POST_TOP:{
							Direction facing = hit.getDirection();
							BlockState defaultState = defaultBlockState().setValue(TYPE, EnumPostType.ARM);
							switch(facing){
								case NORTH: case EAST: case SOUTH: case WEST:{
									BlockPos nPos = pos.relative(facing);
									BlockState nState = worldIn.getBlockState(nPos);
									
									if(nState.isAir() || nState.getBlock() == Blocks.WATER){
										defaultState = defaultState.setValue(FACING, facing)
												.setValue(WATERLOGGED, nState.getBlock()==Blocks.WATER);
										
										worldIn.setBlockAndUpdate(nPos, defaultState);
										defaultState.neighborChanged(worldIn, nPos, this, null, false);
									}else if(getBlockFrom(worldIn, nPos) == this){
										switch(nState.getValue(TYPE)){
											case ARM: case EMPTY:{
												replaceSelf(nState, worldIn, nPos);
												return InteractionResult.SUCCESS;
											}
											default:
												break;
										}
									}
								}
								default:
									break;
							}
							return InteractionResult.SUCCESS;
						}
						case ARM:{
							Direction bfacing = state.getValue(FACING);
							BlockPos offset = pos.relative(bfacing);
							if(worldIn.isEmptyBlock(offset) || worldIn.getBlockState(offset).getBlock() == Blocks.WATER){
								worldIn.setBlockAndUpdate(offset, state.setValue(TYPE, EnumPostType.ARM_DOUBLE).setValue(WATERLOGGED, worldIn.getBlockState(offset).getBlock() == Blocks.WATER));
								worldIn.setBlockAndUpdate(pos, state.setValue(TYPE, EnumPostType.EMPTY));
							}
							return InteractionResult.SUCCESS;
						}
						case ARM_DOUBLE:{
							Direction bfacing = state.getValue(FACING);
							replaceSelf(state, worldIn, pos);
							worldIn.setBlockAndUpdate(pos.relative(bfacing.getOpposite()), state.setValue(TYPE, EnumPostType.ARM));
							return InteractionResult.SUCCESS;
						}
						case EMPTY:{
							Direction bfacing = state.getValue(FACING);
							worldIn.setBlockAndUpdate(pos, state.setValue(TYPE, EnumPostType.ARM));
							replaceSelf(state, worldIn, pos.relative(bfacing));
							return InteractionResult.SUCCESS;
						}
					}
				}
			}
		}
		
		if(Utils.isHammer(playerIn.getMainHandItem()) || IPostMaterial.isValidItem(playerIn.getMainHandItem())){
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.FAIL;
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		if(world.isClientSide)
			return;
		
		updateState(state, world, pos);
	}
	
	private void updateState(BlockState stateIn, Level world, BlockPos pos){
		EnumPostType thisType = stateIn.getValue(TYPE);
		
		if(thisType.id() <= 1){ // If POST (0) or POST_TOP (1)
			BlockState state = world.getBlockState(pos.relative(Direction.DOWN));
			if(state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.WATER){
				Block.dropResources(stateIn, world, pos);
				replaceSelf(stateIn, world, pos);
				return;
			}
		}
		
		BlockState aboveState = world.getBlockState(pos.relative(Direction.UP));
		Block aboveBlock = aboveState.getBlock();
		switch(thisType){
			case POST:{
				if(!(aboveBlock instanceof PostBlock)){
					world.setBlockAndUpdate(pos, stateIn.setValue(TYPE, EnumPostType.POST_TOP));
				}
				return;
			}
			case POST_TOP:{
				if((aboveBlock instanceof PostBlock) && aboveState.getValue(TYPE) == EnumPostType.POST_TOP){
					world.setBlockAndUpdate(pos, stateIn.setValue(TYPE, EnumPostType.POST));
				}
				return;
			}
			case ARM:{
				Direction f = stateIn.getValue(FACING).getOpposite();
				BlockState state = world.getBlockState(pos.relative(f));
				
				if(state != null && !(state.getBlock() instanceof PostBlock)){
					replaceSelf(stateIn, world, pos);
				}else{
					world.setBlockAndUpdate(pos, stateIn.setValue(FLIPSTATE, getFlipState(world, pos)));
				}
				
				return;
			}
			case ARM_DOUBLE:{
				Direction f = stateIn.getValue(FACING).getOpposite();
				BlockState state = world.getBlockState(pos.relative(f));
				if(state != null && !(state.getBlock() instanceof PostBlock)){
					replaceSelf(stateIn, world, pos);
				}
				
				return;
			}
			case EMPTY:{
				BlockState state = world.getBlockState(pos.relative(stateIn.getValue(FACING).getOpposite()));
				if(state != null && !(state.getBlock() instanceof PostBlock)){
					replaceSelf(stateIn, world, pos);
					return;
				}
				
				state = world.getBlockState(pos.relative(stateIn.getValue(FACING)));
				if(state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.WATER){
					replaceSelf(stateIn, world, pos);
				}
			}
		}
	}
	
	private EnumFlipState getFlipState(BlockGetter world, BlockPos pos){
		BlockState aboveState = world.getBlockState(pos.relative(Direction.UP));
		BlockState belowState = world.getBlockState(pos.relative(Direction.DOWN));
		
		Block aboveBlock = aboveState.getBlock();
		Block belowBlock = belowState.getBlock();
		
		boolean up = PostBlock.canConnect(world, pos, Direction.UP) && ((aboveBlock instanceof PostBlock) ? aboveState.getValue(TYPE) != EnumPostType.ARM : true);
		boolean down = PostBlock.canConnect(world, pos, Direction.DOWN) && ((belowBlock instanceof PostBlock) ? belowState.getValue(TYPE) != EnumPostType.ARM : true);
		
		if(up && down) return EnumFlipState.BOTH;
		else if(down) return EnumFlipState.DOWN;
		else return EnumFlipState.UP;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
		return stateBounds(state);
	}
	
	private static final VoxelShape X_BOUNDS = Shapes.box(0.0, 0.34375, 0.3125, 1.0, 1.0, 0.6875);
	private static final VoxelShape Z_BOUNDS = Shapes.box(0.3125, 0.34375, 0.0, 0.6875, 1.0, 1.0);
	private static final Byte2ObjectMap<VoxelShape> armMap = new Byte2ObjectArrayMap<>();
	private static final Byte2ObjectMap<VoxelShape> defaultMap = new Byte2ObjectArrayMap<>();
	private static VoxelShape stateBounds(BlockState state){
		EnumPostType type = state.getValue(TYPE);
		switch(type){
			case ARM:
			case ARM_DOUBLE:{
				Direction dir = state.getValue(FACING);
				EnumFlipState flipstate = state.getValue(FLIPSTATE);
				
				/*
					Bit-6 = FlipDown
					Bit-5 = FlipUp
					Bit-4 = West
					Bit-3 = South
					Bit-2 = East
					Bit-1 = North
					
					If Bit5 and Bit6 are both 1 then its EnumFlipState.BOTH
					By default it's EnumFlipState.UP
				 */
				byte bid = 0x00;
				
				switch(flipstate){
					case UP:	bid = 0x10; break;
					case DOWN:	bid = 0x20; break;
					case BOTH:	bid = 0x30; break;
				}
				
				switch(dir){
					case WEST:	bid |= 0x08; break;
					case SOUTH:	bid |= 0x04; break;
					case EAST:	bid |= 0x02; break;
					default:	bid |= 0x01; // Basicly default to North
				}
				
				if(!armMap.containsKey(bid)){
					double minY = 0.0;
					double maxY = 1.0;
					switch(flipstate){
						case UP:	minY = 0.34375; maxY = 1.0; break;
						case DOWN:	minY = 0.0; maxY = 0.65625; break;
						case BOTH:	minY = 0.0; maxY = 1.0; break;
					}
					
					double minX = (dir == Direction.EAST) ? 0.0 : 0.3125;
					double maxX = (dir == Direction.WEST) ? 1.0 : 0.6875;
					double minZ = (dir == Direction.SOUTH) ? 0.0 : 0.3125;
					double maxZ = (dir == Direction.NORTH) ? 1.0 : 0.6875;
					
					VoxelShape shape = Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
					armMap.put(bid, shape);
					return shape;
				}
				
				return armMap.get(bid);
			}
			case EMPTY:{
				if(state.getValue(FACING).getAxis() == Axis.X){
					return X_BOUNDS;
				}
				
				return Z_BOUNDS;
			}
			default:{
				byte bid = 0x00;
				if(state.getValue(LPARM_NORTH))	bid |= 0x01;
				if(state.getValue(LPARM_SOUTH))	bid |= 0x02;
				if(state.getValue(LPARM_EAST))	bid |= 0x04;
				if(state.getValue(LPARM_WEST))	bid |= 0x08;
				
				if(!defaultMap.containsKey(bid)){
					VoxelShape shape = POST_SHAPE;
					
					if(state.getValue(LPARM_NORTH))	shape = Shapes.joinUnoptimized(shape, LPARM_NORTH_BOUNDS, BooleanOp.OR);
					if(state.getValue(LPARM_SOUTH))	shape = Shapes.joinUnoptimized(shape, LPARM_SOUTH_BOUNDS, BooleanOp.OR);
					if(state.getValue(LPARM_EAST))	shape = Shapes.joinUnoptimized(shape, LPARM_EAST_BOUNDS, BooleanOp.OR);
					if(state.getValue(LPARM_WEST))	shape = Shapes.joinUnoptimized(shape, LPARM_WEST_BOUNDS, BooleanOp.OR);
					
					shape = shape.optimize();
					defaultMap.put(bid, shape);
					return shape;
				}
				
				return defaultMap.get(bid);
			}
		}
	}
	
	public static boolean canConnect(BlockGetter worldIn, BlockPos posIn, Direction facingIn){
		BlockPos nPos = posIn.relative(facingIn);
		
		BlockState otherState = worldIn.getBlockState(nPos);
		Block otherBlock = otherState.getBlock();
		
		// Go straight out if air, no questions asked.
		if(otherBlock == Blocks.AIR || otherState.isAir())
			return false;
		
		// Secondary, general checks
		if(otherBlock instanceof CrossCollisionBlock || otherBlock instanceof PostBlock || otherBlock instanceof HorizontalTrussBlock)
			return false;
		
		// Tertiary, block specific checks
		if(IPOTags.IGNORED_BY_POSTARM.contains(otherBlock))
			return false;
		
		VoxelShape shape = otherState.getShape(worldIn, nPos);
		if(!shape.isEmpty()){
			AABB box = shape.bounds();
			
			boolean b;
			switch(facingIn){
				case UP:
					return Mth.equal(0.0D, box.minY);
				case DOWN:{
					boolean bool = otherBlock instanceof PostBlock;
					return !bool && Mth.equal(1.0D, box.maxY);
				}
				case NORTH:b = Mth.equal(1.0D, box.maxZ); break; 
				case SOUTH:b = Mth.equal(0.0D, box.minZ); break;
				case WEST: b = Mth.equal(1.0D, box.maxX); break;
				case EAST: b = Mth.equal(0.0D, box.minX); break;
				default:
					b = false;
			}
			
			if(b){
				if(facingIn == Direction.SOUTH || facingIn == Direction.NORTH){
					AABB arm = LPARM_SOUTH_BOUNDS.bounds();
					if((box.minX > 0.0 && box.maxX < 1.0) || (box.minY > 0.0 && box.maxY < 1.0)){
						if((box.minX <= arm.minX && box.maxX >= arm.maxX) && (box.minY <= arm.minY && box.maxY >= arm.maxY)){
							return true;
						}
					}
					
				}else if(facingIn == Direction.EAST || facingIn == Direction.WEST){
					AABB arm = LPARM_EAST_BOUNDS.bounds();
					if((box.minZ > 0.0 && box.maxZ < 1.0) || (box.minY > 0.0 && box.maxY < 1.0)){
						if((box.minZ <= arm.minZ && box.maxZ >= arm.maxZ) && (box.minY <= arm.minY && box.maxY >= arm.maxY)){
							return true;
						}
					}
					
				}
				
				if(facingIn.getAxis() == Axis.Z && box.minX > 0.0 && box.maxX < 1.0) return true;
				if(facingIn.getAxis() == Axis.X && box.minZ > 0.0 && box.maxZ < 1.0) return true;
			}
		}
		
		return false;
	}
}
