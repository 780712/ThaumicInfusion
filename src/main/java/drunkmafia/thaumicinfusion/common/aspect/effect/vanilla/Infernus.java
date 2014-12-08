package drunkmafia.thaumicinfusion.common.aspect.effect.vanilla;

import drunkmafia.thaumicinfusion.common.aspect.AspectEffect;
import drunkmafia.thaumicinfusion.common.util.annotation.Effect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * Created by DrunkMafia on 25/07/2014.
 *
 * See http://www.wtfpl.net/txt/copying for licence
 */
@Effect(aspect = ("infernus"), cost = 4)
public class Infernus extends AspectEffect {

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity ent) {
        setOnFire(ent);
    }

    @Override
    public void onFallenUpon(World world, int x, int y, int z, Entity ent, float fall) {
        setOnFire(ent);
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity ent) {
        setOnFire(ent);
    }

    public void setOnFire(Entity ent){
        if(!(ent instanceof EntityLivingBase))
            return;
        ent.setFire(8);
    }
}
