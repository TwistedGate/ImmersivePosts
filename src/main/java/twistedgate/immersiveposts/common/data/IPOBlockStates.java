package twistedgate.immersiveposts.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;

public class IPOBlockStates extends BlockStateProvider{
	public IPOBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, IPOMod.ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels(){
	}
}
