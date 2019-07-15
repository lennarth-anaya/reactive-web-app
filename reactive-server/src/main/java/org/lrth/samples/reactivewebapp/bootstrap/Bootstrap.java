package org.lrth.samples.reactivewebapp.bootstrap;

import java.util.ArrayList;

import org.lrth.samples.reactivewebapp.data.XEntityRepository;
import org.lrth.samples.reactivewebapp.entities.XEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class Bootstrap implements CommandLineRunner {
  private final XEntityRepository xEntityRepository;

  public Bootstrap(XEntityRepository xEntityRepository) {
    this.xEntityRepository = xEntityRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    if (xEntityRepository.count().block() == 0) {
      ArrayList<XEntity> entities = new ArrayList<>();
      entities.add(XEntity.builder().description("Test pre-existent entity 1").build());
      entities.add(XEntity.builder().description("Test pre-existent entity 2").build());
      
      xEntityRepository
        .saveAll(entities)
        .blockLast();
    }
  }
}
