package fr.anthonyquere.talkwithme.core.ai.langchain;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import fr.anthonyquere.talkwithme.core.crud.companions.Companion;
import fr.anthonyquere.talkwithme.core.crud.message.Message;
import fr.anthonyquere.talkwithme.core.crud.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CompanionChatMemory implements ChatMemory {
  private final Companion companion;
  private final MessageRepository messageRepository;

  @Override
  public Object id() {
    return this.companion.getId();
  }

  @Override
  public void add(ChatMessage chatMessage) {
    var messageBuilder = Message.builder();
    messageBuilder.companion(companion);
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
    } else {
      // Ignore SYSTEM and other messages
      return;
    }

    messageRepository.save(messageBuilder.build());
  }

  @Override
  public List<ChatMessage> messages() {
    var messages = new ArrayList<ChatMessage>();
    var systemMessage = "From this context: "
      + companion.getBackground()
      + "\nYou live in a minecraft world and the user speaking to you is a player.";

    var summary = messageRepository.getFirstByCompanion_IdAndTypeOrderByCreatedAtDesc(companion.getId(), "SUMMARY");
    if (summary.isPresent()) {
      systemMessage += "\nThis is a summary of the previous messages: " + summary.get().getMessage();
    }

    systemMessage += "\nAnswer at best but keep it very short and simple.";

    messages.add(new SystemMessage(systemMessage));


    var discussion = messageRepository
      .getMessagesByCompanion_IdOrderByCreatedAtDesc(companion.getId(), Pageable.ofSize(10))
      .stream()
      .filter(m -> m.getType() != null)
      .map(m -> switch (m.getType()) {
        case "USER" -> new UserMessage(m.getMessage());
        case "AI" -> new AiMessage(m.getMessage());
        default -> null;
      })
      .filter(Objects::nonNull)
      .toList();

    messages.addAll(discussion);
    return messages;
  }

  @Override
  public void clear() {
    messageRepository.deleteByCompanionId(companion.getId());
  }
}
