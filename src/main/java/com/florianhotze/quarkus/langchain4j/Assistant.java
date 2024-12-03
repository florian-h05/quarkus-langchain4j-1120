package com.florianhotze.quarkus.langchain4j;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface Assistant {
    Multi<String> chat(@UserMessage String prompt);
}
