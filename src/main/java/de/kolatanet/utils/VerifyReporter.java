package de.kolatanet.utils;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.Report;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;


public class VerifyReporter {

  private static final String FILE_NAME = "-report.json";

  private final File outputFile;

  public VerifyReporter(Path outputDir, String project) throws IOException {
    if (!Files.exists(outputDir)) {
      outputDir.toFile().mkdir();
    }
    outputFile = outputDir.resolve(project + FILE_NAME).toFile();
    outputFile.createNewFile();

  }

  public Path createReport(Collection<String> dependencyScope, Collection<Library> dependencies)
      throws IOException {
    Report report = new Report(dependencyScope, dependencies);

    try (FileWriter fw = new FileWriter(outputFile)) {
      fw.write(report.toString());
    }
    return outputFile.toPath();
  }

}
