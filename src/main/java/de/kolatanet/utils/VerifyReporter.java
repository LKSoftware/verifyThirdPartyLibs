package de.kolatanet.utils;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.Report;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Creates a report for verification process.
 *
 * @author Leon Kolata
 */
class VerifyReporter {

  private static final String FILE_NAME = "-report.json";

  private final File outputFile;

  /**
   * Creates a outputdir and the file to write to.
   */
  VerifyReporter(String outputDir, String project) throws IOException {
    Path out = Paths.get(outputDir).resolve("verifyGradleDependencies");
    if (!Files.exists(out)) {
      out.toFile().mkdir();
    }
    outputFile = out.resolve(project + FILE_NAME).toFile();
    outputFile.createNewFile();

  }

  /**
   * Fills the report with data.
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
