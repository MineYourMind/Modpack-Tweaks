package modpacktweaks.client.gui;

import java.io.FileNotFoundException;

import modpacktweaks.client.gui.modGuides.GuiGuideBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiHelper
{
	@SideOnly(Side.CLIENT)
	public static UpdateGui updateGui;

	public static void doDownloaderGUI()
	{
		Minecraft.getMinecraft().displayGuiScreen((updateGui = new UpdateGui(Minecraft.getMinecraft().currentScreen, false)));
	}

	public static void doGuideGUI()
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiGuideBase());
	}

	public static void initMap() throws FileNotFoundException
	{
		GuiGuideBase.initMap();
	}

	public static void doBookGUI(EntityPlayer player, ItemStack stack, boolean par3)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(player, stack, par3));
	}
}
