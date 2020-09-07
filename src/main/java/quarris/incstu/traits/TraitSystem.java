package quarris.incstu.traits;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class TraitSystem {

    private static final Map<ResourceLocation, Supplier<ITrait<? extends Entity>>> TRAIT_SUPPLIERS = new HashMap<>();

    public static void registerTrait(ResourceLocation name, Supplier<ITrait<? extends Entity>> traitSupplier) {
        TRAIT_SUPPLIERS.putIfAbsent(name, traitSupplier);
    }

    public static boolean isRegistered(ResourceLocation name) {
        return TRAIT_SUPPLIERS.containsKey(name);
    }

    public static Set<ResourceLocation> traitKeys() {
        return TRAIT_SUPPLIERS.keySet();
    }

    public static <T extends Entity> ITrait<T> createTrait(CompoundNBT nbt) {
        if (nbt.contains("Name")) {
            ITrait<T> trait = (ITrait<T>) TRAIT_SUPPLIERS.get(new ResourceLocation(nbt.getString("Name"))).get();
            trait.deserializeNBT(nbt);
            return trait;
        }

        return null;
    }

    public static <T extends Entity> ITrait<T> createTrait(ResourceLocation name) {
        return (ITrait<T>) TRAIT_SUPPLIERS.get(name).get();
    }

    public static <T extends Entity> void addTraitToEntity(Entity entity, ResourceLocation name, ITrait<T> trait) {
        entity.getCapability(TraitHolderCapability.INSTANCE).ifPresent(cap -> cap.addTrait(name, trait));
    }

    public static <T extends Entity> void removeTraitFromEntity(Entity entity, ResourceLocation name) {
        entity.getCapability(TraitHolderCapability.INSTANCE).ifPresent(cap -> cap.removeTrait(name));
    }
}
