package org.t2404e.sendmail.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendEmailService {
    private final List<String> sentEmails = new ArrayList<>();

    // Thêm email đã gửi
    public void addEmail(String email) {
        if (!sentEmails.contains(email)) {
            sentEmails.add(email);
        }
    }

    // Lấy danh sách email đã gửi
    public List<String> getSentEmails() {
        return sentEmails;
    }
}
