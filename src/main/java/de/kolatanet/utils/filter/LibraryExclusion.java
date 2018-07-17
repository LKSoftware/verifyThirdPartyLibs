package de.kolatanet.utils.filter;

import com.google.gson.Gson;
import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.LibraryExclusionJson;
import de.kolatanet.utils.basemodel.LibraryExclusionJson.Exclusion;
import de.kolatanet.utils.utils.FileLocator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import org.gradle.api.Project;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class LibraryExclusion implements Predicate<Library> {

  private static final Logger LOG = LoggerFactory.getLogger(LibraryExclusion.class);

  private LibraryExclusionJson libraryExclusionJson;

  public LibraryExclusion(Project project) {
    try {
      List<Path> file = FileLocator.getInstance()
          .locate(project.getRootDir().toPath(), "json", "LibraryExclusions");

      if (!file.isEmpty()) {
        String json = FileUtils.readFileToString(file.get(0).toFile());
        libraryExclusionJson = new Gson().fromJson(json, LibraryExclusionJson.class);
      }
    } catch (IOException e) {
      LOG.info("You did not define a LibraryExclusions.json");
    }
  }

  public Exclusion getExclusion(Library library) {
    return libraryExclusionJson.getLibraries().stream()
        .filter(exclusion -> exclusion.getDependency()
            .equals(library.getGroupId() + ":" + library.getArtifactId()))
        .findFirst().orElse(null);
  }

  @Override
  public boolean test(Library library) {
    return getExclusion(library) != null;
  }
}
