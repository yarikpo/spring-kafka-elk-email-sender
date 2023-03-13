package ua.clamor1s.emailsender.listener;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ua.clamor1s.emailsender.dto.MessageSaveDto;
import ua.clamor1s.emailsender.service.MessageService;

@Component
@RequiredArgsConstructor
public class MessageListener {

    private final MessageService service;

    @KafkaListener(topics = "${kafka.topic.emailReceiver}")
    public void messageReceived(MessageSaveDto message) {
        service.receiveMessage(message);
    }

}
