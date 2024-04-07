package fr.anthonyquere.talkwithme.core.adapters.data.md.companions;

import fr.anthonyquere.talkwithme.core.hexa.CompanionStorage;
import fr.anthonyquere.talkwithme.core.hexa.domains.Companion;
import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionRetrieveError;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class MarkdownCompanionRepository implements CompanionStorage {

  @Override
  public Companion getById(String id) {
    var parser = Parser.builder()
      .extensions(List.of(YamlFrontMatterExtension.create()))
      .build();

    Node document;

    try (var resource = getClass().getClassLoader().getResourceAsStream("neighbors/" + id + "/" + id + ".md")) {
      if (resource == null) {
        throw new FileNotFoundException("Fail to fetch resource");
      }
      document = parser.parseReader(new InputStreamReader(resource));

    } catch (IOException ex) {
      throw new CompanionRetrieveError("Fail to retrieve companion", ex);
    }

    var visitor = new MarkdownParser();
    document.accept(visitor);

    var yamlData = visitor.getData();

    return Companion.builder()
      .id(id)
      .name(yamlData.get("name").getFirst())
      .species(yamlData.get("species").getFirst())
      .gender(yamlData.get("gender").getFirst())
      .background(visitor.getContent())
      .build();
  }

  @Override
  public Collection<Companion> findAll() {
    Set<String> companionIds;

    try {
      companionIds = Arrays.stream(new String(getClass().getClassLoader().getResourceAsStream("neighbors").readAllBytes())
          .split("\n"))
        .filter(id -> !id.isEmpty())
        .collect(Collectors.toSet());
      ;

    } catch (IOException ex) {
      throw new CompanionRetrieveError("Fail to retrieve companion", ex);
    }

    return companionIds.stream().map(this::getById).collect(Collectors.toSet());
  }

  private static class MarkdownParser extends YamlFrontMatterVisitor {
    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void visit(Text text) {
      stringBuilder.append(text.getLiteral());
    }

    public String getContent() {
      return stringBuilder.toString();
    }
  }

}
