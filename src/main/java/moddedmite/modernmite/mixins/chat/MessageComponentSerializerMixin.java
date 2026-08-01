package moddedmite.modernmite.mixins.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import moddedmite.modernmite.internal.chat.ChatLinkComponentAccess;
import net.minecraft.ChatMessageComponent;
import net.minecraft.MessageComponentSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(MessageComponentSerializer.class)
public class MessageComponentSerializerMixin {

    @Inject(method = "deserializeComponent", at = @At("RETURN"))
    private void modernMite$readClickEvent(JsonElement json, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ChatMessageComponent> cir) {
        if (!json.isJsonObject()) {
            return;
        }
        JsonObject object = json.getAsJsonObject();
        ChatMessageComponent component = cir.getReturnValue();
        JsonElement clickEventElement = object.has("click_event")
                ? object.get("click_event")
                : object.get("clickEvent");
        if (clickEventElement != null && clickEventElement.isJsonObject()) {
            JsonObject clickEvent = clickEventElement.getAsJsonObject();
            JsonElement action = clickEvent.get("action");
            JsonElement value = clickEvent.has("url") ? clickEvent.get("url") : clickEvent.get("value");
            if (action != null && action.isJsonPrimitive() && "open_url".equals(action.getAsString())
                    && value != null && value.isJsonPrimitive()) {
                ((ChatLinkComponentAccess) component).modernMite$setOpenUrl(value.getAsString());
            }
        }
        this.modernMite$readExtra(object, type, context, component);
    }

    @Unique
    private void modernMite$readExtra(JsonObject object, Type type, JsonDeserializationContext context, ChatMessageComponent component) {
        JsonElement extra = object.get("extra");
        if (extra == null || !extra.isJsonArray()) {
            return;
        }
        for (JsonElement childJson : extra.getAsJsonArray()) {
            if (childJson.isJsonPrimitive()) {
                component.appendComponent(ChatMessageComponent.createFromText(childJson.getAsString()));
            } else if (childJson.isJsonObject()) {
                component.appendComponent(((MessageComponentSerializer) (Object) this)
                        .deserializeComponent(childJson, type, context));
            }
        }
    }

    @Inject(method = "serializeComponent", at = @At("RETURN"))
    private void modernMite$writeClickEvent(ChatMessageComponent component, Type type, JsonSerializationContext context, CallbackInfoReturnable<JsonElement> cir) {
        String url = ((ChatLinkComponentAccess) component).modernMite$getOpenUrl();
        if (url == null || !cir.getReturnValue().isJsonObject()) {
            return;
        }
        JsonObject clickEvent = new JsonObject();
        clickEvent.addProperty("action", "open_url");
        clickEvent.addProperty("url", url);
        cir.getReturnValue().getAsJsonObject().add("click_event", clickEvent);
    }

    @Redirect(method = "serializeComponentChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/ChatMessageComponent;getText()Ljava/lang/String;"))
    private String modernMite$preserveChildComponent(ChatMessageComponent child) {
        return null;
    }
}
