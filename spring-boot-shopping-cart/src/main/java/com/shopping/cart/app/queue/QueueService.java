package com.shopping.cart.app.queue;

import java.util.Collections;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Sithes
 *
 */
@Service
public class QueueService implements MessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueueService.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	private int counter = 0;

	public int completedJobs() {
		return counter;
	}

	public void send(String destination, String message) {
		LOGGER.info("Entering QueueService.sending with  message='{}' to destination='{}'", message, destination);
		jmsTemplate.convertAndSend(destination, message);
	}

	@SuppressWarnings("unchecked")
	public int pendingJobs(String queueName) {
		return jmsTemplate.browse(queueName, (s, qb) -> Collections.list(qb.getEnumeration()).size());
	}

	public boolean isUp() {
		var connection = jmsTemplate.getConnectionFactory();
		try {
			connection.createConnection().close();
			return true;
		} catch (JMSException e) {
			LOGGER.error("JMSException @QueueService isUp Method ", e); 
		}
		return false;
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
			try {
				LOGGER.info("Processing task @QueueService.onMessage with '{}'",  textMessage.getText());
				Thread.sleep(2000);
				LOGGER.info("Completed task @QueueService.onMessage with '{}'", textMessage.getText());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				e.printStackTrace();
			}
			counter++;
		} else {
			LOGGER.error("Error @QueueService.onMessage Message is not a text message '{}'", message.toString());
		}
	}
}
