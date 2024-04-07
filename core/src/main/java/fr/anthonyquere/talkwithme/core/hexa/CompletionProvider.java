package fr.anthonyquere.talkwithme.core.hexa;


import fr.anthonyquere.talkwithme.core.hexa.domains.Companion;

public interface CompletionProvider {
  CompletionOutput answerMessage(Companion companion, String userId, String question) throws Exception;
}
