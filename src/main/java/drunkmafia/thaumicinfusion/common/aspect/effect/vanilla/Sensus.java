package drunkmafia.thaumicinfusion.common.aspect.effect.vanilla;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.thaumicinfusion.common.aspect.AspectEffect;
import drunkmafia.thaumicinfusion.common.block.InfusedBlock;
import drunkmafia.thaumicinfusion.common.util.annotation.Effect;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.items.armor.ItemGoggles;

/**
 * Created by DrunkMafia on 25/07/2014.
 * <p/>
 * See http://www.wtfpl.net/txt/copying for licence
 */
@Effect(aspect = ("sensus"), cost = 4, hasCustomBlock = true)
public class Sensus extends AspectEffect {

    boolean shouldRender, oldRender;

    @Override
    @SideOnly(Side.CLIENT)
    public void updateBlock(World world) {
        if(!world.isRemote)
            return;

        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3];
        shouldRender = stack != null && stack.getItem() instanceof ItemGoggles;
        if(shouldRender != oldRender){
            oldRender = shouldRender;
            Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(pos.x, pos.y, pos.z);
        }
    }

    @Override
    public InfusedBlock getBlock() {
        return new InfusedBlock(Material.rock).setPass(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRender(World world, int x, int y, int z, RenderBlocks blocks) {
        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3];
        return stack != null && stack.getItem() instanceof ItemGoggles;
    }
}
