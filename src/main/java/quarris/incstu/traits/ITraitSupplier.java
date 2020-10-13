package quarris.incstu.traits;

import net.minecraft.entity.Entity;

public interface ITraitSupplier<T extends Entity> {

    ITrait<T> create(T holder);

}
