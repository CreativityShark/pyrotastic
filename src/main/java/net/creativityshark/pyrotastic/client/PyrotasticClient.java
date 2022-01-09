package net.creativityshark.pyrotastic.client;

import net.creativityshark.pyrotastic.client.entities.FireworksCrateEntityRenderer;
import net.creativityshark.pyrotastic.common.entities.PyrotasticEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PyrotasticClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(PyrotasticEntities.FIREWORKS_CRATE_ENTITY, FireworksCrateEntityRenderer::new);
    }
}
