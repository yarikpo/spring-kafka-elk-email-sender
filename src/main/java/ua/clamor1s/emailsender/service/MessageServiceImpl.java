package ua.clamor1s.emailsender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;
import ua.clamor1s.emailsender.data.MessageData;
import ua.clamor1s.emailsender.data.MessageStatus;
import ua.clamor1s.emailsender.dto.MessageSaveDto;
import ua.clamor1s.emailsender.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${kafka.topic.emailReceiver}")
    private String topic;

    private final KafkaOperations<String, MessageSaveDto> kafkaOperations;

    private final MessageRepository repository;


    @Override
    public ResponseEntity<?> sendMessage(MessageSaveDto message) {
        sendKafkaMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Message has been successfully sent to Kafka.");
    }

    @Override
    public void receiveMessage(MessageSaveDto message) {
        MessageData data = fromMessageDtoToData(message);
        System.out.println("received: " + data.toString());
        saveMessageToDB(data);
    }

    private void saveMessageToDB(MessageData message) {
        MessageData saved = repository.save(message);
        System.out.println("saved: " + saved.getId());
    }

    private MessageData fromMessageDtoToData(MessageSaveDto message) {
        return MessageData.builder()
                .sender(message.getSender())
                .subject(message.getSubject())
                .content(message.getContent())
                .receivers(new ArrayList<>(message.getReceivers()))
                .status(MessageStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void sendKafkaMessage(MessageSaveDto message) {
        kafkaOperations.send(topic, message.getSender(), message);
    }

}
