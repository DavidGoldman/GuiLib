package com.mcf.davidee.guilib.vanilla.items;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.core.Widget;

/**
 * 
 * This class is a Widget copy of a vanilla item button. 
 * This button supports "Air" - an item with ID 0.
 * 
 * Note that items use zLevel for rendering - change zLevel as needed.
 *
 */
public class ItemButton extends Button implements Shiftable {

	public static final int WIDTH = 18;
	public static final int HEIGHT = 18;
	public static final RenderItem itemRenderer = new RenderItem();

	protected ItemStack item;
	protected List<Widget> tooltip;

	private GuiScreen parent;
	protected boolean hover;

	public ItemButton(ItemStack item, ButtonHandler handler) {
		super(WIDTH, HEIGHT, handler);

		this.parent = mc.currentScreen;
		this.zLevel = 100;
		setItem(item);
	}

	protected void setItem(ItemStack item) {
		if (item.getItem() == null && Item.func_150891_b(item.getItem()) != 0)      /* getIdFromItem */
			throw new IllegalArgumentException("Item to display does not exist");
		this.item = item;
		this.tooltip = Arrays.asList((Widget)new ItemTooltip(item, parent));
	}

	/**
	 * Draws the item or string "Air" if itemID = 0.
	 */
	@Override
	public void draw(int mx, int my) {
		hover = inBounds(mx, my);
		if (hover) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			drawRect(x, y, x + width, y + height, 0x55909090);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			tooltip.get(0).setPosition(mx, my);
		}
        //getIdFromItem
		if (Item.func_150891_b(item.getItem()) != 0) {
			RenderHelper.enableGUIStandardItemLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			itemRenderer.zLevel = this.zLevel;
			itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, x + 1, y + 1);
			itemRenderer.zLevel = 0;
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
		}
		else //Air
			drawString(mc.fontRenderer, "Air" , x + 3, y + 5, -1);
	}


	@Override
	public List<Widget> getTooltips() {
		return (hover) ? tooltip : super.getTooltips();
	}

	@Override
	public void shiftY(int dy) {
		this.y += dy;
	}
}
