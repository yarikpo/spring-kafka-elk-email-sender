package ua.clamor1s.emailsender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;
import ua.clamor1s.emailsender.dto.MessageSaveDto;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${kafka.topic.emailReceiver}")
    private String topic;

    private final KafkaOperations<String, MessageSaveDto> kafkaOperations;


    @Override
    public ResponseEntity<?> sendMessage(MessageSaveDto message) {
        sendKafkaMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Message has been successfully sent to Kafka.");
    }

    @Override
    public void receiveMessage(MessageSaveDto message) {
        System.out.println("received: " + message.toString());
    }

    private void sendKafkaMessage(MessageSaveDto message) {
        kafkaOperations.send(topic, message.getSender(), message);
    }

}
