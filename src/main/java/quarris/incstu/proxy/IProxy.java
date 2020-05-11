package quarris.incstu.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy {
    default void clientSetup(FMLClientSetupEvent event) {}

    void setup(FMLCommonSetupEvent event);
}
