package de.kolatanet.utils.gradle;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.LibraryList;
import java.util.ArrayList;
import java.util.Collection;
import org.gradle.api.Project;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;


/**
 * {@link DependencyFinder} uses gradle api to get all dependencies by the defined configurations.
 *
 * @author Leon Kolata
 */
public class DependencyFinder {

  private final LibraryList DEPENDENCY_LIST = new LibraryList();

  private final Collection<String> DEPENDENCY_SCOPE = new ArrayList<>();

  /**
   * Creates DependencyFinder and adds the given scope list.
   */
  public DependencyFinder(Collection<String> dependencyScope) {
    DEPENDENCY_SCOPE.addAll(dependencyScope);
  }

  /**
   * Asks gradle api about dependencies and returns a LibraryList.
   * @param project
   * @return
   */
  public LibraryList findDependencies(Project project) {

    project.getAllprojects().stream().forEach(this::find);

    return DEPENDENCY_LIST;
  }

  /**
   * Find all dependencies in a gradle project with defined configurations.
   * @param project
   */
  private void find(Project project) {

    for (String scope : DEPENDENCY_SCOPE) {
      try {
        if (!project.getConfigurations().isEmpty()) {
          project.getConfigurations()
              .getByName(scope)
              .getAllDependencies()
              .stream()
              .filter(dep -> dep instanceof DefaultExternalModuleDependency)
              .forEach(dep -> DEPENDENCY_LIST.add(new Library(dep.getGroup(),
                  dep.getName(),
                  dep.getVersion()).addOriginInProject(project.getName())
                  .addDependencyScope(scope)));
        }
      } catch (Exception e) {

      }
    }

  }
}
