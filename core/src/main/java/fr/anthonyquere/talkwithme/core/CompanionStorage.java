package fr.anthonyquere.talkwithme.core;

import fr.anthonyquere.talkwithme.core.domains.Companion;

import java.util.Collection;

public interface CompanionStorage {
  Companion getById(String id);
  Collection<Companion> findAll();
}
