package twistedgate.immersiveposts.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.IPOStuff;
import twistedgate.immersiveposts.ImmersivePosts;

public class MultiMetaItem extends Item{
	private String[] names;
	public final String regName;
	public MultiMetaItem(String regName){
		this.regName=regName;
		
		setCreativeTab(ImmersivePosts.creativeTab);
		setRegistryName(new ResourceLocation(IPOMod.ID, regName));
		setTranslationKey(IPOMod.ID+"."+regName);
		setFull3D();
		setHasSubtypes(true);
		
		IPOStuff.ITEMS.add(this);
	}
	
	protected void setNames(String...names){
		if(names!=null && names.length>16)
			throw new IllegalArgumentException("Too many names.");
		
		this.names=names;
	}
	
	public String getName(int index){
		if(index<0 || index>=this.names.length)
			return "<error>";
		
		return this.names[index];
	}
	
	public int getSubItemCount(){
		return this.names.length;
	}
	
	
	@Override
	public String getTranslationKey(ItemStack stack){
		return this.getTranslationKey()+"."+this.names[stack.getMetadata()];
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		if(tab!=null && tab.equals(ImmersivePosts.creativeTab)){
			for(int i=0;i<this.names.length;i++)
				items.add(new ItemStack(this,1,i));
		}
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}
}
