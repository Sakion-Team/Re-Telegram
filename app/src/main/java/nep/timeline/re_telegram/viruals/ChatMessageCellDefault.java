package nep.timeline.re_telegram.viruals;

import java.lang.reflect.Field;

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
            Field timeTextWidthField = FieldUtils.getFieldOfClass(this.instance, "timeTextWidth");
            if (timeTextWidthField != null)
                timeTextWidthField.setInt(this.instance, width);
            else
                throw new NullPointerException("Not found timeTextWidth in " + this.instance.getClass().getName());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void setTimeWidth(int width)
    {
        try
        {
            Field timeWidthField = FieldUtils.getFieldOfClass(this.instance, "timeWidth");
            if (timeWidthField != null)
                timeWidthField.setInt(this.instance, width);
            else
                throw new NullPointerException("Not found timeWidth in " + this.instance.getClass().getName());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
