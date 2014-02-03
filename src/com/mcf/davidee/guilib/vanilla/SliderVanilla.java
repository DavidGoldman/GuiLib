package com.mcf.davidee.guilib.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Slider;

/**
 * 
 * Vanilla GuiSlider in Widget form.
 *
 */
public class SliderVanilla extends Slider {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

	public SliderVanilla(int width, int height, float value, SliderFormat format) {
		super(width, height, value, format);

	}

	public SliderVanilla(float value, SliderFormat format) {
		this(150, 20, value, format);
	}

	@Override
	public void handleClick(int mx, int my) {
		super.handleClick(mx, my);
		//getSoundHandler.playSound; unknown
		mc.func_147118_V().func_147682_a(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
	}

	@Override
	public void draw(int mx, int my) {
		if (dragging){
			value = (float)(mx - (this.x + 4)) / (float)(this.width - 8);
			value = MathHelper.clamp_float(value, 0, 1);
		}

		mc.getTextureManager().bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(x, y, 0, 46, width / 2, height);
		drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46, width / 2, height);
		drawTexturedModalRect(x + (int)(value * (float)(width - 8)), y, 0, 66, 4, 20);
		drawTexturedModalRect(x + (int)(value * (float)(width - 8)) + 4, y, 196, 66, 4, 20);
		drawCenteredString(mc.fontRenderer, format.format(this), x + width / 2, y + (height - 8) / 2, 
				(inBounds(mx,my)) ? 16777120 : 0xffffff);
	}

}
