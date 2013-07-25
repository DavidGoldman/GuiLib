package com.mcf.davidee.guilib.basic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Widget;
import com.mcf.davidee.guilib.core.Button.ButtonHandler;

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
		mc.displayGuiScreen(parent);
	}

	/**
	 * Called when the selected container did not capture this keyboard event.
	 * 
	 * @param c Character typed (if any)
	 * @param code Keyboard.KEY_ code for this key
	 */
	protected void unhandledKeyTyped(char c, int code) { }

	/**
	 * Called to draw this screen's background
	 */
	protected void drawBackground() {
		drawDefaultBackground();
	}

	
	@Override
	public void drawScreen(int mx, int my, float f) {
		drawBackground();
		List<Widget> overlays = new ArrayList<Widget>();
		for (Container c : containers)
			overlays.addAll(c.draw(mx, my, mc.displayWidth / width));
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
	protected void mouseMovedOrUp(int mx, int my, int code) {
		if (code == 0){
			for (Container c : containers)
				c.mouseReleased(mx, my);
		}
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if (delta != 0 && selectedContainer != null)
			selectedContainer.mouseWheel(MathHelper.clamp_int(delta,-5,5));
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
	public void onGuiClosed() {
		closed = true;
		Keyboard.enableRepeatEvents(false);
	}

	public class CloseHandler implements ButtonHandler{
		public void buttonClicked(Button button) {
			close();
		}
	}

}
