package com.aetherteam.aether.world.structurepiece.golddungeon;


import com.aetherteam.aether.Aether;
import com.aetherteam.aether.blockentity.TreasureChestBlockEntity;
import com.aetherteam.aether.loot.AetherLoot;
import com.aetherteam.aether.world.structurepiece.AetherStructurePieceTypes;
import com.aetherteam.aether.world.structurepiece.AetherTemplateStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * A room inside the island. This should contain the Sun Spirit.
 */
public class GoldBossRoom extends GoldDungeonPiece {
    public GoldBossRoom(StructureTemplateManager manager, String name, BlockPos pos, Rotation rotation, Holder<StructureProcessorList> processors) {
        super(AetherStructurePieceTypes.GOLD_BOSS_ROOM.get(), manager, name, AetherTemplateStructurePiece.makeSettingsWithPivot(GoldBossRoom.makeSettings(), manager, GoldDungeonPiece.makeLocation(name), rotation), pos, processors);
    }

    public GoldBossRoom(StructurePieceSerializationContext context, CompoundTag tag) {
        super(AetherStructurePieceTypes.GOLD_BOSS_ROOM.get(), context.registryAccess(), tag, context.structureTemplateManager(), resourceLocation -> GoldBossRoom.makeSettings());
    }

    private static StructurePlaceSettings makeSettings() {
        return new StructurePlaceSettings()
                .setFinalizeEntities(true);
    }

    @Override
    protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
        if (name.equals("Treasure Chest")) {
            BlockPos chest = pos.below();
            BlockEntity entity = level.getBlockEntity(chest);
            if (entity instanceof RandomizableContainerBlockEntity container) {
                container.setLootTable(AetherLoot.GOLD_DUNGEON_REWARD, random.nextLong());
            }
            TreasureChestBlockEntity.setDungeonType(level, chest, ResourceLocation.fromNamespaceAndPath(Aether.MODID, "gold"));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}
