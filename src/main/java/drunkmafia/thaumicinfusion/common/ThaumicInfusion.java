package drunkmafia.thaumicinfusion.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import drunkmafia.thaumicinfusion.common.aspect.AspectHandler;
import drunkmafia.thaumicinfusion.common.block.BlockHandler;
import drunkmafia.thaumicinfusion.common.block.TIBlocks;
import drunkmafia.thaumicinfusion.common.command.TICommand;
import drunkmafia.thaumicinfusion.common.event.CommonEventContainer;
import drunkmafia.thaumicinfusion.common.event.TickEventHandler;
import drunkmafia.thaumicinfusion.common.intergration.ThaumcraftIntergration;
import drunkmafia.thaumicinfusion.common.tab.TITab;
import drunkmafia.thaumicinfusion.net.ChannelHandler;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import static drunkmafia.thaumicinfusion.common.lib.ModInfo.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies="required-after:Thaumcraft;", canBeDeactivated = true)
public class ThaumicInfusion {

    @Instance(MODID)
    public static ThaumicInfusion instance;

    @SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = COMMON_PROXY_PATH)
    public static CommonProxy proxy;

    public boolean isServer;
    public Logger logger;
    public Configuration config;

    @EventHandler
    public void constructing(FMLConstructionEvent event){
        AspectHandler.getInstance().addMod();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        isServer = event.getSide().isServer();
        config = new Configuration(event.getSuggestedConfigurationFile());

        TITab.init();
        TIBlocks.initBlocks();

        AspectHandler.getInstance().preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ChannelHandler.init();
        BlockHandler.blacklistBlocks();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        MinecraftForge.EVENT_BUS.register(new CommonEventContainer());
        FMLCommonHandler.instance().bus().register(new TickEventHandler());
        proxy.initRenderers();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        AspectHandler.getInstance().postInit();
        ThaumcraftIntergration.init();
        BlockHandler.generateSafeTiles();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event){
        TICommand.init((ServerCommandManager)event.getServer().getCommandManager());
    }

    public static String translate(String key, Object... params) {
        return StatCollector.translateToLocalFormatted(key, params);
    }

    public static Logger getLogger(){
        return ThaumicInfusion.instance.logger;
    }
}
