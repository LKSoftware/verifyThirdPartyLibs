package de.kolatanet.utils;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.gradle.DependencyFinder;
import de.kolatanet.utils.maven.LicenseChecker;
import de.kolatanet.utils.maven.UpdateCheckerMavenCentral;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gradle Plugin Task for verifying dependencies.
 *
 * @author Leon Kolata
 */
public class VerifyTask extends DefaultTask {

  private static final Logger LOG = LoggerFactory.getLogger(VerifyTask.class);

  private final Project currentProject;

  /**
   * Dependency scope Input in config block as String scopes seperated by ','.
   */
  @Input
  public String scope;

  @Input
  public String proxyHost;
  @Input
  public int proxyPort;

  /**
   * Keeps track of currentProject incase of multiproject.
   */
  public VerifyTask() {
    currentProject = getProject();
  }

  @TaskAction
  void runVerify() {
    LOG.warn("Verify Dependencies");
    DependencyFinder dep = new DependencyFinder(getDependencyScope());
    UpdateCheckerMavenCentral uc = new UpdateCheckerMavenCentral(getProxy());

    LicenseChecker lc = new LicenseChecker(getProxy());

    Collection<Library> result = dep.findDependencies(currentProject)
        .stream()
        .map(lc)
        .map(uc)
        .collect(Collectors.toList());

    try {
      VerifyReporter reporter = new VerifyReporter(
          currentProject.getBuildDir().getAbsolutePath() + "/verifyGradleDependencies",
          currentProject.getName());

      Path path = reporter.createReport(getDependencyScope(), result);
      LOG.warn("Created verify report: " + path);

    } catch (Exception e) {
      LOG.error("Failed while creating verify report: ", e);
    }

  }

  private Collection<String> getDependencyScope() {
    if (scope != null) {
      return Arrays.asList(scope.trim().split(","));
    }
    return Collections.emptyList();
  }

  private Proxy getProxy() {
    if (proxyHost != null && proxyPort != 0) {
      return new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }
    return Proxy.NO_PROXY;
  }

}
