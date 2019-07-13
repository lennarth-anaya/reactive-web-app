package org.lrth.samples.reactivewebapp.data;

import org.lrth.samples.reactivewebapp.entities.XEntity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface XEntityRepository extends ReactiveMongoRepository<XEntity, String> {
}
