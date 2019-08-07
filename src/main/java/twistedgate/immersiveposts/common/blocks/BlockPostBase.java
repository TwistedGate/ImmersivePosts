package twistedgate.immersiveposts.common.blocks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;
import twistedgate.immersiveposts.utils.BlockUtilities;

public class BlockPostBase extends IPOBlockBase{
	private static final AxisAlignedBB BASE_SIZE=new AxisAlignedBB(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	
	public BlockPostBase(){
		super(Material.ROCK, "postbase");
		setResistance(5.0F);
		setHardness(3.0F);
		
		IPOStuff.ITEMS.add(new ItemPostBase(this));
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
		return (face==EnumFacing.UP)?BlockFaceShape.SOLID:BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean hasItem(){
		return true;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return BASE_SIZE;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!worldIn.isRemote){
			ItemStack held=playerIn.getHeldItemMainhand();
			if(EnumPostMaterial.isFenceItem(held)){
				if(!worldIn.isAirBlock(pos.offset(EnumFacing.UP))){
					IBlockState aboveState=worldIn.getBlockState(pos.offset(EnumFacing.UP));
					Block b=aboveState.getBlock();
					
					if(b instanceof BlockPost){
						ItemStack tmp=((BlockPost)b).postMaterial.getItemStack();
						if(!held.isItemEqual(tmp)){
							playerIn.sendStatusMessage(new TextComponentTranslation("immersiveposts.expectedlocal", new TextComponentString(tmp.getDisplayName())), true);
							return true;
						}
					}
				}
				
				for(int y=1;y<(worldIn.getActualHeight()-pos.getY());y++){
					BlockPos nPos=pos.add(0,y,0);
					
					if((BlockUtilities.getBlockFrom(worldIn, nPos) instanceof BlockPost)){
						IBlockState s=worldIn.getBlockState(nPos);
						if(s.getValue(BlockPost.TYPE)==EnumPostType.ARM){
							return true;
						}
					}
					
					if(worldIn.isAirBlock(nPos)){
						IBlockState fb=EnumPostMaterial.getPostStateFrom(held);
						if(fb!=null && !playerIn.getPosition().equals(nPos) && worldIn.setBlockState(nPos, fb)){
							if(!playerIn.capabilities.isCreativeMode){
								held.shrink(1);
							}
						}
						return true;
						
					}else if(!(worldIn.getBlockState(nPos).getBlock() instanceof BlockPost)){
						return true;
					}
				}
			}
		}
		
		return EnumPostMaterial.isFenceItem(playerIn.getHeldItemMainhand());
	}
	
	
	public static class ItemPostBase extends ItemBlock{
		public ItemPostBase(Block block){
			super(block);
			setRegistryName(block.getRegistryName());
		}
		
		@Override
		public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn){
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
				for(EnumPostMaterial t:EnumPostMaterial.values()){
					tooltip.add("- \u00A7a"+t.getItemStack().getDisplayName());
				}
			}else{
				tooltip.add(I18n.format("tooltip.postbase"));
			}
		}
	}
}
