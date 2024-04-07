package fr.anthonyquere.talkwithme.core.hexa;

import fr.anthonyquere.talkwithme.core.hexa.domains.Companion;

import java.util.Collection;

public interface CompanionStorage {
  Companion getById(String id);
  Collection<Companion> findAll();
}
