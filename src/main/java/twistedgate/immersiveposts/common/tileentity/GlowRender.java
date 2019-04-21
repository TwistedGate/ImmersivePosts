package twistedgate.immersiveposts.common.tileentity;

/*
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import twistedgate.immersiveposts.common.blocks.BlockPost;
*/

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/** Not certain i can pull this off */
public class GlowRender extends TileEntitySpecialRenderer<TileEntityGlowy>{
	@Override
	public void render(TileEntityGlowy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		//super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		/*
		TextureManager textureManager=Minecraft.getMinecraft().getTextureManager();
		BlockRendererDispatcher dispatcher=Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockModelShapes shapes=dispatcher.getBlockModelShapes();
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);
			
			IBlockState state=((BlockPost)te.getBlockType()).getStateFromMeta(te.getBlockMetadata());
			IBakedModel model=shapes.getModelForState(state);
		}
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		*/
	}
}
