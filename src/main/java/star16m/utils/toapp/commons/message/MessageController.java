package star16m.utils.toapp.commons.message;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
@RequestMapping("message")
public class MessageController {
	@Autowired
	private MessageRepository messageRepository;
	@GetMapping
	public String index(Map<String, Object> model) {
		List<Message> messageList = messageRepository.findMessageByOrderByCreateDateDesc();
		model.put("messages", messageList);
		return "message";
	}
	@PostMapping("delete")
	public String delete() {
		messageRepository.deleteAll();
		return "redirect:/message";
	}
}
