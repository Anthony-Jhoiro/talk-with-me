package fr.anthonyquere.talkwithme.core.crud.companions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@Entity
@Getter
@Setter
public class Companion {
  @Id
  @GenericGenerator(name = "id", type = CompanionIdGenerator.class)
  @GeneratedValue(generator = "id")
  private String id;

  private String name;

  @Column(length = 500)
  private String background;

  public static class CompanionIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
      var c = (Companion) o;
      return c.getName().toLowerCase().replaceAll("[^a-zA-Z]", "_");
    }
  }
}
