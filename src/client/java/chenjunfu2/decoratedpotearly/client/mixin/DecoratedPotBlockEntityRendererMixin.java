package chenjunfu2.decoratedpotearly.client.mixin;

import chenjunfu2.decoratedpotearly.api.DecoratedPotBlockEntityHelper;
import chenjunfu2.decoratedpotearly.data.DecoratedPotWobbleType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlockEntityRenderer.class)
public class DecoratedPotBlockEntityRendererMixin
{
	@Inject
	(
		method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
		at = @At
		(
			value = "INVOKE",
			target = "Lnet/minecraft/client/util/SpriteIdentifier;getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;)Lnet/minecraft/client/render/VertexConsumer;",
			shift = At.Shift.BEFORE
		)
	)
	void renderInject(DecoratedPotBlockEntity decoratedPotBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci)
	{
		DecoratedPotWobbleType wobbleType = ((DecoratedPotBlockEntityHelper)decoratedPotBlockEntity).decoratedpotearly$getLastWobbleType();
		if (wobbleType != null && decoratedPotBlockEntity.getWorld() != null)
		{
			float g = ((float)(decoratedPotBlockEntity.getWorld().getTime() - ((DecoratedPotBlockEntityHelper)decoratedPotBlockEntity).decoratedpotearly$getLastWobbleTime()) + f) / wobbleType.lengthInTicks;
			if (g >= 0.0F && g <= 1.0F)
			{
				if (wobbleType == DecoratedPotWobbleType.POSITIVE)
				{
					float h = 0.015625F;
					float k = g * (float) (Math.PI * 2);
					float l = -1.5F * (MathHelper.cos(k) + 0.5F) * MathHelper.sin(k / 2.0F);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(l * 0.015625F), 0.5F, 0.0F, 0.5F);
					float m = MathHelper.sin(k);
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(m * 0.015625F), 0.5F, 0.0F, 0.5F);
				}
				else
				{
					float h = MathHelper.sin(-g * 3.0F * (float) Math.PI) * 0.125F;
					float k = 1.0F - g;
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(h * k), 0.5F, 0.0F, 0.5F);
				}
			}
		}
	}

}
