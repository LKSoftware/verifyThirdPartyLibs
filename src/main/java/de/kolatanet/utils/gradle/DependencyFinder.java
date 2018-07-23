package de.kolatanet.utils.gradle;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.basemodel.LibraryList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ResolvedDependency;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * {@link DependencyFinder} uses gradle api to get all dependencies by the defined configurations.
 *
 * @author Leon Kolata
 */
public class DependencyFinder {

  private static final Logger LOG = LoggerFactory.getLogger(DependencyFinder.class);

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
   */

  public LibraryList findDependencies(Project project) {

    project.getAllprojects().forEach(this::find);

    return DEPENDENCY_LIST;
  }

  /**
   * Find all dependencies in a gradle project with defined configurations.
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
              .forEach(dep -> addLibrary(dep, project, scope));
        }
      } catch (Exception e) {
        LOG.warn("Could not find configuration for scope: " + scope);
      }
    }

  }

  private void addLibrary(Dependency dependency, Project project, String scope) {
    String version = dependency.getVersion();
    if (version != null && version.equals("null") || version == null) {
      Optional<ResolvedDependency> found =
          project.getConfigurations().getByName(scope)
              .getResolvedConfiguration()
              .getFirstLevelModuleDependencies().stream()
              .filter(dep -> dep.getModuleGroup().equals(dependency.getGroup())
                  && dep.getModuleName().equals(dependency.getName())).findFirst();

      addResolvedVersion(dependency, project, scope,
          found.map(ResolvedDependency::getModuleVersion).orElse("undefined"));
      return;
    }
    addResolvedVersion(dependency, project, scope, version);
  }

  private void addResolvedVersion(Dependency dep, Project project, String scope, String version) {
    DEPENDENCY_LIST
        .add(new Library(dep.getGroup(), dep.getName(), version)
            .addOriginInProject(project.getName()).addDependencyScope(scope));
  }
}