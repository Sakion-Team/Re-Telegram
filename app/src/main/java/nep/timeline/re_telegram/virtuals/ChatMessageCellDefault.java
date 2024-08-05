package nep.timeline.re_telegram.virtuals;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.utils.FieldUtils;

public class ChatMessageCellDefault {
    protected final Object instance;

    protected ChatMessageCellDefault(Object instance)
    {
        this.instance = instance;
    }

    public int getTimeTextWidth()
    {
        return FieldUtils.getFieldIntOfClass(this.instance, "timeTextWidth");
    }

    public int getTimeWidth()
    {
        return FieldUtils.getFieldIntOfClass(this.instance, "timeWidth");
    }

    public void setTimeTextWidth(int width)
    {
        try
        {
            XposedHelpers.setIntField(this.instance, "timeTextWidth", width);
            //Field timeTextWidthField = FieldUtils.getFieldOfClass(this.instance, "timeTextWidth");
            //if (timeTextWidthField != null)
            //    timeTextWidthField.setInt(this.instance, width);
            //else
            //    throw new NullPointerException("Not found timeTextWidth in " + this.instance.getClass().getName());
        }
        catch (Throwable e)
        {
            Utils.log(e);
        }
    }

    public void setTimeWidth(int width)
    {
        try
        {
            XposedHelpers.setIntField(this.instance, "timeWidth", width);
            /*Field timeWidthField = FieldUtils.getFieldOfClass(this.instance, "timeWidth");
            if (timeWidthField != null)
                timeWidthField.setInt(this.instance, width);
            else
                throw new NullPointerException("Not found timeWidth in " + this.instance.getClass().getName());*/
        }
        catch (Throwable e)
        {
            Utils.log(e);
        }
    }
}
