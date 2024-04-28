package nep.timeline.re_telegram.virtuals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class MessageObject {
    private final Object instance;

    public MessageObject(Object instance)
    {
        this.instance = instance;
    }

    public TLRPC.Message getMessageOwner()
    {
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : this.instance.getClass().getDeclaredFields())
            if (declaredField.getName().equals(AutomationResolver.resolve("MessageObject", "messageOwner", AutomationResolver.ResolverType.Field)))
                fields.add(declaredField);

        if (!fields.isEmpty()) {
            try
            {
                Field messageOwnerField = null;
                for (Field field : fields) {
                    if (field.getType().getName().equals(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$Message")))
                    {
                        messageOwnerField = field;
                    }
                }
                if (messageOwnerField != null) {
                    Object messageOwner = messageOwnerField.get(this.instance);
                    if (messageOwner == null)
                        return null;
                    return new TLRPC.Message(messageOwner);
                }
                else
                    Utils.log("Not found messageOwner field in MessageObject's fields, " + Utils.issue);
            }
            catch (IllegalAccessException e)
            {
                Utils.log(e);
            }
        }
        else
            Utils.log("Not found messageOwner field in MessageObject, " + Utils.issue);

        return null;
    }
}
