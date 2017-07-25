package star16m.utils.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
	public List<Message> findTop10ByOrderByCreateDateDesc();
}
