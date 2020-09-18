package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.data.models.LoadedModelBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import twistedgate.immersiveposts.IPOContent;
import twistedgate.immersiveposts.IPOContent.Blocks;
import twistedgate.immersiveposts.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * @author TwistedGate
 */
public class IPOBlockStates extends BlockStateProvider{
	final IPOLoadedModels loadedModels;
	final ExistingFileHelper exFileHelper;
	public IPOBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper, IPOLoadedModels loadedModels){
		super(gen, IPOMod.ID, exFileHelper);
		this.exFileHelper=exFileHelper;
		this.loadedModels=loadedModels;
	}
	
	class Test extends ModelBuilder<Test>{
		protected Test(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper){
			super(outputLocation, existingFileHelper);
		}
	}
	
	@Override
	protected void registerStatesAndModels(){
		ExistingModelFile postBase=new ExistingModelFile(modLoc("block/postbase"), this.exFileHelper);
		MultiPartBlockStateBuilder baseBuilder=getMultipartBuilder(Blocks.post_Base);
		
		baseBuilder.part()
			.modelFile(postBase).addModel()
			.condition(BlockPostBase.HIDDEN, false);
		
		baseBuilder.part()
			.modelFile(new ExistingModelFile(modLoc("block/empty"), this.exFileHelper)).addModel()
			.condition(BlockPostBase.HIDDEN, true);
		
		for(Block block:IPOContent.BLOCKS)
			if(block instanceof BlockPost)
				postStateFor((BlockPost)block);
		
		fence(Fences.iron,		"fence/iron",		mcLoc("block/iron_block"));
		fence(Fences.gold,		"fence/gold",		mcLoc("block/gold_block"));
		fence(Fences.copper,	"fence/copper",		ieLoc("block/metal/storage_copper"));
		fence(Fences.lead,		"fence/lead",		ieLoc("block/metal/storage_lead"));
		fence(Fences.silver,	"fence/silver",		ieLoc("block/metal/storage_silver"));
		fence(Fences.nickel,	"fence/nickel",		ieLoc("block/metal/storage_nickel"));
		fence(Fences.constantan,"fence/constantan",	ieLoc("block/metal/storage_constantan"));
		fence(Fences.electrum,	"fence/electrum",	ieLoc("block/metal/storage_electrum"));
		fence(Fences.uranium,	"fence/uranium",	ieLoc("block/metal/storage_uranium_side"));
		
		loadedModels.backupModels();
	}
	
	private void fence(FenceBlock block, String name, ResourceLocation texture){
		try{
			fenceBlock(block, name, texture);
			
//			String[] s=name.split("/");
//			getBuilder(IPOMod.ID+":block/fences/inventory/"+s[1]+"_fence_inventory")
//				.parent(new ExistingModelFile(new ResourceLocation("block/fence_inventory"), this.exFileHelper))
//				.texture("texture", texture);
		}catch(Throwable e){
			IPODataGen.log.warn("Oops.. {}", e.getMessage());
		}
	}
	
	private void postStateFor(BlockPost block){
		LoadedModelBuilder modelArm			=getPostModel(block, "arm");
		LoadedModelBuilder modelArmTwoWay	=getPostModel(block, "arm_twoway");
		LoadedModelBuilder modelArmDouble	=getPostModel(block, "arm_double");
		LoadedModelBuilder modelPost		=getPostModel(block, "post");
		LoadedModelBuilder modelPostTop		=getPostModel(block, "post_top");
		LoadedModelBuilder modelPostArm		=getPostModel(block, "post_arm");
		ExistingModelFile modelEmpty		=new ExistingModelFile(modLoc("block/empty"), this.exFileHelper);
		
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
		
		builder.part().modelFile(modelPostArm).rotationY(270).addModel()
			.condition(BlockPost.LPARM_WEST, true);
		
		for(EnumFlipState flipstate:EnumFlipState.values()){
			boolean isDown=(flipstate==EnumFlipState.DOWN);
			boolean isUp=(flipstate==EnumFlipState.UP);
			boolean isBoth=(flipstate==EnumFlipState.BOTH);
			
			for(Direction dir:Direction.Plane.HORIZONTAL){
				int yArmRot=horizontalRotation(dir, isDown);
				
				builder.part()
					.modelFile(isBoth?modelArmTwoWay:modelArm).rotationX(flipstate==EnumFlipState.DOWN?180:0).rotationY(yArmRot).addModel()
					.condition(BlockPost.TYPE, EnumPostType.ARM)
					.condition(BlockPost.FACING, dir)
					.condition(BlockPost.FLIPSTATE, flipstate)
					.end();
				
				if(isUp)
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
	
	private int horizontalRotation(Direction dir, boolean xFlipped){
		int value;
		
		if(xFlipped){ // Should be true when X rotation is 180
			switch(dir){
				case WEST:	value=90; break;
				case SOUTH:	value=0; break;
				case EAST:	value=270; break;
				case NORTH:
				default:	value=180; break;
			}
		}else{
			switch(dir){
				case WEST:	value=270; break;
				case SOUTH:	value=180; break;
				case EAST:	value=90; break;
				case NORTH:
				default:	value=0; break;
			}
		}
		
		return value;
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
