package twistedgate.immersiveposts.common.blocks.state;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockWoodenDecoration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.enums.EnumPostType;
import twistedgate.immersiveposts.utils.BlockUtilities;

public class PostState extends BlockStateContainer.StateImplementation{
	public PostState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties){
		super(block, properties);
	}
	public PostState(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> unlistedProperties){
		super(blockIn, propertiesIn, unlistedProperties);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing face){
		IBlockState state=worldIn.getBlockState(pos.offset(face));
		Block block=state.getBlock();
		if(block instanceof BlockFence || block instanceof BlockWall) return BlockFaceShape.UNDEFINED;
		
		boolean f=false;
		if(block instanceof BlockWoodenDecoration)
			if(block.getMetaFromState(state)==BlockTypes_WoodenDecoration.FENCE.getMeta())
				f=true;
		
		if(block instanceof BlockMetalDecoration1){
			int tmp=block.getMetaFromState(state);
			if(tmp==BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta() || tmp==BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta())
				f=true;
		}
		
		
		return f?BlockFaceShape.UNDEFINED:BlockFaceShape.SOLID;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
	}
	
	@Override
	public boolean isNormalCube(){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean isFullCube(){
		return false;
	}
	
	@Override
	public void neighborChanged(World world, BlockPos pos, Block block, BlockPos fromPos){
		EnumPostType thisType=this.getValue(BlockPost.TYPE);
		
		if(thisType!=EnumPostType.ARM){
			BlockPos d=pos.offset(EnumFacing.DOWN);
			if(BlockUtilities.getBlockFrom(world, d)==Blocks.AIR){
				block.dropBlockAsItem(world, pos, this, 0);
				world.setBlockToAir(pos);
				return;
			}
		}
		
		IBlockState aboveState=world.getBlockState(pos.offset(EnumFacing.UP));
		Block aboveBlock=aboveState.getBlock();
		switch(thisType){
			case POST:{
				if(!(aboveBlock instanceof BlockPost))
					world.setBlockState(pos, this.withProperty(BlockPost.TYPE, EnumPostType.POST_TOP));
				return;
			}
			case POST_TOP:{
				if((aboveBlock instanceof BlockPost) && aboveState.getValue(BlockPost.TYPE)!=EnumPostType.ARM)
					world.setBlockState(pos, this.withProperty(BlockPost.TYPE, EnumPostType.POST));
				return;
			}
			case ARM:{
				EnumFacing f=this.getValue(BlockPost.DIRECTION).getOpposite();
				if((world.getBlockState(pos.offset(f))!=null) && !(world.getBlockState(pos.offset(f)).getBlock() instanceof BlockPost)){
					world.setBlockToAir(pos);
					return;
				}
				
				if(BlockPost.canConnect(world, pos, EnumFacing.UP)){
					world.setBlockState(pos, this.withProperty(BlockPost.FLIP, false), 3);
				}
				
				boolean bool=BlockPost.canConnect(world, pos, EnumFacing.DOWN);
				world.setBlockState(pos, this.withProperty(BlockPost.FLIP, bool), 3);
				
				return;
			}
		}
	}
}
