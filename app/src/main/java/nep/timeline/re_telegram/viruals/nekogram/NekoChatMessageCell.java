package nep.timeline.re_telegram.viruals.nekogram;

import android.text.SpannableStringBuilder;

import java.lang.reflect.Field;

import nep.timeline.re_telegram.utils.FieldUtils;
import nep.timeline.re_telegram.viruals.ChatMessageCellDefault;

public class NekoChatMessageCell extends ChatMessageCellDefault {
    public NekoChatMessageCell(Object instance) {
        super(instance);
    }

    public SpannableStringBuilder getCurrentTimeString()
    {
        return (SpannableStringBuilder) FieldUtils.getFieldClassOfClass(this.instance, "currentTimeString");
    }

    public void setCurrentTimeString(SpannableStringBuilder currentTimeString)
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
