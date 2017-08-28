package star16m.utils.toapp.commons;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommonService {

	@Autowired
	private MessageRepository messageRepository;
	public Message saveMessage(String type, String message) {
		Message msg = new Message();
		msg.setType(type);
		msg.setMessage(message);
		msg.setCreateDate(new Date());
		return messageRepository.save(msg);
	}
}
