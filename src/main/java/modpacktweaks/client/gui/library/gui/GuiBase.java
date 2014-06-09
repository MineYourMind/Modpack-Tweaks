package modpacktweaks.client.gui.library.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import modpacktweaks.client.gui.library.gui.element.ElementBase;
import modpacktweaks.client.gui.library.gui.element.ElementFakeItemSlot;
import modpacktweaks.client.gui.library.gui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * A modular gui without the need for an inventory. This should be used instead of {@link GuiBaseContainer} if you do not require an inventory to be displayed on the screen.
 * 
 * @author Alz454
 */
public class GuiBase extends GuiScreen implements IGuiBase
{
    protected RenderItem itemRenderer = new RenderItem();

    protected int xSize = 176;
    protected int ySize = 166;
    protected int guiLeft;
    protected int guiTop;

    protected int mouseX = 0;
    protected int mouseY = 0;

    protected int lastIndex = -1;

    protected boolean drawName = true;

    protected String name;
    protected ResourceLocation texture;
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();
    protected List<String> tooltip = new LinkedList<String>();

    public GuiBase()
    {

    }

    public GuiBase(ResourceLocation texture)
    {
        this.texture = texture;
    }

    @Override
    public ElementBase addElement(ElementBase element)
    {
        elements.add(element);

        return element;
    }

    @Override
    public void addElements()
    {

    }

    @Override
    public void addTabs()
    {

    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawBackgroundTexture()
    {
        if (texture != null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        }
    }

    @Override
    public void drawElements()
    {
        for (ElementBase element : elements)
        {
            element.update();

            if (element.isVisible())
            {
                element.draw();
            }
        }
    }

    public void drawGuiBackgroundLayer(float f, int mouseX, int mouseY)
    {
        this.mouseX = mouseX - guiLeft;
        this.mouseY = mouseY - guiTop;

        drawBackgroundTexture();
        drawElements();
    }

    public void drawGuiForegroundLayer(int mouseX, int mouseY)
    {
        if (drawName)
        {
            fontRenderer.drawString(StatCollector.translateToLocal(name), GuiUtils.getCenteredOffset(this, StatCollector.translateToLocal(name), xSize), 6, 0x404040);
        }

        ElementBase element = getElementAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (element != null && !element.isDisabled())
        {
            List<String> list = new ArrayList<String>();
            element.addTooltip(list);

            if (!list.isEmpty())
            {
                GuiUtils.drawTooltipHoveringText(this, list, mouseX - guiLeft, mouseY - guiTop);
                return;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f)
    {
        drawDefaultBackground();
        drawBackgroundTexture();
        drawGuiBackgroundLayer(f, mouseX, mouseY);

        float left = guiLeft, top = guiTop;

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        super.drawScreen(mouseX, mouseY, f);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(left, top, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f / 1.0F, 240f / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);

        drawGuiForegroundLayer(mouseX, mouseY);

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public ElementBase getElementAtPosition(int mouseX, int mouseY)
    {
        for (ElementBase element : elements)
        {
            if (element.isVisible() && element.intersectsWith(mouseX, mouseY))
            {
                return element;
            }
        }

        return null;
    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    @Override
    public int getGuiLeft()
    {
        return guiLeft;
    }

    @Override
    public int getGuiTop()
    {
        return guiTop;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public RenderItem getItemRenderer()
    {
        return itemRenderer;
    }

    @Override
    public Minecraft getMinecraft()
    {
        return mc;
    }

    @Override
    public int getMouseX()
    {
        return mouseX;
    }

    @Override
    public int getMouseY()
    {
        return mouseY;
    }

    @Override
    public TextureManager getTextureManager()
    {
        return getMinecraft().renderEngine;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public float getZLevel()
    {
        return zLevel;
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {

    }

    @Override
    public void handleElementFakeSlotItemChange(ElementFakeItemSlot slot)
    {

    }

    @Override
    public void initGui()
    {
        super.initGui();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        elements.clear();
        buttonList.clear();

        addElements();
        addTabs();
    }

    @Override
    public boolean isItemStackAllowedInFakeSlot(ElementFakeItemSlot slot, ItemStack stack)
    {
        return false;
    }

    @Override
    protected void keyTyped(char character, int index)
    {
        super.keyTyped(character, index);

        if (index == 1 || index == getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            getMinecraft().thePlayer.closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        ElementBase element = getElementAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (element != null)
        {
            if (element.isVisible())
            {
                element.handleMouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
            }
        }
    }

    @Override
    public void setZLevel(float zlevel)
    {
        zLevel = zlevel;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (!getMinecraft().thePlayer.isEntityAlive() || getMinecraft().thePlayer.isDead)
        {
            getMinecraft().thePlayer.closeScreen();
        }
    }
}
