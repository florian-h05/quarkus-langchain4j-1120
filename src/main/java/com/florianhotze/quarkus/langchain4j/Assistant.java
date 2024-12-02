package com.florianhotze.quarkus.langchain4j;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface Assistant {
    TokenStream chat(@UserMessage String prompt);

    TokenStream chat(@MemoryId Object memoryId, @UserMessage String prompt);
}
