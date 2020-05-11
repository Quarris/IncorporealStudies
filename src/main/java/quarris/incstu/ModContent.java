package quarris.incstu;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import quarris.incstu.blocks.impl.ResearchTableBlock;
import quarris.incstu.container.ResearchTableContainer;
import quarris.incstu.tiles.ResearchTableTile;
import quarris.qlib.api.registry.registry.BlockRegistry;
import quarris.qlib.api.registry.registry.ContainerRegistry;
import quarris.qlib.api.registry.registry.TileRegistry;

@TileRegistry(IncStu.MODID)
@ContainerRegistry(IncStu.MODID)
@BlockRegistry(IncStu.MODID)
public class ModContent {

    /* ITEMS */

    /* BLOCKS */
    public static final ResearchTableBlock RESEARCH_TABLE = new ResearchTableBlock();

    /* TILES */
    public static final TileEntityType<ResearchTableTile> RESEARCH_TABLE_TILE_TYPE =
            TileEntityType.Builder.create(ResearchTableTile::new, RESEARCH_TABLE).build(null);

    /* CONTAINERS */
    public static final ContainerType<ResearchTableContainer> RESEARCH_TABLE_CONTAINER_TYPE =
            IForgeContainerType.create((id, inv, data) -> new ResearchTableContainer(id, inv, data.readBlockPos()));

    /* ENTITIES */

}
