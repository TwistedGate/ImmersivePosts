package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.data.models.SpecialModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.loaders.ObjModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseLoader;
import twistedgate.immersiveposts.common.IPOContent;
import twistedgate.immersiveposts.common.IPOContent.Blocks;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.blocks.GenericPostBlock;
import twistedgate.immersiveposts.common.blocks.HorizontalTrussBlock;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.enums.EnumFlipState;
import twistedgate.immersiveposts.enums.EnumHTrussType;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.enums.EnumPostType;

/**
 * @author TwistedGate
 */
public class IPOBlockStates extends BlockStateProvider{
	final ExistingFileHelper exFileHelper;
	public IPOBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, IPOMod.ID, exFileHelper);
		this.exFileHelper = exFileHelper;
	}
	
	@Override
	protected void registerStatesAndModels(){
		// POST BASE
		ExistingModelFile postBase = new ExistingModelFile(modLoc("block/postbase"), this.exFileHelper);
		
		ModelFile modelFile = this.models().getBuilder("postbase_covered")
				.customLoader(SpecialModelBuilder.forLoader(PostBaseLoader.LOCATION)).end();
		
		VariantBlockStateBuilder variantBuilder = getVariantBuilder(Blocks.POST_BASE.get());
		
		variantBuilder.partialState()
			.with(PostBaseBlock.HIDDEN, false)
			.setModels(new ConfiguredModel(postBase));
		
		variantBuilder.partialState()
			.with(PostBaseBlock.HIDDEN, true)
			.setModels(new ConfiguredModel(modelFile));
		
		// POSTS
		for(EnumPostMaterial material:EnumPostMaterial.values()){
			postStateFor(IPOContent.Blocks.Posts.getRegObject(material));
			horizontalPostStateFor(IPOContent.Blocks.HorizontalTruss.getRegObject(material));
		}
		
		// FENCES
		fenceBlock(Fences.IRON.get(),		"fence/iron",		mcLoc("block/iron_block"));
		fenceBlock(Fences.GOLD.get(),		"fence/gold",		mcLoc("block/gold_block"));
		fenceBlock(Fences.COPPER.get(),		"fence/copper",		ieLoc("block/metal/storage_copper"));
		fenceBlock(Fences.LEAD.get(),		"fence/lead",		ieLoc("block/metal/storage_lead"));
		fenceBlock(Fences.SILVER.get(),		"fence/silver",		ieLoc("block/metal/storage_silver"));
		fenceBlock(Fences.NICKEL.get(),		"fence/nickel",		ieLoc("block/metal/storage_nickel"));
		fenceBlock(Fences.CONSTANTAN.get(),	"fence/constantan",	ieLoc("block/metal/storage_constantan"));
		fenceBlock(Fences.ELECTRUM.get(),	"fence/electrum",	ieLoc("block/metal/storage_electrum"));
		fenceBlock(Fences.URANIUM.get(),	"fence/uranium",	ieLoc("block/metal/storage_uranium_side"));
	}
	
	private void postStateFor(RegistryObject<PostBlock> block){
		BlockModelBuilder modelArm			= getPostModel(block, "arm");
		BlockModelBuilder modelArmTwoWay	= getPostModel(block, "arm_twoway");
		BlockModelBuilder modelArmDouble	= getPostModel(block, "arm_double");
		BlockModelBuilder modelPost			= getPostModel(block, "post");
		BlockModelBuilder modelPostTop		= getPostModel(block, "post_top");
		BlockModelBuilder modelPostArm		= getPostModel(block, "post_arm");
		ExistingModelFile modelEmpty		= new ExistingModelFile(modLoc("block/empty"), this.exFileHelper);
		
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
		
		builder.part()
			.modelFile(modelPost).addModel()
			.condition(PostBlock.TYPE, EnumPostType.POST);
		
		builder.part()
			.modelFile(modelPostTop).addModel()
			.condition(PostBlock.TYPE, EnumPostType.POST_TOP);
		
		builder.part().modelFile(modelPostArm).rotationY(0).addModel()
			.condition(PostBlock.LPARM_NORTH, true);
		
		builder.part().modelFile(modelPostArm).rotationY(90).addModel()
			.condition(PostBlock.LPARM_EAST, true);
		
		builder.part().modelFile(modelPostArm).rotationY(180).addModel()
			.condition(PostBlock.LPARM_SOUTH, true);
		
		builder.part().modelFile(modelPostArm).rotationY(270).addModel()
			.condition(PostBlock.LPARM_WEST, true);
		
		for(EnumFlipState flipstate:EnumFlipState.values()){
			boolean isDown = (flipstate == EnumFlipState.DOWN);
			boolean isUp = (flipstate == EnumFlipState.UP);
			boolean isBoth = (flipstate == EnumFlipState.BOTH);
			
			for(Direction dir:Direction.Plane.HORIZONTAL){
				int yArmRot = horizontalRotation(dir, isDown);
				
				builder.part()
					.modelFile(isBoth ? modelArmTwoWay : modelArm).rotationX(flipstate == EnumFlipState.DOWN ? 180 : 0).rotationY(yArmRot).addModel()
					.condition(PostBlock.TYPE, EnumPostType.ARM)
					.condition(PostBlock.FACING, dir)
					.condition(PostBlock.FLIPSTATE, flipstate);
				
				if(isUp){
					builder.part()
						.modelFile(modelArmDouble).rotationY(yArmRot).addModel()
						.condition(PostBlock.TYPE, EnumPostType.ARM_DOUBLE)
						.condition(PostBlock.FACING, dir);
				}
			}
		}
		
		builder.part()
			.modelFile(modelEmpty).addModel()
			.condition(PostBlock.TYPE, EnumPostType.EMPTY);
	}
	
	private void horizontalPostStateFor(RegistryObject<HorizontalTrussBlock> block){
		BlockModelBuilder modelHTrussSingle	= getPostModel(block, "truss_single");
		BlockModelBuilder modelHTrussA		= getPostModel(block, "truss_multi_a");
		BlockModelBuilder modelHTrussB		= getPostModel(block, "truss_multi_b");
		BlockModelBuilder modelHTrussC		= getPostModel(block, "truss_multi_c");
		BlockModelBuilder modelHTrussD_even	= getPostModel(block, "truss_multi_d_even");
		BlockModelBuilder modelHTrussD_odd	= getPostModel(block, "truss_multi_d_odd");
		BlockModelBuilder modelHTrussPanel	= getPostModel(block, "truss_panel");
		BlockModelBuilder modelPointTop		= getPostModel(block, "truss_point_top");
		BlockModelBuilder modelPointBottom	= getPostModel(block, "truss_point_bottom");
		
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
		
		for(Direction dir:Direction.Plane.HORIZONTAL){
			int yRot = horizontalRotation(dir, false);
			
			builder.part()
				.modelFile(modelHTrussSingle).rotationY(yRot).addModel()
				.condition(HorizontalTrussBlock.TYPE, EnumHTrussType.SINGLE)
				.condition(PostBlock.FACING, dir);
			
			builder.part()
				.modelFile(modelHTrussA).rotationY(yRot).addModel()
				.condition(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_A)
				.condition(PostBlock.FACING, dir);
			
			builder.part()
				.modelFile(modelHTrussB).rotationY(yRot).addModel()
				.condition(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_B)
				.condition(PostBlock.FACING, dir);
			
			builder.part()
				.modelFile(modelHTrussC).rotationY(yRot).addModel()
				.condition(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_C)
				.condition(PostBlock.FACING, dir);
			
			builder.part()
				.modelFile(modelHTrussD_even).rotationY(yRot).addModel()
				.condition(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_D_EVEN)
				.condition(PostBlock.FACING, dir);
			
			builder.part()
				.modelFile(modelHTrussD_odd).rotationY(yRot).addModel()
				.condition(HorizontalTrussBlock.TYPE, EnumHTrussType.MULTI_D_ODD)
				.condition(PostBlock.FACING, dir);
			
		}
		
		builder.part()
			.modelFile(modelPointTop).addModel()
			.condition(HorizontalTrussBlock.CONNECTOR_POINT_TOP, true);
		
		builder.part()
			.modelFile(modelPointBottom).addModel()
			.condition(HorizontalTrussBlock.CONNECTOR_POINT_BOTTOM, true);
		
		builder.part()
			.modelFile(modelHTrussPanel).rotationY(horizontalRotation(Direction.NORTH, false)).addModel()
			.condition(HorizontalTrussBlock.PANEL_NORTH, true);
		
		builder.part()
			.modelFile(modelHTrussPanel).rotationY(horizontalRotation(Direction.SOUTH, false)).addModel()
			.condition(HorizontalTrussBlock.PANEL_SOUTH, true);
		
		builder.part()
			.modelFile(modelHTrussPanel).rotationY(horizontalRotation(Direction.EAST, false)).addModel()
			.condition(HorizontalTrussBlock.PANEL_EAST, true);
		
		builder.part()
			.modelFile(modelHTrussPanel).rotationY(horizontalRotation(Direction.WEST, false)).addModel()
			.condition(HorizontalTrussBlock.PANEL_WEST, true);
	}
	
	private int horizontalRotation(Direction dir, boolean xFlipped){
		int value;
		
		if(xFlipped){ // Should be true when X rotation is 180
			switch(dir){
				case WEST:	value = 90; break;
				case SOUTH:	value = 0; break;
				case EAST:	value = 270; break;
				case NORTH:
				default:	value = 180; break;
			}
		}else{
			switch(dir){
				case WEST:	value = 270; break;
				case SOUTH:	value = 180; break;
				case EAST:	value = 90; break;
				case NORTH:
				default:	value = 0; break;
			}
		}
		
		return value;
	}

	private <P extends GenericPostBlock> BlockModelBuilder getPostModel(RegistryObject<P> block, String name){
		ResourceLocation texture = modLoc("block/posts/post_" + block.get().getPostMaterial().getName());
		
		BlockModelBuilder b = this.models().withExistingParent(postModelPath(block, name), mcLoc("block"))
			.customLoader(ObjModelBuilder::begin).automaticCulling(false).flipV(true)
			.modelLocation(modLoc("models/block/post/obj/" + name + ".obj")).end()
			.texture("texture", texture)
			.texture("particle", texture).renderType("cutout");
		
		return b;
	}
	
	private <P extends GenericPostBlock> String postModelPath(RegistryObject<P> block, String name){
		return block.getId().getPath() + "/" + name;
	}
	
	private ResourceLocation ieLoc(String str){
		return new ResourceLocation("immersiveengineering", str);
	}
}
