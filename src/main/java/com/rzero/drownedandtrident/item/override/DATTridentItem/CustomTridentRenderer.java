package com.rzero.drownedandtrident.item.override.DATTridentItem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

// CustomTridentRenderer.java
public class CustomTridentRenderer extends BlockEntityWithoutLevelRenderer {

    // 你的实体材质路径 (不是 item 里的 2D 图标，是 64x64 的展开图)
    public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/trident.png");
    private TridentModel model;

    public CustomTridentRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (model == null) {
            // 加载原版三叉戟的几何形状
            model = new TridentModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.TRIDENT));
        }

        poseStack.pushPose();

        // 关键：这里不需要再根据 context 判断是 GUI 还是手持了
        // 因为你的 JSON (separate_transforms) 已经帮你分流了
        // 能运行到这里的，一定是在 3D 场景下 (手持/第三人称)

        poseStack.scale(1.0F, -1.0F, -1.0F); // 修正倒置

        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(TEXTURE), false, stack.hasFoil());
        model.renderToBuffer(poseStack, consumer, light, overlay);

        poseStack.popPose();
    }
}