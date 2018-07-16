package de.kolatanet.utils.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for FileLocator
 */
public class TestFileLocator {

  @Test
  public void canFindFile() throws Exception {
    FileLocator systemUnderTest = FileLocator.getInstance();

    List<Path> result = systemUnderTest.locate(Paths.get("").toAbsolutePath(), "gradle", "build");
    Assert.assertThat("main build gradle", result,
        Matchers.hasItems(Paths.get("build.gradle").toAbsolutePath()));

  }

  @Test
  public void canFindFiles() throws Exception {
    FileLocator systemUnderTest = FileLocator.getInstance();

    List<Path> result = systemUnderTest.locate(Paths.get("").toAbsolutePath(), "gradle", "*");
    Assert.assertThat("main settings gradle", result,
        Matchers.hasItems(Paths.get("settings.gradle").toAbsolutePath()));
  }

  @Test
  public void canExclude() throws Exception {
    FileLocator systemUnderTest = FileLocator.getInstance();

    List<Path> result = systemUnderTest
        .locate(Paths.get("").toAbsolutePath(), "html", "index", "html-report");
    Assert.assertThat("result excluded html-report dir", result,
        Matchers.not(Matchers.contains(Paths.get("html-report/index.html").toAbsolutePath())));

  }

}
