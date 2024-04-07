package fr.anthonyquere.talkwithme.core.adapters.ai.langchain;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import fr.anthonyquere.talkwithme.core.hexa.CompanionConversation;
import fr.anthonyquere.talkwithme.core.hexa.CompanionConversationStorage;
import fr.anthonyquere.talkwithme.core.adapters.data.jpa.conversations.Message;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CompanionChatMemory implements ChatMemory {
  private final CompanionConversation.Id companionConversationId;
  private final CompanionConversationStorage conversationsRepository;

  @Override
  public Object id() {
    return companionConversationId;
  }

  @Override
  public void add(ChatMessage chatMessage) {
    var messageBuilder = Message.builder();
    messageBuilder.companionId(this.companionConversationId.companionId());
    messageBuilder.userId(this.companionConversationId.userId());
    messageBuilder.createdAt(LocalDateTime.now());

    // User messages
    if (chatMessage instanceof UserMessage m) {
      messageBuilder
        .type(ChatMessageType.USER.name())
        .message(
          String.join(" ", m.contents().stream().map(c -> {
            if (c instanceof TextContent textContent) {
              return textContent.text();
            }
            return "";
          }).toList())
        );
    } else if (chatMessage instanceof AiMessage m) {
      messageBuilder
        .type(ChatMessageType.AI.name())
        .message(m.text());
    } else if (chatMessage instanceof SystemMessage m) {
      messageBuilder
        .type(ChatMessageType.SYSTEM.name())
        .message(m.text());
      conversationsRepository.setIntroductionMessage(this.companionConversationId, messageBuilder.build());
      return;
    } else {
      return;
    }

    conversationsRepository.saveMessage(this.companionConversationId, messageBuilder.build());
  }

  @Override
  public List<ChatMessage> messages() {
    var conversation = conversationsRepository
      .getConversationByCompanionAndUser(companionConversationId.companionId(), companionConversationId.userId())
      .messages()
      .stream()
      .filter(m -> m.getType() != null)
      .map(m -> switch (m.getType()) {
        case "USER" -> new UserMessage(m.getMessage());
        case "AI" -> new AiMessage(m.getMessage());
        case "SYSTEM" -> new SystemMessage(m.getMessage());
        default -> null;
      })
      .filter(Objects::nonNull)
      .toList();

    var conversationAsMutableList = new ArrayList<>(conversation);
    Collections.reverse(conversationAsMutableList);

    return conversationAsMutableList;
  }

  @Override
  public void clear() {
    conversationsRepository.clearDiscussion(companionConversationId);
  }


}
