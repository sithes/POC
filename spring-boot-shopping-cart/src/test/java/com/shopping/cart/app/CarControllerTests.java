/**
 * 
 */
package com.shopping.cart.app;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import com.shopping.cart.app.model.Product;

/**
 * @author Sithes
 *
 */
public class CarControllerTests extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testSend() throws Exception {
		queueService.send(QUEUE_NAME, "queueTest");
		assertThat(queueService.pendingJobs(QUEUE_NAME)).isEqualTo(1);
	}

	@Test
	public void testReceive() throws Exception {
		var message = new ActiveMQTextMessage();
		message.setText("queueTest");
		queueService.onMessage(message);
		assertThat(queueService.completedJobs()).isEqualTo(1);
	}

	@Test
	public void testAddProduct() throws Exception {
		Product product = new Product();
		product.setProductName("iPhone");
		product.setQuantity(1);

		MvcResult mvcResult = mockMvc.perform(post("/submit").flashAttr("product", product)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

}
