package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.crud.companions.CompanionRepository;
import fr.anthonyquere.talkwithme.core.crud.message.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {
  @Qualifier("Langchain")
  private final Completion completion;
  private final CompanionRepository companionRepository;

  public CompletionService(@Qualifier("Langchain") Completion completion, CompanionRepository companionRepository) {
    this.completion = completion;
    this.companionRepository = companionRepository;
  }

  public Message complete(String companionId, String question) throws Exception {
    var companion = companionRepository.findById(companionId).orElseThrow();

    var message = Message
      .builder()
      .message(question)
      .companion(companion)
      .build();

    var completionOutput = completion.answerMessage(companion, message.getMessage());

    return Message
      .builder()
      .message(completionOutput.message())
      .companion(companion)
      .build();
  }
}
