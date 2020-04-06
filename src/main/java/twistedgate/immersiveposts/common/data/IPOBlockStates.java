package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.data.models.LoadedModelBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * @author TwistedGate
 */
public class IPOBlockStates extends BlockStateProvider{
	final IPOLoadedModels loadedModels;
	public IPOBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper, IPOLoadedModels loadedModels){
		super(gen, IPOMod.ID, exFileHelper);
		this.loadedModels=loadedModels;
	}
	
	class Test extends ModelBuilder<Test>{
		protected Test(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper){
			super(outputLocation, existingFileHelper);
		}
	}
	
	@Override
	protected void registerStatesAndModels(){
		ExistingModelFile postBase=new ExistingModelFile(modLoc("block/postbase"), existingFileHelper);
		getVariantBuilder(IPOStuff.post_Base).partialState()
			.setModels(new ConfiguredModel(postBase));
		getBuilder(IPOMod.ID+":item/postbase").parent(postBase); // Might aswell generate item model for it too
		
		for(Block block:IPOStuff.BLOCKS)
			if(block instanceof BlockPost)
				alternateGenerator((BlockPost)block);//generateStatesFor((BlockPost)block);
		
		fence(IPOStuff.fence_Iron,		"fences/iron",		mcLoc("block/iron_block"));
		fence(IPOStuff.fence_Gold,		"fences/gold",		mcLoc("block/gold_block"));
		fence(IPOStuff.fence_Copper,	"fences/copper",	ieLoc("block/metal/storage_copper"));
		fence(IPOStuff.fence_Lead,		"fences/lead",		ieLoc("block/metal/storage_lead"));
		fence(IPOStuff.fence_Silver,	"fences/silver",	ieLoc("block/metal/storage_silver"));
		fence(IPOStuff.fence_Nickel,	"fences/nickel",	ieLoc("block/metal/storage_nickel"));
		fence(IPOStuff.fence_Constantan,"fences/constantan",ieLoc("block/metal/storage_constantan"));
		fence(IPOStuff.fence_Electrum,	"fences/electrum",	ieLoc("block/metal/storage_electrum"));
		fence(IPOStuff.fence_Uranium,	"fences/uranium",	ieLoc("block/metal/storage_uranium_side"));
		
		loadedModels.backupModels();
	}
	
	private void fence(FenceBlock block, String name, ResourceLocation texture){
		try{
			fenceBlock(block, name, texture);
			
			// While it's at it also make the inventory model ready
			String[] s=name.split("/");
			getBuilder(IPOMod.ID+":block/fences/inventory/"+s[1]+"_fence_inventory")
				.parent(getExistingFile(new ResourceLocation("block/fence_inventory")))
				.texture("texture", texture);
		}catch(Throwable e){
			IPODataGen.log.warn("Oops.. {}", e.getMessage());
		}
	}
	
	private void alternateGenerator(BlockPost block){
		LoadedModelBuilder modelArm			=getPostModel(block, "arm");
		LoadedModelBuilder modelArmDouble	=getPostModel(block, "arm_double");
		LoadedModelBuilder modelPost		=getPostModel(block, "post");
		LoadedModelBuilder modelPostTop		=getPostModel(block, "post_top");
		LoadedModelBuilder modelPostArm		=getPostModel(block, "post_arm");
		ExistingModelFile modelEmpty		=getExistingFile(modLoc("block/empty"));
		
		MultiPartBlockStateBuilder builder=getMultipartBuilder(block);
		
		builder.part()
			.modelFile(modelPost).addModel()
			.condition(BlockPost.TYPE, EnumPostType.POST)
			.end();
		
		builder.part()
			.modelFile(modelPostTop).addModel()
			.condition(BlockPost.TYPE, EnumPostType.POST_TOP)
			.end();
		
		builder.part().modelFile(modelPostArm).rotationY(0).addModel()
			.condition(BlockPost.LPARM_NORTH, true);
		
		builder.part().modelFile(modelPostArm).rotationY(90).addModel()
			.condition(BlockPost.LPARM_EAST, true);
		
		builder.part().modelFile(modelPostArm).rotationY(180).addModel()
			.condition(BlockPost.LPARM_SOUTH, true);
		
		builder.part().modelFile(modelPostArm).rotationY(-90).addModel()
			.condition(BlockPost.LPARM_WEST, true);
		
		for(int i=0;i<=1;i++){
			boolean flip=(i==1);
			
			for(Direction dir:Direction.Plane.HORIZONTAL){
				int yArmRot;
				if(flip){
					switch(dir){
						case WEST:	yArmRot=90; break;
						case SOUTH:	yArmRot=0; break;
						case EAST:	yArmRot=-90; break;
						case NORTH:
						default:	yArmRot=180; break;
					}
				}else{
					switch(dir){
						case WEST:	yArmRot=-90; break;
						case SOUTH:	yArmRot=180; break;
						case EAST:	yArmRot=90; break;
						case NORTH:
						default:	yArmRot=0; break;
					}
				}
				
				builder.part()
					.modelFile(modelArm).rotationX(flip?180:0).rotationY(yArmRot).addModel()
					.condition(BlockPost.TYPE, EnumPostType.ARM)
					.condition(BlockPost.FACING, dir)
					.condition(BlockPost.FLIP, flip)
					.end();
				
				if(i==0)
					builder.part()
						.modelFile(modelArmDouble).rotationY(yArmRot).addModel()
						.condition(BlockPost.TYPE, EnumPostType.ARM_DOUBLE)
						.condition(BlockPost.FACING, dir)
						.end();
			}
		}
		
		builder.part()
			.modelFile(modelEmpty).addModel()
			.condition(BlockPost.TYPE, EnumPostType.EMPTY)
			.end();
	}
	
	// This is a hybrid of using IE's Builder and Forge's Loader stuff
	protected static final ResourceLocation FORGE_LOADER=new ResourceLocation("forge","obj");
	private LoadedModelBuilder getPostModel(BlockPost block, String name){
		ResourceLocation texture=modLoc("block/posts/post_"+block.getPostMaterial().name().toLowerCase());
		
		LoadedModelBuilder b=this.loadedModels.withExistingParent(postModelPath(block, name), mcLoc("block"))
			.loader(FORGE_LOADER)
			.additional("model", modLoc("models/block/post/obj/"+name+".obj"))
			.texture("texture", texture)
			.texture("particle", texture)
			;
		
		return b;
	}
	
	private String postModelPath(BlockPost block, String name){
		return block.getRegistryName().getPath()+"/"+name;
	}
	
	private ResourceLocation ieLoc(String str){
		return new ResourceLocation("immersiveengineering", str);
	}
}
