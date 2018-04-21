package star16m.utils.toapp.commons.message;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findTop10ByOrderByCreateDateDesc();
	List<Message> findMessageByOrderByCreateDateDesc();
	void deleteByCreateDateLessThan(Date targetDate);
}
