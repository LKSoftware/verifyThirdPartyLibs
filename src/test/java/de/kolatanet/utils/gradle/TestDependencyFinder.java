package de.kolatanet.utils.gradle;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.LibraryList;
import java.util.Arrays;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for DependencyFinder.
 */
public class TestDependencyFinder {

  @Test
  public void canFindDependencies() {
    DependencyFinder systemUnderTest = new DependencyFinder(Arrays.asList("testCompile"));
    Project testProject = ProjectBuilder.builder().withName("testProject").build();
    testProject.getPlugins().apply("java");
    testProject.getDependencies().add("testCompile", "junit:junit:4.12");

    LibraryList result = systemUnderTest.findDependencies(testProject);

    Assert.assertThat(result, Matchers.hasItem(new Library("junit", "junit", "4.12")));

  }

}
