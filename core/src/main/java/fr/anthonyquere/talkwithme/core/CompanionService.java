package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.domains.Companion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanionService {
  private final CompanionStorage companionStorage;
  private final CompanionConversationStorage companionConversationStorage;
  private final CompletionProvider completionProvider;


  public CompanionConversation getConversationById(String companionId, String userId) {
    return companionConversationStorage.getConversationByCompanionAndUser(
        companionId,
        userId
      ).toBuilder()
      .companion(getCompanionById(companionId))
      .build();
  }

  public Companion getCompanionById(String companionId) {
    return companionStorage.getById(
      companionId
    );
  }

  public List<Companion> listAllCompanions() {
    return companionStorage.findAll().stream().sorted(Comparator.comparing(Companion::getName)).toList();
  }

  public void sendMessage(String companionId, String userId, String question) {
    var companion = getCompanionById(companionId);
    try {
      completionProvider.answerMessage(companion, userId, question);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
