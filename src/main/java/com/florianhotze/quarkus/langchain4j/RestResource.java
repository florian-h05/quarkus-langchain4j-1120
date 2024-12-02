package com.florianhotze.quarkus.langchain4j;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestMulti;

@Path("/rest")
class RestResource {

    private final Assistant assistant;

    @Inject
    RestResource(Assistant assistant) {
        this.assistant = assistant;
    }

    @POST
    @Path("/assistant/stream")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Operation(
            operationId = "promptAssistantStreamResponse",
            summary = "Send a prompt to the assistant and stream the response.")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content =
            @Content(
                    mediaType = MediaType.SERVER_SENT_EVENTS,
                    schema = @Schema(implementation = String.class)))
    public Multi<String> streamingPrompt(String prompt) {
        Multi<String> sourceMulti =
                Multi.createFrom()
                        .emitter(
                                emitter ->
                                        assistant
                                                .chat(prompt)
                                                .onNext(emitter::emit)
                                                .onError(emitter::fail)
                                                .onComplete(response -> emitter.complete())
                                                .start());

        return RestMulti.fromMultiData(sourceMulti).withDemand(1).status(200).build();
    }
}
