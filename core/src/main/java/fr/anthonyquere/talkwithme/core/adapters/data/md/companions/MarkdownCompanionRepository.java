package fr.anthonyquere.talkwithme.core.adapters.data.md.companions;

import fr.anthonyquere.talkwithme.core.hexa.CompanionStorage;
import fr.anthonyquere.talkwithme.core.hexa.domains.Companion;
import fr.anthonyquere.talkwithme.core.hexa.domains.CompanionRetrieveError;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Slf4j
public class MarkdownCompanionRepository implements CompanionStorage {

  @Override
  public Companion getById(String id) {
    var parser = Parser.builder()
      .extensions(List.of(YamlFrontMatterExtension.create()))
      .build();

    Node document;

    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    try {
      var resolvedResources = resolver.getResources("classpath*:neighbors/**/" + id + ".md");
      var resource = Stream.of(resolvedResources).findFirst().orElseThrow(() -> new CompanionRetrieveError("Companion with id [" + id + "] not found"));

      document = parser.parseReader(new InputStreamReader(resource.getInputStream()));
    } catch (IOException ex) {
      throw new CompanionRetrieveError("Fail to retrieve companion [" + id + "]", ex);
    }

    var visitor = new MarkdownParser();
    document.accept(visitor);

    var yamlData = visitor.getData();

    return Companion.builder()
      .id(id)
      .name(yamlData.get("name").get(0))
      .species(yamlData.get("species").get(0))
      .gender(yamlData.get("gender").get(0))
      .background(visitor.getContent())
      .build();
  }

  @Override
  public Collection<Companion> findAll() {
    Set<String> companionIds;

    try {
      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      var resolvedResources = resolver.getResources("classpath*:neighbors/**/*.md");

      log.info("found companions: {}", Arrays.stream(resolvedResources).toList());

      companionIds = Arrays.stream(resolvedResources)
        .map(resource -> resource.getFilename().split("/"))
        .map(filePath -> filePath[filePath.length - 1])
        .map(filename -> filename.split("\\.")[0])
        .filter(id -> !id.isEmpty())
        .collect(Collectors.toSet());

    } catch (IOException ex) {
      log.error("fail to retrieve companions: {}", ex.getMessage());
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
