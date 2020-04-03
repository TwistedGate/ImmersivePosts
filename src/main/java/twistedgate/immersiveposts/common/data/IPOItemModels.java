package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.data.models.LoadedModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

public class IPOItemModels extends LoadedModelProvider{
	IPOBlockStates blockStates;
	public IPOItemModels(DataGenerator gen, ExistingFileHelper exHelper, IPOBlockStates blockStates){
		super(gen, IPOMod.ID, "item", exHelper);
		this.blockStates=blockStates;
	}
	
	@Override
	public String getName(){
		return "Item Models";
	}
	
	@Override
	protected void registerModels(){
		for(EnumPostMaterial m:EnumPostMaterial.values())
			switch(m){
				case WOOD:case IRON:case ALUMINIUM:case STEEL:case NETHERBRICK:case CONCRETE:case CONCRETE_LEADED: continue;
				default:{
					String name="stick_"+m.toString().toLowerCase();
					getBuilder(IPOMod.ID+":item/"+name)
						.parent(getExistingFile(mcLoc("item/generated")))
						.texture("layer0", modLoc("item/"+name));
				}
			}
	}
}
