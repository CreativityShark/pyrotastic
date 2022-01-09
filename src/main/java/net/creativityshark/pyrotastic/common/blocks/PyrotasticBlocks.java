package net.creativityshark.pyrotastic.common.blocks;

import net.creativityshark.pyrotastic.PyrotasticMod;
import net.creativityshark.pyrotastic.common.blocks.entities.FireworksCrateBlockEntity;
import net.creativityshark.pyrotastic.common.items.FireworksCrateBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PyrotasticBlocks {

    public static final Block FIREWORKS_CRATE = registerBlock("fireworks_crate",
            new FireworksCrateBlock(FabricBlockSettings.of(Material.WOOD).strength(0.5f, 0.5f)),
                    ItemGroup.REDSTONE);

    public static final BlockEntityType<FireworksCrateBlockEntity> FIREWORKS_CRATE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PyrotasticMod.MOD_ID, "fireworks_crate"), FabricBlockEntityTypeBuilder.create(FireworksCrateBlockEntity::new, FIREWORKS_CRATE).build(null));

    private static Block registerBlock(String name, Block block, ItemGroup itemGroup) {
        registerBlockItem(name, block, itemGroup);
        return Registry.register(Registry.BLOCK, new Identifier(PyrotasticMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, ItemGroup itemGroup) {
        if(name.equals("fireworks_crate")) {
            Registry.register(Registry.ITEM, new Identifier(PyrotasticMod.MOD_ID, name),
                    new FireworksCrateBlockItem(block, new FabricItemSettings().group(itemGroup).maxCount(1)));
        } else {
            Registry.register(Registry.ITEM, new Identifier(PyrotasticMod.MOD_ID, name),
                    new BlockItem(block, new FabricItemSettings().group(itemGroup)));
        }
    }

    public static void registerModBlocks() {
        PyrotasticMod.LOGGER.info("registering " + PyrotasticMod.MOD_ID + " blocks");
    }
}
