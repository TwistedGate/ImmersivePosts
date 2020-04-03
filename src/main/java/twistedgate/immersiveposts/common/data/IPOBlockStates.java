package twistedgate.immersiveposts.common.data;

import java.util.ArrayList;

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
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
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
				generateStatesFor((BlockPost)block);
		
		fence(IPOStuff.fence_Iron, "fences/iron", new ResourceLocation("block/iron_block"));
		fence(IPOStuff.fence_Gold, "fences/gold", new ResourceLocation("block/gold_block"));
//		fence(IPOStuff.fence_Copper, "fences/copper", new ResourceLocation("immersiveengineering","block/metal/storage_copper"));
//		fence(IPOStuff.fence_Lead, "fences/lead", new ResourceLocation("immersiveengineering","block/metal/storage_lead"));
//		fence(IPOStuff.fence_Silver, "fences/silver", new ResourceLocation("immersiveengineering","block/metal/storage_silver"));
//		fence(IPOStuff.fence_Nickel, "fences/nickel", new ResourceLocation("immersiveengineering","block/metal/storage_nickel"));
//		fence(IPOStuff.fence_Constantan, "fences/constantan", new ResourceLocation("immersiveengineering","block/metal/storage_constantan"));
//		fence(IPOStuff.fence_Electrum, "fences/electrum", new ResourceLocation("immersiveengineering","block/metal/storage_electrum"));
//		fence(IPOStuff.fence_Uranium, "fences/uranium", new ResourceLocation("immersiveengineering","block/metal/storage_uranium_side"));
		
		loadedModels.backupModels();
	}
	
	private void fence(FenceBlock block, String name, ResourceLocation texture){
		fenceBlock(block, name, texture);
		
		// While it's at it also make the inventory model ready
		String[] s=name.split("/");
		getBuilder(IPOMod.ID+":block/fences/inventory/"+s[1]+"_fence_inventory")
			.parent(getExistingFile(new ResourceLocation("block/fence_inventory")))
			.texture("texture", texture);
	}
	
	protected static final ResourceLocation FORGE_LOADER=new ResourceLocation("forge","obj");
	// Basicly testing which one works and which doesnt
	protected static final ResourceLocation IE_LOADER=new ResourceLocation("immersiveengineering", "ie_obj");
	
	private LoadedModelBuilder getPostModel(BlockPost block, String name){
		ResourceLocation texture=new ResourceLocation(IPOMod.ID, "block/posts/post_"+block.getPostMaterial().name().toLowerCase());
		
		LoadedModelBuilder b=this.loadedModels.withExistingParent(block.getRegistryName().getPath(), mcLoc("block"))
			.loader(FORGE_LOADER)
			.additional("model", new ResourceLocation(IPOMod.ID, "models/block/post/obj/"+name))
			.texture("texture", texture)
			.texture("particle", texture)
			;
		
//		ExistingModelFile model=getExistingFile(new ResourceLocation(IPOMod.ID, "block/post/obj/"+name));
//		model.assertExistence();
		return b;
	}
	
	/** Creates every state possible for a post. (Totaling 640 States) */
	private void generateStatesFor(BlockPost block){
		LoadedModelBuilder modelArm			=getPostModel(block, "arm.obj");
		LoadedModelBuilder modelArmDouble	=getPostModel(block, "arm_double.obj");
		LoadedModelBuilder modelPost		=getPostModel(block, "post.obj");
		LoadedModelBuilder modelPostTop		=getPostModel(block, "post_top.obj");
		LoadedModelBuilder modelPostArm		=getPostModel(block, "post_arm.obj");
		ConfiguredModel modelEmpty			=new ConfiguredModel(getExistingFile(modLoc("block/empty")));
		
		for(EnumPostType type:EnumPostType.values()){
			for(int j=0;j<=1;j++){
				for(int i=0;i<16;i++){
					boolean n=(i&0x01)>0;
					boolean s=(i&0x02)>0;
					boolean e=(i&0x04)>0;
					boolean w=(i&0x08)>0;
					
					for(Direction dir:Direction.Plane.HORIZONTAL){
						PartialBlockstate state=getVariantBuilder(block).partialState()
								.with(BlockPost.FACING, dir)
								.with(BlockPost.TYPE, type)
								.with(BlockPost.FLIP, j==1)
								.with(BlockPost.LPARM_NORTH, n)
								.with(BlockPost.LPARM_EAST,  e)
								.with(BlockPost.LPARM_SOUTH, s)
								.with(BlockPost.LPARM_WEST,  w);
						
						// I know this is such a dirty way of doing it, but how else should i do this.
						ArrayList<ConfiguredModel> tmp=new ArrayList<>(0);
						int yArmRot;
						switch(dir){ // Read from default to top.
							case WEST:	yArmRot=90; break;
							case SOUTH:	yArmRot=180; break;
							case EAST:	yArmRot=-90; break;
							default:	yArmRot=0; break; // North
						}
						
						switch(type){
							case POST:		tmp.add(new ConfiguredModel(modelPost)); break;
							case POST_TOP:	tmp.add(new ConfiguredModel(modelPostTop)); break;
							case ARM:		tmp.add(new ConfiguredModel(modelArm, 0, yArmRot, false)); break;
							case ARM_DOUBLE:tmp.add(new ConfiguredModel(modelArmDouble, 0, yArmRot, false)); break;
							case EMPTY:		tmp.add(modelEmpty); break;
							default:
								break;
							
						}
						
						if(n) tmp.add(new ConfiguredModel(modelPostArm, 0, 0, false));
						if(e) tmp.add(new ConfiguredModel(modelPostArm, 0, -90, false));
						if(s) tmp.add(new ConfiguredModel(modelPostArm, 0, 180, false));
						if(w) tmp.add(new ConfiguredModel(modelPostArm, 0, 90, false));
						
						state.addModels(tmp.toArray(new ConfiguredModel[tmp.size()]));
					}
				}
			}
		}
	}
}
