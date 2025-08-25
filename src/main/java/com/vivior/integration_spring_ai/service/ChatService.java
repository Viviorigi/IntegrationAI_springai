package com.vivior.integration_spring_ai.service;

import com.vivior.integration_spring_ai.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public String chat(ChatRequest request) {
        return chatClient
                .prompt(request.message())
                .call()
                .content();

    }
}
