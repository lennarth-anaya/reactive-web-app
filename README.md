# reactive-web-app
Sample app of reactive web application using the right technologies

# Webflux

## Startup

sudo docker run --rm --name test-mongo -p 27017:27017 -v $PWD/mongo/data:/data/db -d mongo

```
cd reactive-server

java -jar build/libs/reactive-web-app-0.0.1-SNAPSHOT.jar
```

Or debugging:

```
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=y \
       -jar build/libs/reactive-web-app-0.0.1-SNAPSHOT.jar
```

## Make standard REST calls:

`http://localhost:8080/api/v1/myentities`

## Make WebFlux streaming calls:

### Command line

`curl http://localhost:8080/random-entities -H "Accept:text/event-stream"`


### JavaScript's EventSource

Open /public/index-sse.html in your browser after starting the application and MongoDB.

The file is not served, you have to open it from its current location with file:/// protocol.

Note on MongoDB: Though MongoDB is not used by /random-entities end point, it's necessary to start up the application.


#### Note on SSE (Server Sent Events):

SSE
* SSE are *one-way channels*, server to browser push only, suitable for streaming applications
* One browser is able to hold only *6 connections* (all opened tabs would fight for them).
* Can only send UTF-8, *no support for binary*.
* Send over HTTP, no need for a special protocol on the server side.
* Built in support for re-connection and event-id

WebSockets
* WebSockets are *two-way channels*, suitable for things like games, messaging, collaboration, financial apps.
* Require WebSocket server implementation to work.
* Can send UTF-8 or *binary*.
* Natively supported in more browsers.
* The best case for the use of WebSocket: low latency, high frequency, and high volume

# WebSockets

This nice example was copied as is from: https://www.nexmo.com/blog/2018/10/08/create-websocket-server-spring-boot-dr

```
cd websockets
./gradlew clean build
./gradlew bootRun
```

Open in your browser: http://localhost:8080

# STOMP

Example from: https://spring.io/guides/gs/messaging-stomp-websocket/

Stomp is a higher level protocol that runs over WebSockets (or equivalent old technologies)

```
cd stomp
./gradlew build
./gradlew bootRun
```

Open in your browser: http://localhost:8080
