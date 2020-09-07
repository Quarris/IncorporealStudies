package quarris.incstu.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import quarris.incstu.Helper;
import quarris.incstu.traits.ITraitHolder;

public class CommonProxy {

    public void clientSetup(FMLClientSetupEvent event) {
    }

    public void setup(FMLCommonSetupEvent event) {
        Helper.registerCapability(ITraitHolder.class);
    }
}
