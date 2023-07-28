package nep.timeline.re_telegram.virtuals;

import java.util.ArrayList;

import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.FieldUtils;

public class TLRPC {
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

        public long getChannelID()
        {
            return FieldUtils.getFieldLongOfClass(this.instance, this.clazz, AutomationResolver.resolve("TLRPC$Peer", "channel_id", AutomationResolver.ResolverType.Field));
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
            try
            {
                if (ClientChecker.check(ClientChecker.ClientType.Yukigram))
                    return FieldUtils.getFieldFromMultiName(this.clazz, AutomationResolver.resolve("TLRPC$Message", "id", AutomationResolver.ResolverType.Field), int.class).getInt(this.instance);
                else
                    return FieldUtils.getFieldIntOfClass(this.instance, this.clazz, "id");
            }
            catch (IllegalAccessException e)
            {
                Utils.log(e);
                e.printStackTrace();
            }
            return -1;
        }

        public Peer getPeerID()
        {
            try
            {
                if (ClientChecker.check(ClientChecker.ClientType.Yukigram))
                    return new Peer(FieldUtils.getFieldFromMultiName(this.clazz, AutomationResolver.resolve("TLRPC$Message", "peer_id", AutomationResolver.ResolverType.Field), AutomationResolver.resolve("org.telegram.tgnet.TLRPC$Peer")).get(this.instance));
                else
                    return new Peer(FieldUtils.getFieldClassOfClass(this.instance, this.clazz, "peer_id"));
            }
            catch (IllegalAccessException e)
            {
                Utils.log(e);
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
    public static class TL_updateDeleteScheduledMessages {
        private final Object instance;

        public TL_updateDeleteScheduledMessages(Object instance)
        {
            this.instance = instance;
        }

        public ArrayList<Integer> getMessages()
        {
            return Utils.castList(FieldUtils.getFieldClassOfClass(this.instance, "messages"), Integer.class);
        }
    }
*/
    public static class TL_updateDeleteChannelMessages {
        private final Object instance;

        public TL_updateDeleteChannelMessages(Object instance)
        {
            this.instance = instance;
        }

        public long getChannelID()
        {
            try
            {
                if (ClientChecker.check(ClientChecker.ClientType.Yukigram))
                    return FieldUtils.getFieldFromMultiName(this.instance.getClass(), AutomationResolver.resolve("TLRPC$TL_updateDeleteChannelMessages", "channel_id", AutomationResolver.ResolverType.Field), long.class).getLong(this.instance);
                else
                    return FieldUtils.getFieldLongOfClass(this.instance, "channel_id");
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            return Long.MIN_VALUE;
        }

        public ArrayList<Integer> getMessages()
        {
            try
            {
                if (ClientChecker.check(ClientChecker.ClientType.Yukigram))
                    return Utils.castList(FieldUtils.getFieldFromMultiName(this.instance.getClass(), AutomationResolver.resolve("TLRPC$TL_updateDeleteChannelMessages", "id", AutomationResolver.ResolverType.Field), ArrayList.class).get(this.instance), Integer.class);
                else
                    return Utils.castList(FieldUtils.getFieldClassOfClass(this.instance, "messages"), Integer.class);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            return null;
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
            try
            {
                if (ClientChecker.check(ClientChecker.ClientType.Yukigram))
                    return Utils.castList(FieldUtils.getFieldFromMultiName(this.instance.getClass(), AutomationResolver.resolve("TLRPC$TL_updateDeleteMessages", "id", AutomationResolver.ResolverType.Field), ArrayList.class).get(this.instance), Integer.class);
                else
                    return Utils.castList(FieldUtils.getFieldClassOfClass(this.instance, "messages"), Integer.class);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
