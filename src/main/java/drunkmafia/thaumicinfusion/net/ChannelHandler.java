/*
 * @author TheDrunkMafia
 *
 * See http://www.wtfpl.net/txt/copying for licence
 */

package drunkmafia.thaumicinfusion.net;

import drunkmafia.thaumicinfusion.common.lib.ModInfo;
import drunkmafia.thaumicinfusion.net.packet.client.ChunkRequestPacketS;
import drunkmafia.thaumicinfusion.net.packet.client.WandAspectPacketS;
import drunkmafia.thaumicinfusion.net.packet.server.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChannelHandler extends SimpleNetworkWrapper {

    private static ChannelHandler instance;
    private int ordinal;

    public ChannelHandler(String channelName) {
        super(channelName);
    }

    public static void registerPackets() {
        Side S = Side.SERVER, C = Side.CLIENT;

        ChannelHandler handler = ChannelHandler.instance();

        //Server Handled Packets
        handler.registerMessage(ChunkRequestPacketS.Handler.class, ChunkRequestPacketS.class, handler.getOrdinal(), S);
        handler.registerMessage(WandAspectPacketS.Handler.class, WandAspectPacketS.class, handler.getOrdinal(), S);

        //Client Handled Packets
        handler.registerMessage(ChunkSyncPacketC.Handler.class, ChunkSyncPacketC.class, handler.getOrdinal(), C);
        handler.registerMessage(BlockSyncPacketC.Handler.class, BlockSyncPacketC.class, handler.getOrdinal(), C);
        handler.registerMessage(EffectSyncPacketC.Handler.class, EffectSyncPacketC.class, handler.getOrdinal(), C);
        handler.registerMessage(EntitySyncPacketC.Handler.class, EntitySyncPacketC.class, handler.getOrdinal(), C);
        handler.registerMessage(DataRemovePacketC.Handler.class, DataRemovePacketC.class, handler.getOrdinal(), C);
    }

    public static ChannelHandler instance() {
        return ChannelHandler.instance != null ? ChannelHandler.instance : (ChannelHandler.instance = new ChannelHandler(ModInfo.CHANNEL));
    }

    @SideOnly(Side.CLIENT)
    public static World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    public static WorldServer getServerWorld(int dim) {
        return DimensionManager.getWorld(dim);
    }

    private int getOrdinal() {
        return this.ordinal++;
    }
}
