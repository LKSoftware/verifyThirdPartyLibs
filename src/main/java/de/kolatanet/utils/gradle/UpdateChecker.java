package de.kolatanet.utils.gradle;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.utils.FileLocator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * {@link UpdateChecker} lazy component to get all updatable dependencies. To work properly you have
 * to apply the plugin 'com.github.ben-manes:gradle-versions-plugin'
 *
 * @author Leon Kolata
 */
@Deprecated
public class UpdateChecker implements Function<Library, Library> {

  private static final Pattern REGEX_UPDATE = Pattern.compile(
      " - ([\\w.-]+:[\\w.-]+) \\[(\\d[\\w.-]+) -> (\\d+[\\w.-]*)\\]+");

  private final Map<String, String> dependencies = new HashMap<>();

  /**
   * Reads all report files and parses them into a Map to check against.
   */
  public UpdateChecker(Path dir) {
    try {
      readReports(dir);

    } catch (IOException e) {

    }
  }

  /**
   * Searches for all reports and parses them into a Map.
   */
  private void readReports(Path dir) throws IOException {
    List<Path> depUpdates = FileLocator.getInstance()
        .locate(dir, "txt", "report")
        .stream()
        .filter(path -> path.getParent()
            .getFileName()
            .toString()
            .equals("dependencyUpdates"))
        .collect(Collectors.toList());

    List<String> lines = new ArrayList<>();
    for (Path path : depUpdates) {
      lines.addAll(Files.readAllLines(path));
    }

    lines.forEach(this::parseReportLine);
  }

  /**
   * Parses a line and checks if the regex matches.
   *
   * @param line report line
   */
  private void parseReportLine(String line) {
    final Matcher matcher = REGEX_UPDATE.matcher(line);

    if (matcher.matches()) {
      dependencies.put(matcher.group(1), matcher.group(2));
    }
  }

  /**
   * Checks whether a {@link Library} is updatable.
   */

  @Override
  public Library apply(Library library) {

    String dependency = library.getGroupId() + ":" + library.getArtifactId();

    if (dependencies.containsKey(dependency)) {
      library.addComment("Latest version " + dependencies.get(dependency) + " is not used!");
    }
    return library;
  }
}
