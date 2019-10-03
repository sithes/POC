/**
 * 
 */
package com.shopping.cart.app;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.shopping.cart.app.queue.QueueService;

/**
 * @author Sithes
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {
	protected static final String QUEUE_NAME = "testQueue";

	protected MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	QueueService queueService;

	protected void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

}
