package nep.timeline.re_telegram.virtuals.nekogram;

import android.text.SpannableStringBuilder;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.utils.FieldUtils;
import nep.timeline.re_telegram.virtuals.ChatMessageCellDefault;

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
            XposedHelpers.setObjectField(this.instance, "currentTimeString", currentTimeString);
            /*Field currentTimeStringField = FieldUtils.getFieldOfClass(this.instance, "currentTimeString");
            if (currentTimeStringField != null)
                currentTimeStringField.set(this.instance, currentTimeString);
            else
                throw new NullPointerException("Not found currentTimeString in " + this.instance.getClass().getName());*/
        }
        catch (Throwable e)
        {
            Utils.log(e);
        }
    }
}
