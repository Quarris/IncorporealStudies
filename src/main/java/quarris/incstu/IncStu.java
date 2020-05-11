package quarris.incstu;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quarris.incstu.proxy.ClientProxy;
import quarris.incstu.proxy.IProxy;
import quarris.incstu.proxy.ServerProxy;

@Mod(IncStu.MODID)
public class IncStu {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "incstu";

    public static ResourceLocation createRes(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static final IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public IncStu() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    public void setup(FMLCommonSetupEvent event) {
        proxy.setup(event);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        proxy.clientSetup(event);
    }

}
