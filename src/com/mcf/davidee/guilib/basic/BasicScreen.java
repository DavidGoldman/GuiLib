package com.mcf.davidee.guilib.basic;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Widget;

/**
 * 
 * The core GuiScreen - use this class for your GUIs.
 *
 */
public abstract class BasicScreen extends GuiScreen {

	private GuiScreen parent;
	private boolean hasInit, closed;
	protected List<Container> containers;
	protected Container selectedContainer;

	public BasicScreen(GuiScreen parent) {
		this.parent = parent;
		this.containers = new ArrayList<Container>();
	}

	/**
	 * Revalidate this GUI.
	 * Reset your widget locations/dimensions here.
	 */
	protected abstract void revalidateGui();
	
	/**
	 * Called ONCE to create this GUI.
	 * Create your containers and widgets here.
	 */
	protected abstract void createGui();
	
	/**
	 * Called when this GUI is reopened after being closed.
	 */
	protected abstract void reopenedGui();

	public GuiScreen getParent() {
		return parent;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void close() {
        Minecraft mc = Minecraft.getMinecraft();
        //displayGuiScreen
		mc.func_147108_a(parent);
	}

	/**
	 * Called when the selectedContainer did not capture this keyboard event.
	 * 
	 * @param c Character typed (if any)
	 * @param code Keyboard.KEY_ code for this key
	 */
	protected void unhandledKeyTyped(char c, int code) { }

	/**
	 * Called to draw this screen's background
	 */
	protected void drawBackground() {
		//drawDefaultBackground
        func_146276_q_();
	}

	
	@Override
	public void drawScreen(int mx, int my, float f) {
		drawBackground();
        Minecraft mc = Minecraft.getMinecraft();
		List<Widget> overlays = new ArrayList<Widget>();
		int scale = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).getScaleFactor();
		for (Container c : containers)
			overlays.addAll(c.draw(mx, my, scale));
		for (Widget w : overlays)
			w.draw(mx, my);
	}

	@Override
	public void updateScreen() {
		for (Container c : containers)
			c.update();
	}

	@Override
	protected void mouseClicked(int mx, int my, int code) {
		if (code == 0){
			for (Container c : containers){
				if (c.mouseClicked(mx, my)){
					selectedContainer = c;
					break;
				}
			}
			for (Container c : containers)
				if (c != selectedContainer)
					c.setFocused(null);
		}
	}

	@Override
	protected void func_146286_b(int mx, int my, int code) {    /* mouseMovedOrUp */
		if (code == 0){
			for (Container c : containers)
				c.mouseReleased(mx, my);
		}
	}

	/**
	 * See {@link GuiScreen#handleMouseInput} for more information about mx and my.
	 */
	@Override
	public void func_146274_d() {   /* handleMouseInput */
		super.func_146274_d();
        Minecraft mc = Minecraft.getMinecraft();
        int width = field_146294_l;
        int height = field_146295_m;
		int delta = Mouse.getEventDWheel();
		if (delta != 0) {
			int mx = Mouse.getEventX() * width / mc.displayWidth;
			int my = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			boolean handled = false;
			delta = MathHelper.clamp_int(delta, -5, 5);
			
			for (Container c : containers) {
				if (c.inBounds(mx, my)) {
					c.mouseWheel(delta);
					handled = true;
					break;
				}
			}
			if (!handled && selectedContainer != null)
				selectedContainer.mouseWheel(delta);
		}
	}

	@Override
	public void keyTyped(char c, int code) {
		boolean handled = (selectedContainer != null) ? selectedContainer.keyTyped(c, code) : false;
		if (!handled)
			unhandledKeyTyped(c,code);
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		if (!hasInit){
			createGui();
			hasInit = true;
		}
		revalidateGui();
		if (closed){
			reopenedGui();
			closed = false;
		}
	}

	public void drawCenteredStringNoShadow(FontRenderer ft, String str, int cx, int y, int color) {
		ft.drawString(str, cx - ft.getStringWidth(str) / 2, y, color);
	}


	@Override
	public void func_146281_b() {   /* onGuiClosed */
		closed = true;
		Keyboard.enableRepeatEvents(false);
	}

	public class CloseHandler implements ButtonHandler{
		public void buttonClicked(Button button) {
			close();
		}
	}

}
