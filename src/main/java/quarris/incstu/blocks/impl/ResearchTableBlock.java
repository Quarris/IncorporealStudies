package quarris.incstu.blocks.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import quarris.incstu.ModContent;
import quarris.incstu.blocks.BaseBlock;
import quarris.incstu.tiles.ResearchTableTile;

import javax.annotation.Nullable;

public class ResearchTableBlock extends BaseBlock {

    public ResearchTableBlock() {
        super(Block.Properties.create(Material.WOOD));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isRemote()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof ResearchTableTile) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModContent.RESEARCH_TABLE_TILE_TYPE.create();
    }
}
