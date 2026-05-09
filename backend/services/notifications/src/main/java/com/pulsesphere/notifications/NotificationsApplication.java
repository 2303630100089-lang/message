package com.pulsesphere.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class NotificationsApplication {
  public static void main(String[] args) {
    SpringApplication.run(NotificationsApplication.class, args);
  }

  @RestController
  static class NotificationsController {
    @PostMapping("/notifications/push")
    public String push() {
      return "queued";
    }
  }
}
