package chenjunfu2.decoratedpotearly.mixin;

import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ActionResult.class)
public abstract class ActionResultMixin
{
	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void addCustomResult(CallbackInfo ci) {
		// 1. 获取当前 values 数组
		ActionResult[] oldValues = ActionResultAccessor.getValues();
		
		// 2. 创建新枚举常量（需要 Invoker）
		ActionResultAccessor invoker = (ActionResultAccessor)(Object)ActionResult.SUCCESS;
		ActionResult passToDefault = invoker.callConstructor("PASS_TO_DEFAULT_BLOCK_ACTION", oldValues.length);
		
		// 3. 创建新数组并替换
		ActionResult[] newValues = new ActionResult[oldValues.length + 1];
		System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
		newValues[oldValues.length] = passToDefault;
		ActionResultAccessor.setValues(newValues);
	}
}
