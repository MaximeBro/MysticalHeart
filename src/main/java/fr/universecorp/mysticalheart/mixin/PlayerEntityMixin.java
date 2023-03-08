package fr.universecorp.mysticalheart.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    PlayerEntity playerEntity;

    @Inject(method = "tick", at = @At(value = "RETURN"), cancellable = true)
    public void tick(CallbackInfo info) {

        playerEntity = ((PlayerEntity) (Object) this);

        Vec3d playerPos = playerEntity.getEyePos();
        Vec3d endPos = playerPos.add(playerEntity.getRotationVector().multiply(10));
        Box box = playerEntity.getBoundingBox().expand(10);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(playerEntity, playerPos, endPos, box, entity -> entity.isLiving(), 50);
        if (entityHitResult != null) {
            Entity entity = entityHitResult.getEntity();

            if(entity instanceof LivingEntity livingEntity) {
                this.renderHpBar(livingEntity);
            }
        }
    }

    public void renderHpBar(LivingEntity entity) {
        float maxHealth = entity.getMaxHealth();
        float currentHealth = entity.getHealth();

        this.playerEntity.sendMessage(Text.of("HP : " + currentHealth + "/" + maxHealth));
    }

}
