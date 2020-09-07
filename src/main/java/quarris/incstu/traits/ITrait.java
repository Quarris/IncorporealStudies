package quarris.incstu.traits;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITrait<T extends Entity> extends INBTSerializable<CompoundNBT> {

    default void onApplied(T entity) {
    }

    default void onRemoved(T entity) {
    }

    boolean canApplyTo(Entity entity);

    @Override
    default CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    default void deserializeNBT(CompoundNBT nbt) {}
}
