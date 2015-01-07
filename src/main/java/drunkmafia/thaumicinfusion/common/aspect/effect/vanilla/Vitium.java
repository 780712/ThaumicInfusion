package drunkmafia.thaumicinfusion.common.aspect.effect.vanilla;

import drunkmafia.thaumicinfusion.common.aspect.AspectEffect;
import drunkmafia.thaumicinfusion.common.world.WorldCoord;
import drunkmafia.thaumicinfusion.common.util.annotation.Effect;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockFluxGas;
import thaumcraft.common.blocks.BlockFluxGoo;

import java.util.Random;

/**
 * Created by DrunkMafia on 12/11/2014.
 * See http://www.wtfpl.net/txt/copying for licence
 */
@Effect(aspect = "vitium", cost = 1)
public class Vitium extends AspectEffect {

    @Override
    public void aspectInit(World world,WorldCoord pos) {
        super.aspectInit(world, pos);
        if(!world.isRemote)
            updateTick(world, pos.x, pos.y, pos.z, new Random());
    }

    public void updateTick(World world, int x, int y, int z, Random random){
        for(int xCoord = -1; xCoord < 2; xCoord++){
            for(int yCoord = -1; yCoord < 2; yCoord++){
                for(int zCoord = -1; zCoord < 2; zCoord++){
                    Block block = world.getBlock(xCoord + x, yCoord + y, zCoord + z);
                    if(block != null && block != world.getBlock(x, y, z) && block instanceof BlockFluxGoo || block instanceof BlockFluxGas)
                        world.setBlockToAir(xCoord + x, yCoord + y, zCoord + z);
                }
            }
        }

        world.scheduleBlockUpdate(x, y, z, world.getBlock(x, y, z), 50);
    }
}
