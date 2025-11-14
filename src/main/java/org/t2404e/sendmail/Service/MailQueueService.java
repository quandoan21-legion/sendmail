package org.t2404e.sendmail.Service;

import org.springframework.stereotype.Service;
import org.t2404e.sendmail.DTO.MailCampaignRequest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MailQueueService {
    private final Queue<MailCampaignRequest> queue = new ConcurrentLinkedQueue<>();

    public void addMail(MailCampaignRequest request) {
        queue.offer(request);
        System.out.println("Added mail to queue for: " + request.getRecipients());
    }

    public MailCampaignRequest getNextMail() {
        return queue.poll(); // lấy và xóa phần tử đầu tiên
    }

    public boolean hasMail() {
        return !queue.isEmpty();
    }
}
