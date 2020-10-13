package quarris.incstu.traits;

import net.minecraft.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public interface ITraitComponent<En extends Entity, T extends ITrait<En>, Ev extends Event> {

    void run(En holder, T trait, Ev event);

}
