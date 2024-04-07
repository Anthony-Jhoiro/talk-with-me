package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.data.jpa.conversations.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletionService {
  private final CompletionProvider completionProvider;
  private final CompanionStorage companionStorage;


  public Message complete(String companionId, String userId, String question) throws Exception {
    var companion = companionStorage.getById(companionId);

    var message = Message
      .builder()
      .message(question)
      .companionId(companion.getId())
      .userId(userId)
      .build();

    var completionOutput = completionProvider.answerMessage(companion, userId, message.getMessage());

    return Message
      .builder()
      .message(completionOutput.message())
      .companionId(companion.getId())
      .userId(userId)
      .build();
  }
}
