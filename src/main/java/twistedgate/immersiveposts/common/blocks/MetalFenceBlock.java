package twistedgate.immersiveposts.common.blocks;

import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.items.IPOBlockItem;

/**
 * @author TwistedGate
 */
public class MetalFenceBlock extends FenceBlock{
	static final BlockBehaviour.Properties DEFAULT_PROP = BlockBehaviour.Properties.of(Material.METAL).strength(3.0F, 15.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().isViewBlocking((s, r, p) -> false);
	
	public MetalFenceBlock(){
		super(DEFAULT_PROP);
	}
	
	public static class FenceItemBlock extends IPOBlockItem{
		public FenceItemBlock(Block block){
			super(block, new Item.Properties());
		}
		
		public void fillCreativeTab(Output out){
			if(IPOConfig.MAIN.isEnabled(this)){
				out.accept(this);
			}
		}
	}
}
