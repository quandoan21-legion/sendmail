package org.t2404e.sendmail.Service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class SaveEmailService {
    private final List<String> savedEmails = new ArrayList<>();

    // Thêm email đã gửi
    public void addEmail(String email) {
        if (!savedEmails.contains(email)) {
            savedEmails.add(email);
        }
    }


}
