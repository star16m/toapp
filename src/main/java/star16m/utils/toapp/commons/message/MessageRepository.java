package star16m.utils.toapp.commons.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop10ByOrderByCreateDateDesc();

    List<Message> findMessageByOrderByCreateDateDesc();

    void deleteByCreateDateLessThan(LocalDate targetDate);
}
