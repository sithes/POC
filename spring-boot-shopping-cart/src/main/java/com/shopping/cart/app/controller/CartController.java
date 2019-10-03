package com.shopping.cart.app.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.cart.app.model.Product;
import com.shopping.cart.app.queue.QueueService;

/**
 * @author Sithes
 *
 */
@Controller
public class CartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private QueueService queueService;

	@Value("${queue.name}")
	private String queueName;

	@Value("${worker.name}")
	private String workerName;

	@Value("${store.enabled}")
	private boolean storeEnabled;

	@Value("${worker.enabled}")
	private boolean workerEnabled;

	@GetMapping("/")
	public String home(Model model) {
		LOGGER.debug("Entering CartController.home with {}", model);
		model.addAttribute("product", new Product());
		model.addAttribute("isStoreEnabled", this.storeEnabled);
		LOGGER.debug("Leaving CartController.home with {}", model);
		return "home";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		LOGGER.debug("Entering CartController.admin with {}", model);
		if (this.workerEnabled) {
			model.addAttribute("isWorkerEnabled", this.workerEnabled);
			model.addAttribute("isConnected", queueService.isUp() ? "yes" : "no");
			model.addAttribute("queueName", this.queueName);
			model.addAttribute("workerName", this.workerName);
			model.addAttribute("pendingJobs", queueService.pendingJobs(queueName));
			model.addAttribute("completedJobs", queueService.completedJobs());
		} else
			return "home";
		LOGGER.debug("Leaving CartController.admin with {}", model);
		return "admin";
	}

	@PostMapping("/submit")
	public String addProduct(@ModelAttribute Product product) {
		LOGGER.debug("Entering CartController.addProduct with {}", product);
		for (long i = 0; i < product.getQuantity(); i++) {
			String id = UUID.randomUUID().toString();
			queueService.send(queueName, id);
		}
		LOGGER.debug("Leaving CartController.addProduct with Success");
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/metrics", produces = "text/plain")
	public String metrics() {
		int totalMessages = queueService.pendingJobs(queueName);
		return "# HELP messages Number of messages in the queueService\n" + "# TYPE messages gauge\n" + "messages "
				+ totalMessages;
	}

	@RequestMapping(value = "/health")
	public ResponseEntity<Object> health() {
		HttpStatus status;
		if (queueService.isUp()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

}