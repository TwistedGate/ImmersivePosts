package twistedgate.immersiveposts.common.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.enums.EnumPostType;

public class IPOBlockStates extends BlockStateProvider{
	private final IPOModelLoader loadedModels;
	public IPOBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper, IPOModelLoader loadedModels){
		super(gen, IPOMod.ID, exFileHelper);
		this.loadedModels=loadedModels;
	}
	
	@Override
	protected void registerStatesAndModels(){
		ResourceLocation model=new ResourceLocation(IPOMod.ID, "block/postbase.json");
		
		getVariantBuilder(IPOStuff.postBase)
			.partialState()
			.setModels(new ConfiguredModel(new ExistingModelFile(model, existingFileHelper)));
		
		post(IPOStuff.woodPost);
	}
	
	// TODO Not sure if this would even work as i expect. So this is more or less experimental.
	private void post(Block block){
		ResourceLocation arm=postModel("arm.obj");
		ResourceLocation armDouble=postModel("arm_double.obj");
		ResourceLocation post=postModel("post.obj");
		ResourceLocation postTop=postModel("post_top.obj");
		ResourceLocation postArm=postModel("post_arm.obj");
		
		getVariantBuilder(block).partialState()
			.with(BlockPost.FACING, Direction.NORTH)
			.with(BlockPost.TYPE, EnumPostType.POST)
			.with(BlockPost.FLIP, false)
			.with(BlockPost.LPARM_NORTH, false)
			.with(BlockPost.LPARM_EAST, false)
			.with(BlockPost.LPARM_SOUTH, false)
			.with(BlockPost.LPARM_WEST, false)
			.setModels(new ConfiguredModel(new ExistingModelFile(post, existingFileHelper)));
		
		getVariantBuilder(block).partialState()
			.with(BlockPost.FACING, Direction.NORTH)
			.with(BlockPost.TYPE, EnumPostType.POST_TOP)
			.with(BlockPost.FLIP, false)
			.with(BlockPost.LPARM_NORTH, false)
			.with(BlockPost.LPARM_EAST, false)
			.with(BlockPost.LPARM_SOUTH, false)
			.with(BlockPost.LPARM_WEST, false)
			.setModels(new ConfiguredModel(new ExistingModelFile(postTop, existingFileHelper)));
	}
	
	private ResourceLocation postModel(String name){
		return new ResourceLocation(IPOMod.ID, "block/post/obj/"+name);
	}
}
