package com.mcf.davidee.guilib.basic;

import java.util.ArrayList;
import java.util.List;

import com.mcf.davidee.guilib.core.Widget;

import net.minecraft.client.gui.GuiScreen;

public abstract class OverlayScreen extends BasicScreen {

	protected BasicScreen bg;

	public OverlayScreen(BasicScreen bg) {
		super(bg);

		this.bg = bg;
	}

	@Override
	public void drawBackground() {
		bg.drawScreen(-1, -1, 0);
	}

	@Override
	protected void revalidateGui() {
		bg.field_146294_l = field_146294_l; /* width */
		bg.field_146295_m = field_146295_m; /* height */
		bg.revalidateGui();
	}

	@Override
	protected void reopenedGui() { }

}
