package fr.anthonyquere.talkwithme.core;


import fr.anthonyquere.talkwithme.core.domains.Companion;

public interface CompletionProvider {
  CompletionOutput answerMessage(Companion companion, String userId, String question) throws Exception;
}
