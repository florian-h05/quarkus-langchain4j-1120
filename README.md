# quarkus-langchain4j-1120

This repository contains code to reproduce [quarkiverse/quarkus-langchain4j#1120](https://github.com/quarkiverse/quarkus-langchain4j/issues/1120).

## Introduction

This project uses Quarkus, the Supersonic Subatomic Java Framework.
It is build with Java 21 and Maven 3.9.0.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Reproducing the issue

A Redis server will be provided automatically by the Quarkus Redis Dev service.
You don't need to spin up your own Redis.

Make sure to provide a value to the OpenAI API key, e.g. through the `QUARKUS_LANGCHAIN4J_OPENAI_API_KEY` environment variable or an `.env` file.
You do not need to provide an actual API key, the issue can still be reproduced without it.
Once the issue is resolved, you however need a valid API key, otherwise you will receive a exception from the OpenAI API.

Run the application in dev mode:

```shell script
./mvnw quarkus:dev
```

Send a POST request to the `/rest/assistant/stream` endpoint using curl:

```shell script
curl -X 'POST' \
  'http://localhost:8080/rest/assistant/stream' \
  -H 'accept: text/event-stream' \
  -H 'Content-Type: text/plain' \
  -d 'How are you?'
```

Alternatively use Swagger UI: <http://localhost:8080/q/swagger-ui/>.

You will receive this exception in the Quarkus console output:

```
java.lang.IllegalStateException: The current thread cannot be blocked: vert.x-eventloop-thread-1
        at io.smallrye.mutiny.operators.uni.UniBlockingAwait.await(UniBlockingAwait.java:30)
        at io.smallrye.mutiny.groups.UniAwait.atMost(UniAwait.java:65)
        at io.quarkus.redis.runtime.datasource.BlockingStringCommandsImpl.get(BlockingStringCommandsImpl.java:41)
        at io.quarkiverse.langchain4j.memorystore.RedisChatMemoryStore.getMessages(RedisChatMemoryStore.java:33)
        at io.quarkiverse.langchain4j.memorystore.RedisChatMemoryStore_xTEUBJ9I9t7XqF0naKuYsMHw80A_Synthetic_ClientProxy.getMessages(Unknown Source)
        at dev.langchain4j.memory.chat.MessageWindowChatMemory.messages(MessageWindowChatMemory.java:84)
        ...
```
