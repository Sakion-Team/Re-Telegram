package nep.timeline.re_telegram.viruals;

import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.MethodUtils;

public class MessagesStorage {
    private final Object instance;

    public MessagesStorage(Object instance)
    {
        this.instance = instance;
    }

    public TLRPC.Chat getChat(long chatId)
    {
        Object result = MethodUtils.invokeMethodOfClass(this.instance, AutomationResolver.resolve("MessagesStorage", "getChat", AutomationResolver.ResolverType.Method));
        return new TLRPC.Chat(result);
    }
}
