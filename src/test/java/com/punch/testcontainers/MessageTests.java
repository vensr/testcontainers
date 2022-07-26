package com.punch.testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.punch.testcontainers.model.Message;
import com.punch.testcontainers.repository.MessageRepository;

@SpringBootTest
class MessageTests extends BaseTestContainer {

	@Autowired
	private MessageRepository messageRepository;

	@Test
	void createMessages() {
		Message message = new Message();
		message.setMessage("This is a sample message");
		messageRepository.save(message);

		assertTrue(message.getId() > 0);
	}

	@Test
	void getMessages() {
		assertTrue(messageRepository.findAll().size() == 1);
	}

}
