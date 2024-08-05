package nep.timeline.re_telegram.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class DeletedMessageInfo {
    public static final long NOT_CHANNEL = 0;

    private final int selectedAccount;
    private final long channelID;
    private final ArrayList<Integer> messageIds;

    public DeletedMessageInfo(int selectedAccount, long channelID, ArrayList<Integer> messageIds)
    {
        this.selectedAccount = selectedAccount;
        this.channelID = channelID;
        this.messageIds = messageIds;
    }

    public DeletedMessageInfo(int selectedAccount, long channelID, int messageId)
    {
        this.selectedAccount = selectedAccount;
        this.channelID = channelID;
        this.messageIds = new ArrayList<>();
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

    public ArrayList<Integer> getMessageIds() {
        return this.messageIds;
    }

    public void insertMessageIds(ArrayList<Integer> messageIds) {
        this.messageIds.addAll(messageIds);
    }

    public void insertMessageId(Integer messageId) {
        this.messageIds.add(messageId);
    }

    public void removeMessageId(Integer messageId) {
        this.messageIds.remove(messageId);
    }
}