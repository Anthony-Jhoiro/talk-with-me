package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.data.md.companions.MarkdownCompanionRepository;
import fr.anthonyquere.talkwithme.core.crud.message.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {
  @Qualifier("langchain")
  private final Completion completion;
  private final MarkdownCompanionRepository companionRepository;

  public CompletionService(@Qualifier("langchain") Completion completion, MarkdownCompanionRepository companionRepository) {
    this.completion = completion;
    this.companionRepository = companionRepository;
  }

  public Message complete(String companionId, String question) throws Exception {
    var companion = companionRepository.getById(companionId);

    var message = Message
      .builder()
      .message(question)
      .companionId(companion.getId())
      .build();

    var completionOutput = completion.answerMessage(companion, message.getMessage());

    return Message
      .builder()
      .message(completionOutput.message())
      .companionId(companionId)
      .build();
  }
}
