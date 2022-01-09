package net.creativityshark.pyrotastic.common.entities;

import net.creativityshark.pyrotastic.PyrotasticMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PyrotasticEntities {

    public static final EntityType FIREWORKS_CRATE_ENTITY = registerEntity("fireworks_crate",
            FabricEntityTypeBuilder.<FireworksCrateEntity>create(SpawnGroup.MISC, FireworksCrateEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).fireImmune().build());

    public static EntityType registerEntity(String name, EntityType entityType) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(PyrotasticMod.MOD_ID, name), entityType);
    }

    public static void registerModEntities() {
        PyrotasticMod.LOGGER.info("registering " + PyrotasticMod.MOD_ID + " entities");
    }
}
