package nep.timeline.re_telegram.structs;

import java.util.ArrayList;
import java.util.List;

public class DeletedMessageInfo {
    private final int selectedAccount;
    private final List<Integer> messageIds;

    public DeletedMessageInfo(int selectedAccount, List<Integer> messageIds)
    {
        this.selectedAccount = selectedAccount;
        this.messageIds = messageIds;
    }

    public DeletedMessageInfo(int selectedAccount, int messageId)
    {
        this.selectedAccount = selectedAccount;
        this.messageIds = new ArrayList<>();
        this.messageIds.add(messageId);
    }

    public int getSelectedAccount() {
        return this.selectedAccount;
    }

    public List<Integer> getMessageIds() {
        return this.messageIds;
    }

    public void insertMessageIds(ArrayList<Integer> messageIds) {
        this.messageIds.addAll(messageIds);
    }

    public void insertMessageId(Integer messageId) {
        this.messageIds.add(messageId);
    }
}
