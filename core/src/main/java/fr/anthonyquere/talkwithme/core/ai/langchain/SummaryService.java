package fr.anthonyquere.talkwithme.core.ai.langchain;

import fr.anthonyquere.talkwithme.core.ai.langchain.services.SummaryLangchainService;
import fr.anthonyquere.talkwithme.core.domains.Companion;
import fr.anthonyquere.talkwithme.core.data.jpa.conversations.Message;
import fr.anthonyquere.talkwithme.core.data.jpa.conversations.MessageJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.StringMapMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

  private final MessageJpaRepository messageRepository;
  private final SummaryLangchainService summaryLangchainService;

  @Value("${hardRetentionSummary:5}")
  private Integer hardRetentionSummary = 5;

  @Transactional
  public void summarizeChat(Companion companion) {
    var messages = messageRepository.getMessagesByCompanionIdOrderByCreatedAtDesc(companion.getId(), Pageable.ofSize(20));

    var notArchivedMessages = messages.stream()
      .filter(m -> m.getStatus() == null || m.getStatus().equals(Message.Status.NOT_ARCHIVED))
      .toList();

    if (notArchivedMessages.size() < hardRetentionSummary) {
      return;
    }

    // do not summarize  n last messages
    var messagesToArchive = notArchivedMessages.subList(hardRetentionSummary, notArchivedMessages.size());

    if (messagesToArchive.isEmpty()) {
      log.debug("Nothing to summarize");
      return;
    }

    log.info(new StringMapMessage()
      .with("message", "Generating summary")
      .with("messageCount", messagesToArchive)
      .with("companion", companion)
      .toString()
    );

    String sum;

    try {
      sum = summaryLangchainService.summarize("Summarize the following messages as best you can as if you were explaining it to the AI.\n" + String.join("\n", messagesToArchive.stream().map(Message::toString).toList()));
    } catch (Exception e) {
      log.error("Fail to generate summary: {}", e.getMessage());
      return;
    }

    var summaryMessage = Message.builder()
      .message(sum)
      .type("SUMMARY")
      .companionId(companion.getId())
      .createdAt(LocalDateTime.now())
      .build();


    // Mark previous messages as archived
    messagesToArchive.forEach(m -> m.setStatus(Message.Status.ARCHIVED));
    messageRepository.saveAll(messagesToArchive);
    // Save summary
    messageRepository.save(summaryMessage);

  }
}
