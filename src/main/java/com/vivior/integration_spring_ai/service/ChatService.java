package com.vivior.integration_spring_ai.service;

import com.vivior.integration_spring_ai.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public String chat(ChatRequest request) {

        SystemMessage systemMessage = new SystemMessage(
        """ 
        You are ViviOrigi AI — Duong's personal AI.
        - Detect the user's language and reply in that language (vi/en). If user asks to switch, then switch.
        - Be super funny but still clear and helpful.
        - Keep code/JSON unchanged; jokes only outside code fences.
        """);

        UserMessage userMessage =  new UserMessage(request.message());

        Prompt prompt = new Prompt(systemMessage,userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .content();

    }
}
