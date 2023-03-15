package ua.clamor1s.emailsender.service;


import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.clamor1s.emailsender.data.MessageData;
import ua.clamor1s.emailsender.data.MessageStatus;
import ua.clamor1s.emailsender.dto.MessageSaveDto;
import ua.clamor1s.emailsender.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${kafka.topic.emailReceiver}")
    private String topic;

    private final KafkaOperations<String, MessageSaveDto> kafkaOperations;

    private final MessageRepository repository;

    private final ElasticsearchOperations elasticsearchOperations;

    private final Session session;
    private final Dotenv env;


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
        sendMessagesWithStatus(MessageStatus.PENDING);
    }

    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void scheduledResendErrorMessages() {
        System.out.println("Scheduled function.");
        sendMessagesWithStatus(MessageStatus.ERROR);
    }

    private void sendMessagesWithStatus(MessageStatus status) {
        Criteria criteria = new Criteria("status").is(status);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<MessageData> hits = elasticsearchOperations.search(query, MessageData.class);
        List<MessageData> messages = (List<MessageData>) SearchHitSupport.unwrapSearchHits(hits);

        messages.stream()
                .forEach(this::sendEmailMessage);

    }

    public void sendEmailMessage(MessageData messageData) {
        String[] recipients = new String[messageData.getReceivers().size()];
        recipients = messageData.getReceivers().toArray(recipients);
        String subject = messageData.getSubject() == null ? "" : messageData.getSubject();
        String text = messageData.getContent() == null ? "" : messageData.getContent();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(env.get("EMAIL_ADDRESS")));
            message.setSubject(subject);
            message.setText(text);

            for (String recipient : recipients) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipient));
                Transport.send(message);

                System.out.println("Email sent to " + recipient);
            }

            messageData.setStatus(MessageStatus.DONE);

        }
        catch (MessagingException e) {
            messageData.setErrorMessage(e.toString());
            messageData.setStatus(MessageStatus.ERROR);
        }
        finally {
            System.out.println(messageData);
            repository.save(messageData);
        }

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
