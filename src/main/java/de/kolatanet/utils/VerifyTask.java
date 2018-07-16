package de.kolatanet.utils;

import de.kolatanet.utils.basemodel.Library;
import de.kolatanet.utils.gradle.DependencyFinder;
import de.kolatanet.utils.gradle.UpdateCheckerMaven;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;


public class VerifyTask extends DefaultTask {

  private final Project currentProject;

  @Input
  public String scope;

  @Input
  public String proxyHost;
  @Input
  public int proxyPort;

  public VerifyTask() {
    currentProject = getProject();
  }

  @TaskAction
  void runVerify() {
    System.out.println("Verify ThirdPartyLibs");
    DependencyFinder dep = new DependencyFinder(getDependencyScope());
    UpdateCheckerMaven uc =
        getProxy() != null ? new UpdateCheckerMaven(getProxy()) : new UpdateCheckerMaven();

    Collection<Library> result = dep.findDependencies(currentProject)
        .stream()
        .map(uc)
        .collect(Collectors.toList());

    try {
      VerifyReporter reporter = new VerifyReporter(currentProject.getBuildDir()
          .toPath()
          .resolve("verifyThirdPartyLibs"),
          currentProject.getName());

      Path path = reporter.createReport(getDependencyScope(), result);
      System.out.println("Created verify report: " + path);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private Collection<String> getDependencyScope() {
    return Arrays.asList(scope.trim().split(","));
  }

  private Proxy getProxy() {
    if (proxyHost != null && proxyPort != 0) {
      return new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }
    return null;
  }

}
