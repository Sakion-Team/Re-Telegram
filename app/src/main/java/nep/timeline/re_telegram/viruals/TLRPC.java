package nep.timeline.re_telegram.viruals;

import java.util.ArrayList;

import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.FieldUtils;

public class TLRPC {
    public static class Chat { // Nekogram is eL0
        private final Object instance;

        public Chat(Object instance)
        {
            this.instance = instance;
        }

        public long getId()
        {
            return FieldUtils.getFieldLongOfClass(this.instance, "id");
        }

        public String getTitle()
        {
            return (String) FieldUtils.getFieldClassOfClass(this.instance, "title");
        }
    }

    public static class Peer {
        private final Object instance;
        private final Class<?> clazz;

        public Peer(Object instance)
        {
            this.instance = instance;
            if (!instance.getClass().getName().equals(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$Peer")))
            {
                Class<?> clazz = instance.getClass().getSuperclass();
                if (!clazz.getName().equals(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$Peer")))
                    this.clazz = clazz.getSuperclass();
                else
                    this.clazz = clazz;
            }
            else
            {
                this.clazz = instance.getClass();
            }
        }

        public long getUserID()
        {
            return FieldUtils.getFieldLongOfClass(this.instance, this.clazz, "user_id");
        }

        public long getChatID()
        {
            return FieldUtils.getFieldLongOfClass(this.instance, this.clazz, "chat_id");
        }

        public long getChannelID()
        {
            return FieldUtils.getFieldLongOfClass(this.instance, this.clazz, "channel_id");
        }
    }

    public static class Message {
        private final Object instance;
        private final Class<?> clazz;

        public Message(Object instance)
        {
            this.instance = instance;
            if (!instance.getClass().getName().equals(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$Message")))
            {
                Class<?> clazz = instance.getClass().getSuperclass();
                if (!clazz.getName().equals(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$Message")))
                    this.clazz = clazz.getSuperclass();
                else
                    this.clazz = clazz;
            }
            else
            {
                this.clazz = instance.getClass();
            }
        }

        public int getID()
        {
            return FieldUtils.getFieldIntOfClass(this.instance, this.clazz, "id");
        }

        public Peer getPeerID()
        {
            return new Peer(FieldUtils.getFieldClassOfClass(this.instance, this.clazz, "peer_id"));
        }
    }

    public static class TL_updateDeleteChannelMessages {
        private final Object instance;

        public TL_updateDeleteChannelMessages(Object instance)
        {
            this.instance = instance;
        }

        public long getChannelID()
        {
            return FieldUtils.getFieldLongOfClass(this.instance, "channel_id");
        }

        public ArrayList<Integer> getMessages()
        {
            return Utils.castList(FieldUtils.getFieldClassOfClass(this.instance, "messages"), Integer.class);
        }
    }

    public static class TL_updateDeleteMessages {
        private final Object instance;

        public TL_updateDeleteMessages(Object instance)
        {
            this.instance = instance;
        }

        public ArrayList<Integer> getMessages()
        {
            return Utils.castList(FieldUtils.getFieldClassOfClass(this.instance, "messages"), Integer.class);
        }
    }
}
