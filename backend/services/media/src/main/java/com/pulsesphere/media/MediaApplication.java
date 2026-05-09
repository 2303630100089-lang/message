package com.pulsesphere.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MediaApplication {
  public static void main(String[] args) {
    SpringApplication.run(MediaApplication.class, args);
  }

  @RestController
  static class MediaController {
    @PostMapping("/media/upload")
    public String upload() {
      return "upload-placeholder";
    }
  }
}
