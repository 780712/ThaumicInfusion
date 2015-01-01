package drunkmafia.thaumicinfusion.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by DrunkMafia on 08/07/2014.
 * <p/>
 * See http://www.wtfpl.net/txt/copying for licence
 */
public class Savable {

    public Savable() {
    }

    public static Savable loadDataFromNBT(NBTTagCompound tag) {
        if (!tag.hasKey("class")) return null;
        try {
            Class<?> c = Class.forName(tag.getString("class"));
            if (Savable.class.isAssignableFrom(c)) {
                Savable data = (Savable) c.newInstance();
                data.readNBT(tag);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeNBT(NBTTagCompound tagCompound) {
        tagCompound.setString("class", this.getClass().getCanonicalName());
    }

    public void readNBT(NBTTagCompound tagCompound) {}
}
