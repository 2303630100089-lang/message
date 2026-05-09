package com.pulsesphere.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SearchApplication {
  public static void main(String[] args) {
    SpringApplication.run(SearchApplication.class, args);
  }

  @RestController
  static class SearchController {
    @GetMapping("/search")
    public String search(@RequestParam String q) {
      return "search accepted";
    }
  }
}
