package modpacktweaks.client.gui.library.gui.element;

import modpacktweaks.client.gui.library.gui.GuiBase;
import modpacktweaks.client.gui.modGuides.GuiGuideBase;

public class ElementTextClickable extends ElementText
{
	private String modid;

	public ElementTextClickable(GuiBase gui, int x, int y, String displayText, String tooltip, int color, String modid)
	{
		super(gui, x, y, displayText, tooltip, color, false);

		this.modid = modid;
	}

	@Override
	public boolean handleMouseClicked(int x, int y, int mouseButton)
	{
		((GuiGuideBase) this.gui).displayModInfo(modid);
		return true;
	}

	@Override
	public void draw()
	{
		if (intersectsWith(gui.getMouseX(), gui.getMouseY()))
			this.colour = 0x505050;
		else
			this.colour = 0x282828;
		super.draw();
	}
}