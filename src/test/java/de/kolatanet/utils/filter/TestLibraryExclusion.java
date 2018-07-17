package de.kolatanet.utils.filter;

import de.kolatanet.utils.basemodel.Library;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.gradle.api.Project;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Test;

public class TestLibraryExclusion {

  @Test
  public void testExclusion() throws IOException {
    Project testProject = ProjectBuilder.builder().withName("testProject").build();
    testProject.getPlugins().apply("java");

    File f = Files
        .createFile(Paths.get(testProject.getRootDir().toString(), "LibraryExclusions.json"))
        .toFile();

    FileUtils.copyFile(Paths.get("LibraryExclusions.json").toAbsolutePath().toFile(),
        Paths.get(testProject.getRootDir().toString(), "LibraryExclusions.json").toFile());

    LibraryExclusion systemUnderTest = new LibraryExclusion(testProject);

    Assert.assertTrue(systemUnderTest.test(new Library("de.example", "example", "1.0")));
  }

}
