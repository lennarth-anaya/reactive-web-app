package org.lrth.samples.reactivewebapp.controller;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

import org.lrth.samples.reactivewebapp.data.XEntityRepository;
import org.lrth.samples.reactivewebapp.entities.XEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.reactivestreams.Publisher;

@RestController
public class XEntityController {
  private final XEntityRepository xEntityRepository;

  public XEntityController(XEntityRepository xEntityRepository) {
    this.xEntityRepository = xEntityRepository;
  }

  @GetMapping("/api/v1/myentities")
  public Flux<XEntity> list() {
    return xEntityRepository.findAll();
  }

  @GetMapping("/api/v1/myentities/{id}")
  public Mono<XEntity> getById(@PathVariable String id) {
    return xEntityRepository.findById(id);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/api/v1/myentities")
  public Mono<Void> create(@RequestBody Publisher<XEntity> entityStream) {
    return xEntityRepository.saveAll(entityStream)
      .then();
  }

  @PutMapping("/api/v1/myentities/{id}")
  public Mono<XEntity> update(@PathVariable String id, @RequestBody XEntity entity) {
    entity.setId(id);
    return xEntityRepository.save(entity);
  }

  @PatchMapping("/api/v1/myentities/{id}")
  public Mono<XEntity> patch(@PathVariable String id, @RequestBody XEntity entity) {
    // TODO: another way to do it without blocking?
    // NOTE: next business logic would suit better in a service component
    XEntity preExEntity = xEntityRepository.findById(id).block();

    if (!preExEntity.getDescription().equals(entity.getDescription())) {
        preExEntity.setDescription(entity.getDescription());
        return xEntityRepository.save(preExEntity);
    }

    return Mono.just(preExEntity);
  }

  @CrossOrigin
  @GetMapping(
      value = "/random-entities",
      produces = {MediaType.TEXT_EVENT_STREAM_VALUE}
  )
  public Flux<XEntity> getRandomEntitiesSSE() {
      Random r = new Random(System.currentTimeMillis());
      
      Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
      Flux<XEntity> entity = Flux.fromStream(Stream.generate(() ->
          XEntity.builder()
              .id("" + r.nextInt())
              .description("DESC-" + r.nextInt())
              .build()
      ));
      
      // Use interval just for generation, but we're only interested on returning entity
      Flux<XEntity> events = Flux.zip(interval, entity).map(
          (fluxes) -> fluxes.getT2()
      );
      
      return events;
  }
  
}
