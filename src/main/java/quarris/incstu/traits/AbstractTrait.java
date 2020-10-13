package quarris.incstu.traits;

import net.minecraft.entity.Entity;

public abstract class AbstractTrait<T extends Entity> implements ITrait<T> {

    protected final T holder;

    public AbstractTrait(T holder) {
        this.holder = holder;
    }
}
