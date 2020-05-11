package quarris.incstu.proxy;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import quarris.incstu.ModContent;
import quarris.incstu.client.gui.ResearchTableScreen;

public class ClientProxy implements IProxy {
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContent.RESEARCH_TABLE_CONTAINER_TYPE, ResearchTableScreen::new);
    }

    @Override
    public void setup(FMLCommonSetupEvent event) {

    }
}
