package de.kolatanet.utils;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.Report;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Creates a report for verification process.
 *
 * @author Leon Kolata
 */
public class VerifyReporter {

  private static final String FILE_NAME = "-report.json";

  private final File outputFile;

  /**
   * Creates a outputdir and the file to write to.
   * @param outputDir
   * @param project
   * @throws IOException
   */
  public VerifyReporter(Path outputDir, String project) throws IOException {
    if (!Files.exists(outputDir)) {
      outputDir.toFile().mkdir();
    }
    outputFile = outputDir.resolve(project + FILE_NAME).toFile();
    outputFile.createNewFile();

  }

  /**
   * Fills the report with data.
   * @param dependencyScope
   * @param dependencies
   * @return
   * @throws IOException
   */
  public Path createReport(Collection<String> dependencyScope, Collection<Library> dependencies)
      throws IOException {
    Report report = new Report(dependencyScope, dependencies);

    try (FileWriter fw = new FileWriter(outputFile)) {
      fw.write(report.toString());
    }
    return outputFile.toPath();
  }

}
