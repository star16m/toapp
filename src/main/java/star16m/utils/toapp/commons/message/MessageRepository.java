package star16m.utils.toapp.commons.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessageByOrderByCreateDateDesc();

    @Modifying
    @Query(value = "delete from message where create_date < DATEADD('DAY', -:availableMessageDay, CURRENT_DATE)", nativeQuery = true)
    void deleteOLdMessage(Long availableMessageDay);
}
