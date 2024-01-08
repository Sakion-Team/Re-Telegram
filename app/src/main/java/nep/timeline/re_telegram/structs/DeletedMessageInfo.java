package nep.timeline.re_telegram.structs;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeletedMessageInfo {
    public static final long NOT_CHANNEL = 0;

    private final int selectedAccount;
    private final long channelID;
    private final CopyOnWriteArrayList<Integer> messageIds;

    public DeletedMessageInfo(int selectedAccount, long channelID, CopyOnWriteArrayList<Integer> messageIds)
    {
        this.selectedAccount = selectedAccount;
        this.channelID = channelID;
        this.messageIds = messageIds;
    }

    public DeletedMessageInfo(int selectedAccount, long channelID, int messageId)
    {
        this.selectedAccount = selectedAccount;
        this.channelID = channelID;
        this.messageIds = new CopyOnWriteArrayList<>();
        this.messageIds.add(messageId);
    }

    public int getSelectedAccount() {
        return this.selectedAccount;
    }

    public long getChannelID()
    {
        return this.channelID;
    }

    public boolean isNotChannel()
    {
        return this.channelID == NOT_CHANNEL;
    }

    public CopyOnWriteArrayList<Integer> getMessageIds() {
        return this.messageIds;
    }

    public void insertMessageIds(CopyOnWriteArrayList<Integer> messageIds) {
        this.messageIds.addAll(messageIds);
    }

    public void insertMessageId(Integer messageId) {
        this.messageIds.add(messageId);
    }

    public void removeMessageId(Integer messageId) {
        this.messageIds.remove(messageId);
    }
}