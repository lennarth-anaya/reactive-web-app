package org.lrth.samples.reactivewebapp.bootstrap;

import org.lrth.samples.reactivewebapp.data.XEntityRepository;
import org.lrth.samples.reactivewebapp.entities.XEntity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
  private final XEntityRepository xEntityRepository;

  public Bootstrap(XEntityRepository xEntityRepository) {
    this.xEntityRepository = xEntityRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    /*if (xEntityRepository.count().block() == 0) {
      xEntityRepository
        .save(XEntity.builder().description("Test pre-existent entity").build())
        .block();
    }*/
  }
}
