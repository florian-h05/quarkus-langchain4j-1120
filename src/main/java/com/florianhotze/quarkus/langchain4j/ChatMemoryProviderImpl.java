package com.florianhotze.quarkus.langchain4j;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import io.quarkiverse.langchain4j.memorystore.RedisChatMemoryStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class ChatMemoryProviderImpl implements ChatMemoryProvider {
    private final ChatMemoryProvider delegate;

    @Inject
    ChatMemoryProviderImpl(RedisChatMemoryStore store) {
        this.delegate =
                memoryId ->
                        MessageWindowChatMemory.builder()
                                .id(memoryId)
                                .maxMessages(3)
                                .chatMemoryStore(store)
                                .build();
    }

    @Override
    public ChatMemory get(Object o) {
        return delegate.get(o);
    }
}
