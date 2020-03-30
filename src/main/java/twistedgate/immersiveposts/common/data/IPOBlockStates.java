package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.data.models.LoadedModelBuilder;
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
	final IPOLoadedModels loadedModels;
	public IPOBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper, IPOLoadedModels loadedModels){
		super(gen, IPOMod.ID, exFileHelper);
		this.loadedModels=loadedModels;
	}
	
	@Override
	protected void registerStatesAndModels(){
		ResourceLocation model=modLoc("block/postbase");
		
		getVariantBuilder(IPOStuff.postBase)
			.partialState()
			.setModels(new ConfiguredModel(new ExistingModelFile(model, existingFileHelper)));
		
		post(IPOStuff.woodPost);
	}
	
	private LoadedModelBuilder getPostModel(BlockPost block, String name){
		ResourceLocation source=new ResourceLocation(IPOMod.ID, "block/post/obj/"+name);
		
		LoadedModelBuilder model=loadedModels.withExistingParent(block.getRegistryName().getPath(), mcLoc("block"))
				.loader(new ResourceLocation("immersiveengineering","ie_obj"))
				.additional("model", new ResourceLocation(source.getNamespace(), "models/"+source.getPath()))
				//.texture("texture", block.getPostMaterial().getTexture())
				;
		return model;
	}
	
	// TODO Not sure if this would even work as i expect. So this is more or less experimental.
	private void post(BlockPost block){
		LoadedModelBuilder modelArm			=getPostModel(block, "arm.obj");
		LoadedModelBuilder modelArmDouble	=getPostModel(block, "arm_double.obj");
		LoadedModelBuilder modelPost		=getPostModel(block, "post.obj");
		LoadedModelBuilder modelPostTop		=getPostModel(block, "post_top.obj");
		LoadedModelBuilder modelPostArm		=getPostModel(block, "post_arm.obj");
		
		
		getVariantBuilder(block).partialState()
			.with(BlockPost.FACING, Direction.NORTH)
			.with(BlockPost.TYPE, EnumPostType.POST)
			.with(BlockPost.FLIP, false)
			.with(BlockPost.LPARM_NORTH, false)
			.with(BlockPost.LPARM_EAST, false)
			.with(BlockPost.LPARM_SOUTH, false)
			.with(BlockPost.LPARM_WEST, false)
			.setModels(new ConfiguredModel(modelPost));
		
		getVariantBuilder(block).partialState()
			.with(BlockPost.FACING, Direction.NORTH)
			.with(BlockPost.TYPE, EnumPostType.POST_TOP)
			.with(BlockPost.FLIP, false)
			.with(BlockPost.LPARM_NORTH, false)
			.with(BlockPost.LPARM_EAST, false)
			.with(BlockPost.LPARM_SOUTH, false)
			.with(BlockPost.LPARM_WEST, false)
			.setModels(new ConfiguredModel(modelPostTop));
		
		for(Direction dir:Direction.Plane.HORIZONTAL){
			getVariantBuilder(block).partialState()
				.with(BlockPost.FACING, dir)
				.with(BlockPost.TYPE, EnumPostType.ARM)
				.with(BlockPost.FLIP, false)
				.with(BlockPost.LPARM_NORTH, false)
				.with(BlockPost.LPARM_EAST, false)
				.with(BlockPost.LPARM_SOUTH, false)
				.with(BlockPost.LPARM_WEST, false)
				.setModels(new ConfiguredModel(modelArm));
			
			getVariantBuilder(block).partialState()
				.with(BlockPost.FACING, dir)
				.with(BlockPost.TYPE, EnumPostType.ARM_DOUBLE)
				.with(BlockPost.FLIP, false)
				.with(BlockPost.LPARM_NORTH, false)
				.with(BlockPost.LPARM_EAST, false)
				.with(BlockPost.LPARM_SOUTH, false)
				.with(BlockPost.LPARM_WEST, false)
				.setModels(new ConfiguredModel(modelArmDouble));
			
			getVariantBuilder(block).partialState()
				.with(BlockPost.FACING, dir)
				.with(BlockPost.TYPE, EnumPostType.EMPTY)
				.with(BlockPost.FLIP, false)
				.with(BlockPost.LPARM_NORTH, false)
				.with(BlockPost.LPARM_EAST, false)
				.with(BlockPost.LPARM_SOUTH, false)
				.with(BlockPost.LPARM_WEST, false)
				.setModels(new ConfiguredModel(new ExistingModelFile(modLoc("block/empty"), existingFileHelper)));
			
			getVariantBuilder(block).partialState()
				.with(BlockPost.FACING, dir)
				.with(BlockPost.TYPE, EnumPostType.ARM)
				.with(BlockPost.FLIP, true)
				.with(BlockPost.LPARM_NORTH, false)
				.with(BlockPost.LPARM_EAST, false)
				.with(BlockPost.LPARM_SOUTH, false)
				.with(BlockPost.LPARM_WEST, false)
				.setModels(new ConfiguredModel(modelPostArm));
		}
	}
}
