package quarris.incstu.traits;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.Set;

public interface ITraitHolder extends ICapabilitySerializable<CompoundNBT> {

    Set<ResourceLocation> activeTraitNames();

    <T extends Entity> void addTrait(ResourceLocation name, ITrait<T> trait);

    <T extends Entity> void removeTrait(ResourceLocation name);

    boolean hasTrait(ResourceLocation name);
}
