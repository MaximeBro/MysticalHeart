package fr.universecorp.mysticalheart.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticalheart.MysticalHeart;
import fr.universecorp.mysticalheart.config.Configs;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class PlayerHUD implements HudRenderCallback {

    final Identifier TEXTURE = new Identifier(MysticalHeart.MODID, "textures/hud/background.png");

    private PlayerEntity playerEntity;
    private LivingEntity lastEntity;

    private int lastTimeToRender = 0;


    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {

        if(MinecraftClient.getInstance().player.isPlayer()) {
            this.playerEntity = (PlayerEntity) MinecraftClient.getInstance().player;
        }

        Vec3d playerPos = playerEntity.getEyePos();
        Vec3d endPos = playerPos.add(playerEntity.getRotationVector().multiply(Configs.HUD_DISTANCE));
        Box box = playerEntity.getBoundingBox().expand(Configs.HUD_DISTANCE);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(playerEntity, playerPos, endPos, box, entity -> entity.isLiving(), 0);

        if(playerEntity != null && entityHitResult != null) {
            if(entityHitResult.getEntity() instanceof LivingEntity entity) {
                this.lastEntity = entity;
            }
        }


        if(this.lastEntity != null) {
            this.lastTimeToRender++;

            if(this.lastTimeToRender >= 600) {
                this.lastEntity = null;
                this.lastTimeToRender = 0;
            }
        }



        if(this.lastTimeToRender > 0) {
            this.renderHud(matrixStack, this.lastEntity, playerEntity);
        } else {
            if (playerEntity != null && entityHitResult != null) {
                Entity entity = entityHitResult.getEntity();
                this.renderHud(matrixStack, entity, playerEntity);
            }
        }


    }

    public void renderHud(MatrixStack matrixStack, Entity entity, PlayerEntity player) {
        if(entity instanceof LivingEntity livingEntity) {

            if(!Configs.HUD_DISPLAYED) {
                return;
            }

            if(this.lastEntity == null || this.lastEntity != entity) {
                this.lastEntity = (LivingEntity) entity;
            }

            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100), player);

            int maxHealth = (int) livingEntity.getMaxHealth();
            int currentHealth = (int) livingEntity.getHealth();
            int armor = livingEntity.getArmor();
            int ratio = currentHealth * 64 / maxHealth;

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);

            // Background
            DrawableHelper.drawTexture(matrixStack, 5, 5, 0, 0, 150, 67, 256, 256);

            // Health bar
            DrawableHelper.drawTexture(matrixStack, 71, 52, 66, 67, ratio, 11, 256, 256);

            // Armor
            if(armor > 0) {
                DrawableHelper.drawTexture(matrixStack, 74, 28, 41, 68, 9, 9, 256, 256);
            }

            // Texts
            // - Name -
            TextRenderer textRenderer = MinecraftClient.getInstance().inGameHud.getTextRenderer();
            Text entityName = livingEntity.getName();
            textRenderer.draw(matrixStack, entityName,
                    64 + 32 - (int)(textRenderer.getWidth(entityName) / 4) - 5, 16, Configs.NAME_COLOR);

            // - Health -
            int posX = currentHealth > 99 ? 88 : 91;
            Text healthDisplay = Text.of(currentHealth < 10000 ? (currentHealth + "/" + maxHealth) : (currentHealth/100 + "K/" + maxHealth/100 + "K"));
            textRenderer.draw(matrixStack, healthDisplay,
                    posX, 39, Configs.HEALTH_COLOR);

            // - Armor -
            if(armor > 0) {
                Text armorDisplay = Text.of(armor + "");
                textRenderer.draw(matrixStack, armorDisplay,
                        92, 29, Configs.ARMOR_COLOR);
            }


            // Entity 3D rendering
            float livingH = this.lastEntity.getHeight();
            int h = 20;
            int posY = 54;
            if(livingH*10 > 20)  { h = 18; }
            if(livingH*10 <= 20) { h = 20; }
            if(livingH*10 >= 15 && livingH*10 < 20) { h = 23; }
            if(livingH*10 < 15)  { h = 30; }

            if(h < 20) { posY = 58; }

            this.drawEntity(31, posY + 8, h, this.lastEntity);
        }
    }

    public void drawEntity(int x, int y, int size, LivingEntity entity) {
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate((float)x, (float)y, 1050.0F);
        matrixStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0F, 0.0F, 1000.0F);
        matrixStack2.scale((float)size, (float)size, (float)size);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(-5.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        matrixStack2.multiply(quaternionf);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0F + 20.0F;
        entity.setYaw(170.0F);
        entity.setPitch(-5.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternionf2.conjugate();
        entityRenderDispatcher.setRotation(quaternionf2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack2, immediate, 15728880);
        });
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

}
