package com.example.practice.scheduler;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.services.CustomerService;
import com.example.practice.services.EmailService;
import com.example.practice.services.OrderService;
import com.example.practice.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@EnableScheduling
@Component
public class OrderScheduler {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;


    @Scheduled(fixedDelay = 50000)
    public void placeOrder() {

        int randomAmount = getRandomAmount();
        Customer randomCustomer = getRandomCustomer(customerService.getAllCustomer());
        Product randomProduct = getRandomProduct(productService.getAllProducts());
        String emailContent;
        Order order = new Order();
        order.setAmount(randomAmount);
        order.setCustomer(randomCustomer);
        order.setProduct(randomProduct);
        orderService.saveOrder(order);
        String customerEmail = randomCustomer.getEmail();
        log.info("Order with id:'{}' planner", order.getId());
        String emailSubject = "Уведомление о заказе";
        if (order != null) {
            emailContent = "Ваш заказ успешно поставлен в очередь для выполнения.";
            log.info("Заказ успешно поставлен в очередь для выполнения");
        } else {
            emailContent = "Ваш заказ невозможно выполнить.";
            log.info("Заказ невозможно выполнить");
        }
        emailService.sendEmail(customerEmail, emailSubject, emailContent);
    }

    private int getRandomAmount() {
        int minAmount = 1;
        int maxAmount = 10;

        Random random = new Random();
        return random.nextInt(maxAmount - minAmount + 1) + minAmount;
    }

    private Customer getRandomCustomer(List<Customer> customers) {
        Random random = new Random();
        int randomIndex = random.nextInt(customers.size());
        return customers.get(randomIndex);
    }

    private Product getRandomProduct(List<Product> products) {
        Random random = new Random();
        int randomIndex = random.nextInt(products.size());
        return products.get(randomIndex);
    }
}
