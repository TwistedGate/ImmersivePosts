package twistedgate.immersiveposts.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.Utils;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumHTrussType;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * All-in-one package. Containing everything into one neat class is the best.
 * @author TwistedGate
 */
public class PostBlock extends GenericPostBlock implements IPostBlock, IWaterLoggable{
	public static final RedstoneParticleData URAN_PARTICLE=new RedstoneParticleData(0.0F, 1.0F, 0.0F, 1.0F);
	
	public static final VoxelShape POST_SHAPE=VoxelShapes.create(0.3125, 0.0, 0.3125, 0.6875, 1.0, 0.6875);
	
	public static final VoxelShape LPARM_NORTH_BOUNDS=VoxelShapes.create(0.3125, 0.25, 0.0, 0.6875, 0.75, 0.3125);
	public static final VoxelShape LPARM_SOUTH_BOUNDS=VoxelShapes.create(0.3125, 0.25, 0.6875, 0.6875, 0.75, 1.0);
	public static final VoxelShape LPARM_EAST_BOUNDS=VoxelShapes.create(0.6875, 0.25, 0.3125, 1.0, 0.75, 0.6875);
	public static final VoxelShape LPARM_WEST_BOUNDS=VoxelShapes.create(0.0, 0.25, 0.3125, 0.3125, 0.75, 0.6875);
	
	// LPARM = (Little-)Post Arm
	public static final BooleanProperty LPARM_NORTH=BooleanProperty.create("parm_north");
	public static final BooleanProperty LPARM_EAST=BooleanProperty.create("parm_east");
	public static final BooleanProperty LPARM_SOUTH=BooleanProperty.create("parm_south");
	public static final BooleanProperty LPARM_WEST=BooleanProperty.create("parm_west");
	
	public static final BooleanProperty WATERLOGGED=BlockStateProperties.WATERLOGGED;
	
	public static final DirectionProperty FACING=DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final EnumProperty<EnumPostType> TYPE=EnumProperty.create("type", EnumPostType.class);
	
	public static final EnumProperty<EnumFlipState> FLIPSTATE=EnumProperty.create("flipstate", EnumFlipState.class);
	
	public PostBlock(EnumPostMaterial postMaterial){
		super(postMaterial, "");
		
		setDefaultState(getStateContainer().getBaseState()
				.with(WATERLOGGED, false)
				.with(FACING, Direction.NORTH)
				.with(FLIPSTATE, EnumFlipState.UP)
				.with(TYPE, EnumPostType.POST_TOP)
				.with(LPARM_NORTH, false)
				.with(LPARM_EAST, false)
				.with(LPARM_SOUTH, false)
				.with(LPARM_WEST, false)
				);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder){
		builder.add(
				WATERLOGGED, FACING, FLIPSTATE, TYPE,
				LPARM_NORTH, LPARM_EAST, LPARM_SOUTH, LPARM_WEST
				);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
		return !state.get(WATERLOGGED);
	}
	
	@Override
	public FluidState getFluidState(BlockState state){
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context){
		BlockState state = super.getStateForPlacement(context);
		FluidState fs = context.getWorld().getFluidState(context.getPos());
		
		return state.with(WATERLOGGED, fs.getFluid() == Fluids.WATER);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos){
		if(state.get(WATERLOGGED)){
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		
		if(state.get(TYPE).id() > 1){
			return state;
		}
		
		boolean b0 = canConnect(world, pos, Direction.NORTH);
		boolean b1 = canConnect(world, pos, Direction.EAST);
		boolean b2 = canConnect(world, pos, Direction.SOUTH);
		boolean b3 = canConnect(world, pos, Direction.WEST);
		
		return state.with(LPARM_NORTH, b0)
					.with(LPARM_EAST, b1)
					.with(LPARM_SOUTH, b2)
					.with(LPARM_WEST, b3);
	}
	
	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity){
		return true;
	}
	
	@Override
	public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face){
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand){
		if(this.postMaterial == EnumPostMaterial.URANIUM){
			if(stateIn.get(TYPE) != EnumPostType.ARM && rand.nextFloat() < 0.125F){
				double x = pos.getX() + 0.375 + 0.25 * rand.nextDouble();
				double y = pos.getY() + rand.nextDouble();
				double z = pos.getZ() + 0.375 + 0.25 * rand.nextDouble();
				worldIn.addParticle(URAN_PARTICLE, x, y, z, 0.0, 0.0, 0.0);
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side){
		return false;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		return this.postMaterial.getItemStack();
	}
	
	@Override
	public boolean canConnectTransformer(IBlockReader world, BlockPos pos){
		return world.getBlockState(pos).get(TYPE).id() < 2;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote){
			ItemStack held = playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isValidItem(held)){
				if(!held.isItemEqual(this.postMaterial.getItemStack())){
					playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.expectedlocal", this.postMaterial.getItemStack().getDisplayName()), true);
					return ActionResultType.SUCCESS;
				}
				
				for(int y = 0;y <= (worldIn.getHeight(Type.WORLD_SURFACE, pos.getX(), pos.getZ()) - pos.getY());y++){
					BlockPos nPos = pos.add(0, y, 0);
					
					BlockState nState = worldIn.getBlockState(nPos);
					if(nState.getBlock() instanceof PostBlock){
						EnumPostType type = nState.get(PostBlock.TYPE);
						if(!(type == EnumPostType.POST || type == EnumPostType.POST_TOP) && nState.get(PostBlock.FLIPSTATE) == EnumFlipState.DOWN){
							return ActionResultType.SUCCESS;
						}else{
							nState = worldIn.getBlockState(nPos.offset(Direction.UP));
							if(nState.getBlock() instanceof PostBlock){
								type = nState.get(PostBlock.TYPE);
								if(!(type == EnumPostType.POST || type == EnumPostType.POST_TOP)){
									return ActionResultType.SUCCESS;
								}
							}
						}
					}
					
					if(worldIn.isAirBlock(nPos) || worldIn.getBlockState(nPos).getBlock() == Blocks.WATER){
						BlockState fb=EnumPostMaterial.getPostState(held)
								.with(WATERLOGGED, worldIn.getBlockState(nPos).getBlock() == Blocks.WATER);
						
						if(fb != null && !playerIn.getPosition().equals(nPos) && worldIn.setBlockState(nPos, fb)){
							if(!playerIn.isCreative()){
								held.shrink(1);
							}
						}
						return ActionResultType.SUCCESS;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof PostBlock)){
						return ActionResultType.SUCCESS;
					}
				}
			}else if(Utils.isHammer(held)){
				if(playerIn.isSneaking()){
					switch(state.get(TYPE)){
						case POST:
						case POST_TOP:{
							Direction facing = hit.getFace();
							if(!Direction.Plane.HORIZONTAL.test(facing)){
								break;
							}
							
							// Check first, do stuff later
							boolean success = false;
							int size = 0;
							for(;size <= IPOConfig.MAIN.maxTrussLength.get();size++){
								BlockPos nPos = pos.offset(facing, size + 1);
								BlockState nState = worldIn.getBlockState(nPos);
								
								if(!nState.getBlock().isAir(nState, worldIn, nPos)){
									if(nState.getBlock() instanceof HorizontalTrussBlock){
										return ActionResultType.FAIL;
									}else if((nState.getBlock() instanceof PostBlock && nState.get(TYPE).id() <= 1)){
										if(this.getPostMaterial() != ((PostBlock) nState.getBlock()).getPostMaterial()){
											playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.truss_notsametype"), true);
											return ActionResultType.FAIL;
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
									BlockPos nPos = pos.offset(facing);
									BlockState nState = worldIn.getBlockState(nPos);
									BlockState hState = IPOContent.Blocks.HorizontalTruss.get(getPostMaterial()).getDefaultState()
											.with(HorizontalTrussBlock.FACING, facing)
											.with(WATERLOGGED, nState.getBlock() == Blocks.WATER);
									worldIn.setBlockState(nPos, hState);
								}else{
									for(int i = 0;i < size;i++){
										BlockPos nPos = pos.offset(facing, i + 1);
										BlockState nState = worldIn.getBlockState(nPos);
										BlockState hState = IPOContent.Blocks.HorizontalTruss.get(getPostMaterial()).getDefaultState()
												.with(HorizontalTrussBlock.FACING, facing)
												.with(WATERLOGGED, nState.getBlock() == Blocks.WATER);
										
										if(i == 0){
											hState = hState.with(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_A);
										}else if(i == (size - 1)){
											if(i % 2 == 0){
												hState = hState.with(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_D_ODD);
											}else{
												hState = hState.with(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_D_EVEN);
											}
											
											hState = ((HorizontalTrussBlock) hState.getBlock()).updatePostPlacement(hState, null, null, worldIn, nPos, null);
											
										}else{
											if(i % 2 == 0){
												hState = hState.with(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_C);
											}else{
												hState = hState.with(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_B);
											}
										}
										
										worldIn.setBlockState(nPos, hState);
									}
								}
								
								return ActionResultType.SUCCESS;
							}else if(size == 0){
								playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.truss_minimumdistance"), true);
							}else if(!success && size >= 1){
								playerIn.sendStatusMessage(new TranslationTextComponent("immersiveposts.truss_postnotfound"), true);
							}
							break;
						}
						default:
							break;
					}
				}else{
					switch(state.get(TYPE)){
						case POST:case POST_TOP:{
							Direction facing = hit.getFace();
							BlockState defaultState = getDefaultState().with(TYPE, EnumPostType.ARM);
							switch(facing){
								case NORTH:case EAST:case SOUTH:case WEST:{
									BlockPos nPos = pos.offset(facing);
									BlockState nState = worldIn.getBlockState(nPos);
									
									if(nState.getBlock().isAir(nState, worldIn, nPos) || nState.getBlock() == Blocks.WATER){
										defaultState = defaultState.with(FACING, facing)
												.with(WATERLOGGED, nState.getBlock()==Blocks.WATER);
										
										worldIn.setBlockState(nPos, defaultState);
										defaultState.neighborChanged(worldIn, nPos, this, null, false);
									}else if(getBlockFrom(worldIn, nPos) == this){
										switch(nState.get(TYPE)){
											case ARM: case EMPTY:{
												replaceSelf(nState, worldIn, nPos);
												return ActionResultType.SUCCESS;
											}
											default:break;
										}
									}
								}
								default:break;
							}
							return ActionResultType.SUCCESS;
						}
						case ARM:{
							Direction bfacing = state.get(FACING);
							BlockPos offset = pos.offset(bfacing);
							if(worldIn.isAirBlock(offset) || worldIn.getBlockState(offset).getBlock() == Blocks.WATER){
								worldIn.setBlockState(offset, state.with(TYPE, EnumPostType.ARM_DOUBLE).with(WATERLOGGED, worldIn.getBlockState(offset).getBlock() == Blocks.WATER));
								worldIn.setBlockState(pos, state.with(TYPE, EnumPostType.EMPTY));
							}
							return ActionResultType.SUCCESS;
						}
						case ARM_DOUBLE:{
							Direction bfacing = state.get(FACING);
							replaceSelf(state, worldIn, pos);
							worldIn.setBlockState(pos.offset(bfacing.getOpposite()), state.with(TYPE, EnumPostType.ARM));
							return ActionResultType.SUCCESS;
						}
						case EMPTY:{
							Direction bfacing = state.get(FACING);
							worldIn.setBlockState(pos, state.with(TYPE, EnumPostType.ARM));
							replaceSelf(state, worldIn, pos.offset(bfacing));
							return ActionResultType.SUCCESS;
						}
					}
				}
			}
		}
		
		if(Utils.isHammer(playerIn.getHeldItemMainhand()) || EnumPostMaterial.isValidItem(playerIn.getHeldItemMainhand())){
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.FAIL;
	}
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		if(world.isRemote)
			return;
		
		updateState(state, world, pos);
	}
	
	private void updateState(BlockState stateIn, World world, BlockPos pos){
		EnumPostType thisType = stateIn.get(TYPE);
		
		if(thisType.id() <= 1){ // If POST (0) or POST_TOP (1)
			BlockState state = world.getBlockState(pos.offset(Direction.DOWN));
			if(state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.WATER){
				Block.spawnDrops(stateIn, world, pos);
				replaceSelf(stateIn, world, pos);
				return;
			}
		}
		
		BlockState aboveState = world.getBlockState(pos.offset(Direction.UP));
		Block aboveBlock = aboveState.getBlock();
		switch(thisType){
			case POST:{
				if(!(aboveBlock instanceof PostBlock)){
					world.setBlockState(pos, stateIn.with(TYPE, EnumPostType.POST_TOP));
				}
				return;
			}
			case POST_TOP:{
				if((aboveBlock instanceof PostBlock) && aboveState.get(TYPE) == EnumPostType.POST_TOP){
					world.setBlockState(pos, stateIn.with(TYPE, EnumPostType.POST));
				}
				return;
			}
			case ARM:{
				Direction f = stateIn.get(FACING).getOpposite();
				BlockState state = world.getBlockState(pos.offset(f));
				
				if(state != null && !(state.getBlock() instanceof PostBlock)){
					replaceSelf(stateIn, world, pos);
				}else{
					world.setBlockState(pos, stateIn.with(FLIPSTATE, getFlipState(world, pos)));
				}
				
				return;
			}
			case ARM_DOUBLE:{
				Direction f = stateIn.get(FACING).getOpposite();
				BlockState state = world.getBlockState(pos.offset(f));
				if(state != null && !(state.getBlock() instanceof PostBlock)){
					replaceSelf(stateIn, world, pos);
				}
				
				return;
			}
			case EMPTY:{
				BlockState state = world.getBlockState(pos.offset(stateIn.get(FACING).getOpposite()));
				if(state != null && !(state.getBlock() instanceof PostBlock)){
					replaceSelf(stateIn, world, pos);
					return;
				}
				
				state = world.getBlockState(pos.offset(stateIn.get(FACING)));
				if(state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.WATER){
					replaceSelf(stateIn, world, pos);
				}
			}
		}
	}
	
	private EnumFlipState getFlipState(IBlockReader world, BlockPos pos){
		BlockState aboveState = world.getBlockState(pos.offset(Direction.UP));
		BlockState belowState = world.getBlockState(pos.offset(Direction.DOWN));
		
		Block aboveBlock = aboveState.getBlock();
		Block belowBlock = belowState.getBlock();
		
		boolean up = PostBlock.canConnect(world, pos, Direction.UP) && ((aboveBlock instanceof PostBlock) ? aboveState.get(TYPE) != EnumPostType.ARM : true);
		boolean down = PostBlock.canConnect(world, pos, Direction.DOWN) && ((belowBlock instanceof PostBlock) ? belowState.get(TYPE) != EnumPostType.ARM : true);
		
		EnumFlipState flipState;
		if(up && down)
			flipState = EnumFlipState.BOTH;
		else if(down)
			flipState = EnumFlipState.DOWN;
		else
			flipState = EnumFlipState.UP;
		
		return flipState;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return stateBounds(state);
	}
	
	private static final VoxelShape X_BOUNDS = VoxelShapes.create(0.0, 0.34375, 0.3125, 1.0, 1.0, 0.6875);
	private static final VoxelShape Z_BOUNDS = VoxelShapes.create(0.3125, 0.34375, 0.0, 0.6875, 1.0, 1.0);
	private static final Byte2ObjectMap<VoxelShape> armMap = new Byte2ObjectArrayMap<>();
	private static final Byte2ObjectMap<VoxelShape> defaultMap = new Byte2ObjectArrayMap<>();
	private static VoxelShape stateBounds(BlockState state){
		EnumPostType type = state.get(TYPE);
		switch(type){
			case ARM:
			case ARM_DOUBLE:{
				Direction dir = state.get(FACING);
				EnumFlipState flipstate = state.get(FLIPSTATE);
				
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
					
					VoxelShape shape = VoxelShapes.create(minX, minY, minZ, maxX, maxY, maxZ);
					armMap.put(bid, shape);
					return shape;
				}
				
				return armMap.get(bid);
			}
			case EMPTY:{
				if(state.get(FACING).getAxis() == Axis.X){
					return X_BOUNDS;
				}
				
				return Z_BOUNDS;
			}
			default:{
				byte bid = 0x00;
				if(state.get(LPARM_NORTH))	bid |= 0x01;
				if(state.get(LPARM_SOUTH))	bid |= 0x02;
				if(state.get(LPARM_EAST))	bid |= 0x04;
				if(state.get(LPARM_WEST))	bid |= 0x08;
				
				if(!defaultMap.containsKey(bid)){
					VoxelShape shape = POST_SHAPE;
					
					if(state.get(LPARM_NORTH))	shape = VoxelShapes.combine(shape, LPARM_NORTH_BOUNDS, IBooleanFunction.OR);
					if(state.get(LPARM_SOUTH))	shape = VoxelShapes.combine(shape, LPARM_SOUTH_BOUNDS, IBooleanFunction.OR);
					if(state.get(LPARM_EAST))	shape = VoxelShapes.combine(shape, LPARM_EAST_BOUNDS, IBooleanFunction.OR);
					if(state.get(LPARM_WEST))	shape = VoxelShapes.combine(shape, LPARM_WEST_BOUNDS, IBooleanFunction.OR);
					
					shape = shape.simplify();
					defaultMap.put(bid, shape);
					return shape;
				}
				
				return defaultMap.get(bid);
			}
		}
	}
	
	
	static final RegistryObject<Block> IE_POST_TRANSFORMER = RegistryObject.of(new ResourceLocation(Lib.MODID, "post_transformer"), ForgeRegistries.BLOCKS);
	static final RegistryObject<Block> IE_TRANSFORMER = RegistryObject.of(new ResourceLocation(Lib.MODID, "transformer"), ForgeRegistries.BLOCKS);
	static final RegistryObject<Block> IE_TRANSFORMER_HV = RegistryObject.of(new ResourceLocation(Lib.MODID, "transformer_hv"), ForgeRegistries.BLOCKS);
	
	public static boolean canConnect(IBlockReader worldIn, BlockPos posIn, Direction facingIn){
		BlockPos nPos = posIn.offset(facingIn);
		
		BlockState otherState = worldIn.getBlockState(nPos);
		Block otherBlock = otherState.getBlock();
		
		// Go straight out if air, no questions asked.
		if(otherBlock == Blocks.AIR || otherBlock.isAir(otherState, worldIn, nPos))
			return false;
		
		// Secondary, general checks
		if(otherBlock instanceof FourWayBlock || otherBlock instanceof PostBlock || otherBlock instanceof HorizontalTrussBlock)
			return false;
		
		// Tertiary, block specific checks
		if(otherBlock == IE_POST_TRANSFORMER.get() || otherBlock == IE_TRANSFORMER.get() || otherBlock == IE_TRANSFORMER_HV.get())
			return false;
		
		if(facingIn == Direction.DOWN || facingIn == Direction.UP){
			VoxelShape shape = otherState.getShape(worldIn, nPos);
			if(!shape.isEmpty()){
				AxisAlignedBB box = shape.getBoundingBox();
				switch(facingIn){
					case UP:
						return box.minY == 0.0;
					case DOWN:{
						boolean bool = otherBlock instanceof PostBlock;
						return !bool && box.maxY == 1.0;
					}
					default:
						break;
				}
				return false;
			}
		}
		
		VoxelShape shape = otherState.getShape(worldIn, nPos);
		if(!shape.isEmpty()){
			AxisAlignedBB box = shape.getBoundingBox();
			
			if(facingIn == Direction.SOUTH || facingIn == Direction.NORTH){
				AxisAlignedBB arm = LPARM_SOUTH_BOUNDS.getBoundingBox();
				if((box.minX > 0.0 && box.maxX < 1.0) || (box.minY > 0.0 && box.maxY < 1.0)){
					if((box.minX <= arm.minX && box.maxX >= arm.maxX) && (box.minY <= arm.minY && box.maxY >= arm.maxY)){
						return true;
					}
				}
				
			}else if(facingIn == Direction.EAST || facingIn == Direction.WEST){
				AxisAlignedBB arm = LPARM_EAST_BOUNDS.getBoundingBox();
				if((box.minZ > 0.0 && box.maxZ < 1.0) || (box.minY > 0.0 && box.maxY < 1.0)){
					if((box.minZ <= arm.minZ && box.maxZ >= arm.maxZ) && (box.minY <= arm.minY && box.maxY >= arm.maxY)){
						return true;
					}
				}
				
			}
			
			boolean b;
			switch(facingIn){
				case NORTH:b = MathHelper.epsilonEquals(1.0D, box.maxZ); break; 
				case SOUTH:b = MathHelper.epsilonEquals(0.0D, box.minZ); break;
				case WEST: b = MathHelper.epsilonEquals(1.0D, box.maxX); break;
				case EAST: b = MathHelper.epsilonEquals(0.0D, box.minX); break;
				default:
					b = false;
			}
			
			if(b){
				if(facingIn.getAxis() == Axis.Z && box.minX > 0.0 && box.maxX < 1.0) return true;
				if(facingIn.getAxis() == Axis.X && box.minZ > 0.0 && box.maxZ < 1.0) return true;
			}
		}
		
		return false;
	}
}
