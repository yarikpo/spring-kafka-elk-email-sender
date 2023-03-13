package ua.clamor1s.emailsender.service;

import org.springframework.http.ResponseEntity;
import ua.clamor1s.emailsender.dto.MessageSaveDto;

public interface MessageService {

    ResponseEntity<?> sendMessage(MessageSaveDto message);

    void receiveMessage(MessageSaveDto message);

}
