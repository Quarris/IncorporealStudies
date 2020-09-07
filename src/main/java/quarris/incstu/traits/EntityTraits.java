package quarris.incstu.traits;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityTraits implements ITraitHolder {

    private final Entity entity;
    private HashMap<ResourceLocation, ITrait<?>> traits = new HashMap<>();


    private LazyOptional<ITraitHolder> holder = LazyOptional.of(() -> this);

    public EntityTraits(Entity entity) {
        this.entity = entity;
    }

    @Override
    public <T extends Entity> void addTrait(ResourceLocation name, ITrait<T> trait) {
        if (trait.canApplyTo(this.entity)) {
            this.traits.put(name, trait);
            trait.onApplied((T) this.entity);
        }
    }

    @Override
    public <T extends Entity> void removeTrait(ResourceLocation name) {
        if (this.hasTrait(name)) {
            ITrait<T> trait = (ITrait<T>) this.traits.get(name);
            trait.onRemoved((T) this.entity);
            this.traits.remove(name);
        }
    }

    @Override
    public boolean hasTrait(ResourceLocation name) {
        return this.traits.containsKey(name);
    }

    @Override
    public Set<ResourceLocation> activeTraitNames() {
        return this.traits.keySet();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == TraitHolderCapability.INSTANCE)
            return holder.cast();

        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT traitList = new ListNBT();
        for (Map.Entry<ResourceLocation, ITrait<?>> entry : this.traits.entrySet()) {
            CompoundNBT traitNBT = entry.getValue().serializeNBT();
            traitNBT.putString("Name", entry.getKey().toString());
            traitList.add(traitNBT);
        }
        if (!traitList.isEmpty())
            nbt.put("Traits", traitList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("Traits")) {
            this.traits.clear();
            ListNBT traits = nbt.getList("Traits", Constants.NBT.TAG_COMPOUND);
            for (int i = 0, traitsSize = traits.size(); i < traitsSize; i++) {
                CompoundNBT traitNBT = (CompoundNBT) traits.get(i);
                ResourceLocation name = new ResourceLocation(traitNBT.getString("Name"));
                ITrait trait = TraitSystem.createTrait(traitNBT);
                this.traits.put(name, trait);
            }
        }
    }
}