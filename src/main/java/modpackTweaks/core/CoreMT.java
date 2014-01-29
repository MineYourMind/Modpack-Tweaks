package modpackTweaks.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import modpackTweaks.lib.Reference;
import modpackTweaks.util.FileLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class CoreMT implements IFMLLoadingPlugin
{

	@Override
	@Deprecated
	public String[] getLibraryRequestClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		Logger coreLog = Logger.getLogger("ModpackTweaks-Preload");
		
		File mcDir = (File) data.get("mcLocation");
		File modsDir = null;

		try
		{
			modsDir = new File(mcDir.getCanonicalPath() + "/mods/");
		}
		catch (IOException e)
		{
			coreLog.severe("Mods dir does not exist. How did you mess that up?");
		}

		Reference.modsFolder = modsDir;

		Reference.thaumcraftFilename = (String) FileLoader.manuallyGetConfigValue(data, "Thaumcraft_filename", "");
		//Reference.TTFilename = (String) FileLoader.manuallyGetConfigValue(data, "ThaumicTinkerer_filename", "");
		Reference.KAMIFilename = (String) FileLoader.manuallyGetConfigValue(data, "KAMI_filename", "");

		//FileLoader.removeDuplicateMods();

		if ((Boolean) FileLoader.manuallyGetConfigValue(data, "autoEnableTT", new Boolean(true)))
		{
			try
			{
				modsDir = new File(mcDir.getCanonicalPath() + "/mods/");
			}
			catch (IOException e)
			{
				coreLog.severe("Mods dir does not exist. How did you mess that up?");
			}

			Reference.modsFolder = modsDir;

			File thaumcraft = new File(modsDir, Reference.thaumcraftFilename);

			FileLoader.getThaumicTinkererFilenameState();

			if (!thaumcraft.exists())
			{
				coreLog.info("Failed to locate Thaumcraft. Disabling Thaumic Tinkerer.");
				FileLoader.disableTT();
				coreLog.info("Thaumic Tinkerer Disabled.");
			}
			else
			{
				coreLog.info("found Thaumcraft! Enabling Thaumic Tinkerer and addons if needed.");
				FileLoader.enableTT();
			}
		}
	}
}
