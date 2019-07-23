# Live dashboard with Kafka Streams API

Cloned from [sajmmon's kafka-streams-api-websockets example](https://github.com/ebi-wp/kafka-streams-api-websockets/)

This is an example on how to use Kafka Streams API to aggregate logs and display results online in a dashboard. 

Genome Campus Software Craftsmanship [Meetup](https://www.meetup.com/pl-PL/Genome-Campus-Software-Craftsmanship-Community/events/243518985/)

Tools:
-------------------

1. Java 8+
2. Apache Kafka 0.11
3. Apache Kafka Streams API
4. Spring Boot
5. STOMP 
6. Chart.js

How to run:
--------

### Kafka Docker Compose

1. Add next entry to your `etc/hosts` file:

  ```
  127.0.0.1 kafka
  ```

2. ```
    cd kafka-setup
    sudo docker-compose up -d
    ```

### Java Applications

Note: To debug any of these applications just add next parameters before java's cp flag:

```
-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y
```

3. Clone and build java project
    
   ```git clone https://github.com/ebi-wp/kafka-streams-api-websockets.git```
   
   ```cd kafka-streaming-websockets```
   
   ```mvn clean package```

4. Run producer

   ```java -cp target/shade-kafka-streaming-websockets-0.1.0.jar uk.ac.ebi.produce.KafkaExampleProducer```
   
5. Run streaming client

   ```java -cp target/shade-kafka-streaming-websockets-0.1.0.jar uk.ac.ebi.streaming.KafkaStreamingMain```
   
6. Run spring boot

   ```java -cp target/shade-kafka-streaming-websockets-0.1.0.jar uk.ac.ebi.Application```

7. See live dashboard in [localhost:7070](http://localhost:7070)

When you click "Connect" you should see:

![Live dashboard](docs/img/dashboard.png)

8. To stop all applications just Ctrl+C on each terminal they are running in.

9. To stop Kafka and Zookeeper just type:

   ```sudo docker-compose stop```


Recommended resources:
----------------------

1. [Kafka Streaming (by Gwen Shapira)](https://github.com/gwenshap/kafka-streams-stockstats)
2. [Using WebSocket with Spring Boot (by Pivotal)](https://spring.io/guides/gs/messaging-stomp-websocket/)
3. [Apache Kafka with Spring (by Baeldung)](http://www.baeldung.com/spring-kafka)
4. [Kafka Streams Examples (by Confluent)](https://github.com/confluentinc/examples/tree/3.3.0-post/kafka-streams)
5. [Creating custom serializers (by Niels.nu)](http://niels.nu/blog/2016/kafka-custom-serializers.html)

Issues:
-------
If you run without internet access you might need to add following to *config/server.properties*
```
advertised.host.name = localhost
advertised.port = 9092
```
