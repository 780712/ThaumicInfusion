package drunkmafia.thaumicinfusion.net.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import drunkmafia.thaumicinfusion.common.aspect.AspectEffect;
import drunkmafia.thaumicinfusion.common.util.BlockHelper;
import drunkmafia.thaumicinfusion.common.util.WorldCoord;
import drunkmafia.thaumicinfusion.common.world.BlockData;
import drunkmafia.thaumicinfusion.common.world.BlockSavable;
import drunkmafia.thaumicinfusion.net.ChannelHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * Created by DrunkMafia on 15/11/2014.
 * See http://www.wtfpl.net/txt/copying for licence
 */
public class EffectSyncPacketC  implements IMessage {

    public EffectSyncPacketC() {}

    private AspectEffect effect;
    private NBTTagCompound tagCompound;

    public EffectSyncPacketC(AspectEffect effect) {
        this.effect = effect;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            NBTTagCompound tag = new PacketBuffer(buf).readNBTTagCompoundFromBuffer();
            if (tag != null) {
                tagCompound = tag;
                effect = AspectEffect.loadDataFromNBT(tag);
            }
        } catch (Exception e) {}
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            if (effect != null) {
                NBTTagCompound tag = new NBTTagCompound();
                effect.writeNBT(tag);
                new PacketBuffer(buf).writeNBTTagCompoundToBuffer(tag);
            }
        } catch (Exception e) {}
    }

    public static class Handler implements IMessageHandler<EffectSyncPacketC, IMessage> {
        @Override
        public IMessage onMessage(EffectSyncPacketC message, MessageContext ctx) {
            AspectEffect effect = message.effect;
            if (effect == null || ctx.side.isServer()) return null;
            World world = ChannelHandler.getPlayer(ctx).worldObj;
            WorldCoord pos = effect.getPos();
            BlockData data = BlockHelper.getWorldData(world).getBlock(BlockData.class, effect.getPos());
            if(data != null &&  data.getEffect(effect.getClass()) != null)
                data.getEffect(effect.getClass()).readNBT(message.tagCompound);

            Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(pos.x, pos.y, pos.z);
            return null;
        }
    }
}
