package org.lrth.samples.reactivewebapp.controller;

import static org.mockito.BDDMockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import org.lrth.samples.reactivewebapp.entities.XEntity;
import org.lrth.samples.reactivewebapp.data.XEntityRepository;
import org.lrth.samples.reactivewebapp.controller.XEntityController;

// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
// import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
// import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
// import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import org.reactivestreams.Publisher;

// would allow Spring context and MockBean, at the expense of having to use embedded MongoDB
// @RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

// SpringBootApplication annotation prevent next exclusion to avoid spring boot context to try to really connect to Mongo's
// so it cannot be used
// @EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class XEntityControllerTest {
  
  WebTestClient webTestClient;

  // see comments above to check why we'd rather use plain Mockito's Mock
  // @MockBean
  @Mock
  XEntityRepository xEntityRepository;
  
  @InjectMocks
  XEntityController xEntityController;
  
  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    webTestClient = WebTestClient.bindToController(xEntityController).build();
  }

  @Test
  public void list() {
    given(xEntityRepository.findAll())
        .willReturn(Flux.just(
          XEntity.builder().description("Cat1").build(),
          XEntity.builder().description("Cat2").build()
        ));

    webTestClient.get().uri("/api/v1/myentities")
      .exchange()
      .expectBodyList(XEntity.class)
      .hasSize(2);
  }

  @Test
  public void findById() {
    given(xEntityRepository.findById("someId"))
        .willReturn(Mono.just(
          XEntity.builder().description("Cat").build()
        ));

    webTestClient.get().uri("/api/v1/myentities/someId")
      .exchange()
      .expectBody(XEntity.class);
  }

  @Test
  public void create() {
    given(xEntityRepository.saveAll(any(Publisher.class)))
        .willReturn(Flux.just(
          XEntity.builder().description("Cat").build()
        ));

    Mono<XEntity> xEntity = Mono.just(XEntity.builder().description("NewCat").build());

    webTestClient.post().uri("/api/v1/myentities")
      .body(xEntity, XEntity.class)
      .exchange()
      .expectStatus().isCreated();
  }

  @Test
  public void update() {
    given(xEntityRepository.save(any(XEntity.class)))
        .willReturn(Mono.just(
          XEntity.builder().description("UpdatedEnt").build()
        ));

    Mono<XEntity> xEntity = Mono.just(XEntity.builder().description("SomeEnt").build());

    webTestClient.put().uri("/api/v1/myentities/someId")
      .body(xEntity, XEntity.class)
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  public void patchWithChanges() {
    given(xEntityRepository.findById(anyString()))
        .willReturn(Mono.just(
          XEntity.builder().description("x").build()
        ));

    given(xEntityRepository.save(any(XEntity.class)))
        .willReturn(Mono.just(
          XEntity.builder().description("y").build()
        ));

    Mono<XEntity> xEntity = Mono.just(XEntity.builder().description("y").build());

    webTestClient.patch().uri("/api/v1/myentities/someId")
      .body(xEntity, XEntity.class)
      .exchange()
      .expectStatus().isOk();

    verify(xEntityRepository).save(any());
  }

  @Test
  public void patchWithoutChanges() {
    given(xEntityRepository.findById(anyString()))
        .willReturn(Mono.just(
          XEntity.builder().description("x").build()
        ));

    // this should not be called anyways, but to avoid misleading test debug if test fails:
    given(xEntityRepository.save(any(XEntity.class)))
        .willReturn(Mono.just(
          XEntity.builder().description("x").build()
        ));

    Mono<XEntity> xEntity = Mono.just(XEntity.builder().description("x").build());

    webTestClient.patch().uri("/api/v1/myentities/someId")
      .body(xEntity, XEntity.class)
      .exchange()
      .expectStatus().isOk();

    verify(xEntityRepository, never()).save(any());
  }

}
