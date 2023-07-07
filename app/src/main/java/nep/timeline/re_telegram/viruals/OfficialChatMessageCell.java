package nep.timeline.re_telegram.viruals;

import java.lang.reflect.Field;

import nep.timeline.re_telegram.utils.FieldUtils;

public class OfficialChatMessageCell extends ChatMessageCellDefault {
    public OfficialChatMessageCell(Object instance) {
        super(instance);
    }

    public CharSequence getCurrentTimeString()
    {
        return (CharSequence) FieldUtils.getFieldClassOfClass(this.instance, "currentTimeString");
    }

    public void setCurrentTimeString(CharSequence currentTimeString)
    {
        try
        {
            Field currentTimeStringField = FieldUtils.getFieldOfClass(this.instance, "currentTimeString");
            if (currentTimeStringField != null)
                currentTimeStringField.set(this.instance, currentTimeString);
            else
                throw new NullPointerException("Not found currentTimeString in " + this.instance.getClass().getName());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
