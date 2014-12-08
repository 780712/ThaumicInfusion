package drunkmafia.thaumicinfusion.client.renderer.tile;

import drunkmafia.thaumicinfusion.common.block.tile.InfusionCoreTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import drunkmafia.thaumicinfusion.common.util.MathHelper;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.Thaumcraft;

import static drunkmafia.thaumicinfusion.common.lib.BlockInfo.*;

/**
 * Created by DrunkMafia on 19/07/2014.
 * <p/>
 * See http://www.wtfpl.net/txt/copying for licence
 */
public class InfusionCoreRenderer extends TileEntitySpecialRenderer {

    IModelCustom model = AdvancedModelLoader.loadModel(infusionCore_Model);
    float transSpeed = 0.025F, rotSpeed = 2F, hover, ticks;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaTime) {
        InfusionCoreTile core = (InfusionCoreTile) tile;

        if(core == null)
            return;

        updateCore(core, deltaTime);

        Vector3 pos = new Vector3(x + 0.5D, y + 0.5D + core.yLevel, z + 0.5D);

        renderCore(pos, new Vector3(1.4, 1.3, 1.4), core.coreAxies, core.angle, deltaTime);
        renderCore(pos, new Vector3(1, 1.3, 1), core.coreAxies, -core.angle, deltaTime);
        renderInventory(pos, tile);

        if(core.getStackInSlot(0) != null && !core.matrix.crafting)
            Thaumcraft.instance.renderEventHandler.drawTextInAir(pos.x, pos.y, pos.z, deltaTime, core.getStackInSlot(0).stackSize + " ");
    }

    void renderCore(Vector3 pos, Vector3 scale, Vector3 axies, float angle, float deltaTime){

        GL11.glPushMatrix();
        GL11.glTranslated(pos.x, pos.y + hover, pos.z);
        GL11.glScaled(scale.x, scale.y, scale.z);
        GL11.glRotated(angle, axies.x, axies.y, axies.z);

        Minecraft.getMinecraft().renderEngine.bindTexture(infusionCore_Texture);
        model.renderAll();

        GL11.glPopMatrix();
    }

    void renderInventory(Vector3 pos, TileEntity tile){
        InfusionCoreTile coreTile = (InfusionCoreTile) tile;
        ItemStack inv = coreTile.getStackInSlot(0);
        if(inv == null) return;

        ItemStack item = inv.copy();

        GL11.glPushMatrix();

        pos.y -= 0.1F;

        GL11.glTranslated(pos.x, pos.y + hover, pos.z);
        if (item.getItem() instanceof ItemBlock)
            GL11.glScalef(1.5F, 1.5F, 1.5F);
        else
            GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);

        item.stackSize = 1;
        EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0D, 0.0D, 0.0D, item);
        entityitem.hoverStart = 0.0F;

        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        if (!Minecraft.isFancyGraphicsEnabled()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        }
        GL11.glPopMatrix();
    }

    float rotTarget = 0F;

    void updateCore(InfusionCoreTile core, float deltaTime){
        if(core.matrix != null && core.matrix.active){
            if(core.matrix.crafting) {
                rotTarget = 0.7F;
                core.coreAxies = new Vector3(1, 1, 1);
            }else {
                rotTarget = 0;
                core.coreAxies = new Vector3(0, 1, 0);
            }
            core.yLevel = MathHelper.lerp(core.yLevel, rotTarget, transSpeed * deltaTime, 0.05F);
            core.angle = MathHelper.lerp(core.angle, 360, rotSpeed * deltaTime);
            if(core.angle == 360)
                core.angle = 0;

            ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted + deltaTime;
            hover = net.minecraft.util.MathHelper.sin(ticks % 32767.0F / 16.0F) * 0.05F;
        }
    }
}
