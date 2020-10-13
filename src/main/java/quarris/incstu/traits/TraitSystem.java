package quarris.incstu.traits;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.*;
import java.util.function.Function;

public class TraitSystem {

    @CapabilityInject(ITraitHolder.class)
    public static Capability<ITraitHolder> CAPABILITY;

    private static final Map<ResourceLocation, ITraitSupplier<? extends Entity>> TRAIT_SUPPLIERS = new HashMap<>();
    private static final Table<ResourceLocation, Class<? extends Event>, ITraitComponent> TRAIT_COMPONENTS = HashBasedTable.create();

    public static <T extends Entity> void registerTrait(ResourceLocation name, ITraitSupplier<T> traitSupplier) {
        TRAIT_SUPPLIERS.putIfAbsent(name, traitSupplier);
    }

    public static <En extends Entity, T extends ITrait<En>, Ev extends Event> void registerTraitComponent(ResourceLocation name, Class<Ev> eventClass, ITraitComponent<En, T, Ev> component, Function<Ev, Set<En>> collector) {
        registerTraitComponent(name, EventPriority.NORMAL, eventClass, component, collector);
    }

    public static <En extends Entity, T extends ITrait<En>, Ev extends Event> void registerTraitComponent(ResourceLocation name, EventPriority priority, Class<Ev> eventClass, ITraitComponent<En, T, Ev> component, Function<Ev, Set<En>> collector) {
        TRAIT_COMPONENTS.put(name, eventClass, component);

        MinecraftForge.EVENT_BUS.addListener(priority, false, eventClass, event -> {
            Set<En> affected = collector.apply(event);
            if (affected != null) {
                affected.stream().filter(Objects::nonNull).forEach(holder -> holder.getCapability(CAPABILITY).ifPresent(cap -> {
                            if (cap.hasTrait(name)) {
                                T trait = (T) cap.getTrait(name);
                                for (Map.Entry<Class<? extends Event>, ITraitComponent> entry : TRAIT_COMPONENTS.row(name).entrySet()) {
                                    if (entry.getKey().isAssignableFrom(event.getClass())) {
                                        ITraitComponent comp = TRAIT_COMPONENTS.get(name, entry.getKey());
                                        if (comp != null) {
                                            comp.run(holder, trait, event);
                                        }
                                    }
                                }
                            }
                        })
                );
            }
        });

    }

    /*
    public static <F extends IEnchantEffect, T extends Event> void registerComponent(ResourceLocation name, Class<T> eventClass, IEffectComponent<F, T> component, Function<T, Collection<PlayerEntity>> playerGetter) {
        COMPONENTS.put(name, eventClass, component);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, eventClass, new Consumer<T>() {
            private Function<T, Collection<PlayerEntity>> getPlayer = playerGetter;

            @SuppressWarnings("unchecked")
            @Override
            public void accept(T event) {
                Collection<PlayerEntity> players = getPlayer.apply(event);
                if (players != null) {
                    players.stream().filter(Objects::nonNull).forEach(player -> player.getCapability(EnchantabilityApi.playerEnchant).ifPresent(cap -> {
                                for (IEnchantEffect effect : cap.getEnchants()) {
                                    if (effect.getName().equals(name)) {
                                        for (Map.Entry<Class<? extends Event>, IEffectComponent<? extends IEnchantEffect, ? extends Event>> entry : COMPONENTS.row(name).entrySet()) {
                                            if (entry.getKey().isAssignableFrom(event.getClass())) {
                                                IEffectComponent<F, T> comp = (IEffectComponent<F, T>) COMPONENTS.get(effect.getName(), entry.getKey());
                                                if (comp != null) {
                                                    comp.run((F) effect, event);
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                    );
                }
            }
        });
    }
    */

    public static boolean isRegistered(ResourceLocation name) {
        return TRAIT_SUPPLIERS.containsKey(name);
    }

    public static Set<ResourceLocation> traitKeys() {
        return TRAIT_SUPPLIERS.keySet();
    }

    public static <T extends Entity> ITrait<T> createTrait(T holder, CompoundNBT nbt) {
        if (nbt.contains("Name")) {
            ITrait<T> trait = ((ITraitSupplier<T>) TRAIT_SUPPLIERS.get(new ResourceLocation(nbt.getString("Name")))).create(holder);
            trait.deserializeNBT(nbt);
            return trait;
        }

        return null;
    }

    public static <T extends Entity> ITrait<T> createTrait(T holder, ResourceLocation name) {
        return ((ITraitSupplier<T>) TRAIT_SUPPLIERS.get(name)).create(holder);
    }

    public static <T extends Entity> void addTraitToEntity(Entity entity, ResourceLocation name) {
        entity.getCapability(CAPABILITY).ifPresent(cap -> {
            ITrait<T> trait = (ITrait<T>) createTrait(entity, name);
            if (trait != null) {
                cap.addTrait(name, trait);
            }
        });
    }

    public static <T extends Entity> void removeTraitFromEntity(Entity entity, ResourceLocation name) {
        entity.getCapability(CAPABILITY).ifPresent(cap -> cap.removeTrait(name));
    }
}
