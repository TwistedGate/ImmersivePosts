package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

/**
 * @author TwistedGate
 */
public class IPOItemModels extends ItemModelProvider {
	public IPOItemModels(DataGenerator gen, ExistingFileHelper exHelper){
		super(gen, IPOMod.ID, exHelper);
	}
	
	@Override
	protected void registerModels(){
		getBuilder(IPOMod.ID + ":item/postbase")
			.parent(new ExistingModelFile(modLoc("block/postbase"), this.existingFileHelper));
		
		fence(Fences.IRON.get(),		"fence/iron",		mcLoc("block/iron_block"));
		fence(Fences.GOLD.get(),		"fence/gold",		mcLoc("block/gold_block"));
		fence(Fences.COPPER.get(),		"fence/copper",		ieLoc("block/metal/storage_copper"));
		fence(Fences.LEAD.get(),		"fence/lead",		ieLoc("block/metal/storage_lead"));
		fence(Fences.SILVER.get(),		"fence/silver",		ieLoc("block/metal/storage_silver"));
		fence(Fences.NICKEL.get(),		"fence/nickel",		ieLoc("block/metal/storage_nickel"));
		fence(Fences.CONSTANTAN.get(),	"fence/constantan",	ieLoc("block/metal/storage_constantan"));
		fence(Fences.ELECTRUM.get(),	"fence/electrum",	ieLoc("block/metal/storage_electrum"));
		fence(Fences.URANIUM.get(),		"fence/uranium",	ieLoc("block/metal/storage_uranium_side"));
		
		for(EnumPostMaterial m:EnumPostMaterial.values())
			switch(m){
				case WOOD:case IRON:case ALUMINIUM:case STEEL:case NETHERBRICK:case CONCRETE:case CONCRETE_LEADED: continue;
				default:{
					String name = "stick_" + m.toString().toLowerCase();
					getBuilder(IPOMod.ID + ":item/" + name)
						.parent(getExistingFile(mcLoc("item/generated")))
						.texture("layer0", modLoc("item/" + name));
				}
			}
	}
	
	private ResourceLocation ieLoc(String str){
		return new ResourceLocation("immersiveengineering", str);
	}
	
	private void fence(FenceBlock block, String name, ResourceLocation texture){
		try{
			String[] s = name.split("/");
			getBuilder(IPOMod.ID + ":block/fences/inventory/" + s[1] + "_fence_inventory")
				.parent(new ExistingModelFile(new ResourceLocation("block/fence_inventory"), this.existingFileHelper))
				.texture("texture", texture);
		}catch(Throwable e){
			IPODataGen.log.warn("Oops.. {}", e.getMessage());
		}
	}
}
