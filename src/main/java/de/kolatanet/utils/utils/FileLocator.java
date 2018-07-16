package de.kolatanet.utils.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * {@link FileLocator} is used for locating files in the filesystem, you can define search criteria
 * and get all files matching to these.
 */
public class FileLocator {

  private static final FileLocator INSTANCE = new FileLocator();

  private List<String> excludes = new ArrayList<>();

  private FileLocator() {
    //SINGLETON
  }

  /**
   * Returns the singleton instance.
   */
  public static FileLocator getInstance() {
    return INSTANCE;
  }

  /**
   * Locates all files which apply to the search criteria
   */
  public List<Path> locate(Path dir, String fileType, String fileName, String... excludes)
      throws IOException {
    this.excludes = Arrays.asList(excludes);

    List<Path> result = Files.walk(dir)
        .filter(path -> !isExcluded(path))
        .filter(path -> path.toFile().isFile())
        .filter(path -> path.getFileName().toString().endsWith('.' + fileType))
        .collect(Collectors.toList());

    if (fileName.equals("*")) {
      return result;
    }
    return result.stream()
        .filter(path -> path.getFileName().toString().contains(fileName))
        .collect(Collectors.toList());
  }

  /**
   * Checks whether a path is excluded or not.
   */
  private boolean isExcluded(Path path) {
    for (Path part : path) {
      if (excludes.contains(part.toString())) {
        return true;
      }
    }
    return false;
  }
}
