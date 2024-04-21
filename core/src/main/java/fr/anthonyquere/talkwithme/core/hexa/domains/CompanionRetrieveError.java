package fr.anthonyquere.talkwithme.core.hexa.domains;

public class CompanionRetrieveError extends RuntimeException {
  public CompanionRetrieveError(String message) {
    super(message);
  }

  public CompanionRetrieveError(String message, Throwable cause) {
    super(message, cause);
  }
}
