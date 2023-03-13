package ua.clamor1s.emailsender.repository;

import org.springframework.data.repository.CrudRepository;
import ua.clamor1s.emailsender.data.MessageData;

public interface MessageRepository extends CrudRepository<MessageData, String> {

}
