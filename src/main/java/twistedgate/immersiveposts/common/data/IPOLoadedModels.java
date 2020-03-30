package twistedgate.immersiveposts.common.data;

import java.util.HashMap;
import java.util.Map;

import blusunrize.immersiveengineering.common.data.models.LoadedModelBuilder;
import blusunrize.immersiveengineering.common.data.models.LoadedModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;

public class IPOLoadedModels extends LoadedModelProvider{
	final Map<ResourceLocation, LoadedModelBuilder> models=new HashMap<>();
	public IPOLoadedModels(DataGenerator gen, ExistingFileHelper exHelper){
		super(gen, IPOMod.ID, "block", exHelper);
	}
	
	@Override
	protected void registerModels(){
		super.generatedModels.putAll(models);
	}
	
	public void backupModels(){
		models.putAll(super.generatedModels);
	}
	
	@Override
	public String getName(){
		return "Loaded Models";
	}
}
