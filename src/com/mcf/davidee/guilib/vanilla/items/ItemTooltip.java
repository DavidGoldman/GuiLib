package com.mcf.davidee.guilib.vanilla.items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Widget;

/**
 * 
 * This class represents a Widget copy of an Item's tooltip. 
 * 
 */
public class ItemTooltip extends Widget {
	
	public static final char SECTION = '\u00a7';
	
	public static final Map<Class<?>, String> NAME_MAP = new HashMap<Class<?>, String>();
	
	static {
		NAME_MAP.put(BlockPistonExtension.class, "Piston Extension");
		NAME_MAP.put(BlockPistonMoving.class, "Piston Moving");
		NAME_MAP.put(BlockEndPortal.class, "End Portal");
	}
	
	private static String getUnknownName(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
            return ((ItemBlock)item).getUnlocalizedName();
		}
		return "Unknown";
	}

	private final List<String> tooltips;
	private final FontRenderer font;
	private final GuiScreen parent;

	/**
	 * See {@link net.minecraft.client.gui.inventory.GuiContainer#drawScreen} for more information.
	 */
	public ItemTooltip(ItemStack stack, GuiScreen parent) {
		super(0, 0);

        //getIdFromItem
		if (Item.func_150891_b(stack.getItem()) != 0) {
			tooltips = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
			if (!tooltips.isEmpty()) {
				String name = tooltips.get(0);
				if (name.startsWith("tile.null.name")) 
					name = name.replace("tile.null.name", getUnknownName(stack));
				tooltips.set(0, SECTION + "" + stack.getRarity().rarityColor + name);
				for (int i = 1; i < tooltips.size(); ++i)
					tooltips.set(i, EnumChatFormatting.GRAY + tooltips.get(i));
			}
			font = mc.fontRenderer;
		}
		else {
			tooltips = Arrays.asList("Air");
			font = mc.fontRenderer;
		}
		this.parent = parent;
		this.width = getMaxStringWidth();
		this.height = (tooltips.size() > 1) ? tooltips.size()*10 : 8; 
	}

	@Override
	public void setPosition(int newX, int newY) {
		this.x = newX + 12;
		this.y = newY - 12;
		if (x + width + 6 > parent.field_146294_l)      /* width */
			x -= 28 + width;
		if (y + height + 6 > parent.field_146295_m)     /* height */
			y = parent.field_146295_m - height - 6;     /* height */
	}


	/**
	 * See {@link net.minecraft.client.gui.inventory.GuiContainer#drawHoveringText}
	 */
	@Override
	public void draw(int mx, int my) { 
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if (!tooltips.isEmpty()) {
			final int outlineColor = 0xf0100010;
			drawRect(x - 3, y - 4, x + width + 3, y - 3, outlineColor);
			drawRect(x - 3, y + height + 3, x + width + 3, y + height + 4, outlineColor);
			drawRect(x - 3, y - 3, x + width + 3, y + height + 3, outlineColor);
			drawRect(x - 4, y - 3, x - 3, y + height + 3, outlineColor);
			drawRect(x + width + 3, y - 3, x + width + 4, y + height + 3, outlineColor);
			int gradient1 = 1347420415;
			int gradient2 = (gradient1 & 16711422) >> 1 | gradient1 & -16777216;
			drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, gradient1, gradient2);
			drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, gradient1, gradient2);
			drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, gradient1, gradient1);
			drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, gradient2, gradient2);
			for (int index = 0; index < tooltips.size(); ++index) {
				font.drawStringWithShadow(tooltips.get(index), x, y, -1);
				if (index == 0)
					y += 2;
				y += 10;
			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public boolean click(int mx, int my) {
		return false;
	}

	private int getMaxStringWidth() {
		int max = 0;
		for (String s : tooltips) {
			int width = font.getStringWidth(s);
			if (width > max)
				max = width;
		}
		return max;
	}

}