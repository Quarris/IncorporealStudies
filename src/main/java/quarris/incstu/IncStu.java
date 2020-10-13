package quarris.incstu;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quarris.incstu.commands.TraitCommands;
import quarris.incstu.proxy.ClientProxy;
import quarris.incstu.proxy.CommonProxy;
import quarris.incstu.traits.ITraitSupplier;
import quarris.incstu.traits.TraitSystem;
import quarris.incstu.traits.impl.BlockReachTrait;
import quarris.incstu.traits.impl.EnderTeleportTrait;

import java.util.Collections;

@Mod(IncStu.MODID)
public class IncStu {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "incstu";

    public static final CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.SOUL_SAND);
        }
    };

    public static ResourceLocation createRes(String name) {
        return new ResourceLocation(MODID, name);
    }

    public IncStu() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(proxy::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(proxy::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);

        TraitSystem.registerTrait(createRes("block_reach"), BlockReachTrait::new);
        TraitSystem.registerTrait(createRes("ender_teleport"), EnderTeleportTrait::new);

        TraitSystem.registerTraitComponent(createRes("ender_teleport"), TickEvent.PlayerTickEvent.class, EnderTeleportTrait::teleport, e -> Collections.singleton(e.player));
    }

    @SubscribeEvent
    public void registerCommands(FMLServerStartingEvent event) {
        TraitCommands.register(event.getCommandDispatcher());
    }

    public void data(GatherDataEvent event) {
        //QLibApi.addAdvancement(new IncStuAdvancement());
    }
}
