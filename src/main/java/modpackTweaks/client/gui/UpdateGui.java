package modpackTweaks.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import modpackTweaks.config.ConfigurationHandler;
import modpackTweaks.util.FileLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UpdateGui extends GuiScreen
{
	protected GuiScreen parentScreen;
	private boolean noShow = true, firstTime;
	
	List<ModDownload> mods;
	List<InstructionsGui> modScreens = new ArrayList<InstructionsGui>();
	Iterator<InstructionsGui> iterator;
	
	private String name;

	public void initModInstallationMenus()
	{
		try
		{
			mods = FileLoader.getDownloadMods();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("The mod download configuration file could not be found");
			return;
		}
		
		if (mods.size() == 0)
		{
			System.out.println("No mods to download, or you have not configured this!");
			return;
		}

		for (ModDownload mod : mods)
		{
			if (!Loader.isModLoaded(mod.modid))
				modScreens.add(new InstructionsGui(mod));
		}
		
		iterator = modScreens.iterator();
	}

	public UpdateGui(GuiScreen parentScreen, boolean firstTime)
	{
		name = ConfigurationHandler.organizationName;
		
		this.parentScreen = parentScreen;

		initModInstallationMenus();

		for (InstructionsGui g : modScreens)
		{
			if (!Loader.isModLoaded(g.mod.modid))
				noShow = false;
		}

		this.firstTime = firstTime;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		if (noShow)
		{
			System.out.println("not opening GUI");
			this.mc.displayGuiScreen(this.parentScreen);
			return;
		}
		else
		{
			
		}

		// Unsure exactly what this does but...it seems necessary
		Keyboard.enableRepeatEvents(true);

		this.buttonList.clear();

		this.buttonList.add(new GuiButton(-1, this.width / 2 - 150, this.height / 2 + 30, 300, 20, "Continue"));
		this.buttonList.add(new GuiButton(11, this.width / 2 - 150, this.height / 2 + 65, 300, 20, "Skip the downloads completely"));
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			if (button.id == 11)
				this.mc.displayGuiScreen(this.parentScreen);
			else
			{
				try
				{
					if (GuiHelper.updateGui.iterator.hasNext())
					{
						this.mc.displayGuiScreen(GuiHelper.updateGui.iterator.next());
					}
					/*
					else if (configGui != null)
					{
						File file=new File("config/TPPI/config/hardconfig.zip");
						if (file.exists())	this.mc.displayGuiScreen(configGui);
						configGui = null;
					}
					*/
					else if (GuiHelper.updateGui.modScreens.size() > 0)
					{
						this.mc.displayGuiScreen(new RestartGui());
					}
					else
					{
						this.mc.displayGuiScreen(this.parentScreen);
					}
				}
				catch (Exception e)
				{
					System.err.println("Error opening webpage, please contact " + name + " Team.");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawScreen(par1, par2, par3, true);
	}

	public void drawScreen(int par1, int par2, float par3, boolean draw)
	{
		if (draw)
		{
			this.drawDefaultBackground();

			if (firstTime)
			{
				this.drawCenteredString(this.fontRenderer, "Hey there! This seems like the first time you are starting "+ name + ". Welcome!", this.width / 2, this.height / 2 - 100, 0xFFFFFF);
				this.drawCenteredString(this.fontRenderer, "This menu will not show again unless enabled in the ModpackTweaks config.", this.width / 2, this.height / 2 - 10, 0xFFFFFF);
				this.drawCenteredString(this.fontRenderer, "Alternatively, you may use the command \"/" + ConfigurationHandler.commandName + " download\" to show it in-game.", this.width / 2, this.height / 2, 0xFFFFFF);
			}

			this.drawCenteredString(this.fontRenderer, "As it turns out, there are some mods we really wanted to include,", this.width / 2, this.height / 2 - 80, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, "but couldn't ship directly with the rest of the pack.", this.width / 2, this.height / 2 - 70, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, "Though we had to leave them out, we built this little utility to", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, "help you all add them manually, to gain what we feel is the full " + name + " experience.", this.width / 2, this.height / 2 - 40, 0xFFFFFF);
			
		}

		super.drawScreen(par1, par2, par3);
	}
}
