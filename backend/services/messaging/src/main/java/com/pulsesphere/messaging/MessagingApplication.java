package com.pulsesphere.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MessagingApplication {
  public static void main(String[] args) {
    SpringApplication.run(MessagingApplication.class, args);
  }

  @RestController
  static class MessagingController {
    @GetMapping("/chats/{chatId}/messages")
    public String messages(@PathVariable String chatId) {
      return "messages for " + chatId;
    }
  }
}
