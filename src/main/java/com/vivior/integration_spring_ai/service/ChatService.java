package com.vivior.integration_spring_ai.service;

import com.vivior.integration_spring_ai.dto.BillItem;
import com.vivior.integration_spring_ai.dto.ChatRequest;
import com.vivior.integration_spring_ai.dto.ExpenseInfo;
import com.vivior.integration_spring_ai.dto.FilmInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    public ChatService(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30)
                .build();

        chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(@NotNull ChatRequest request) {
        String conversationId = "conversation2";
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
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

    }

    public List<BillItem> chatWithImage(MultipartFile file, String message){

        Media media = Media.builder()
                    .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                    .data(file.getResource())
                    .build();

        ChatOptions chatOptions = ChatOptions
                .builder().temperature(0D)
                .build();

        return chatClient.prompt()
                .options(chatOptions)
                .system("You are ViviOrigi AI — Duong's personal AI.\n" +
                        "        - Detect the user's language and reply in that language (vi/en). If user asks to switch, then switch.\n" +
                        "        - Be super funny but still clear and helpful.\n" +
                        "        - Keep code/JSON unchanged; jokes only outside code fences.")
                .user(promptUserSpec
                    -> promptUserSpec.media(media)
                    .text(message))
                .call()
                .entity(new ParameterizedTypeReference<List<BillItem>>() {
                });
    }
}
