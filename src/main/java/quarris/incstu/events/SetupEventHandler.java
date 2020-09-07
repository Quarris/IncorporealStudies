package quarris.incstu.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import quarris.incstu.IncStu;
import quarris.incstu.traits.EntityTraits;

@Mod.EventBusSubscriber(modid = IncStu.MODID)
public class SetupEventHandler {

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(IncStu.createRes("traits"), new EntityTraits(event.getObject()));
    }
}
