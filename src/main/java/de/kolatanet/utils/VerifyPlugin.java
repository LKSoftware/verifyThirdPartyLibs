package de.kolatanet.utils;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * VerifyPlugin entry Point Applys and adds the rule to run after evaluation
 */
public class VerifyPlugin implements Plugin<Project> {

  private static final String TASK_NAME_VERIFY = "verify";
  private VerifyTask verifyTask;

  /**
   * GradleApi entry point
   */
  @Override
  public void apply(Project project) {
    verifyTask = project.getTasks().create(TASK_NAME_VERIFY, VerifyTask.class);
    verifyTask.setGroup("Verification");
    verifyTask.setDescription("Used to verify dependencies. (Updates, Licenses)");

    project.afterEvaluate(this::addDependency);
  }

  /**
   * Adding the verify task to the check task.
   */
  private void addDependency(Project project) {
    try {
      project.getTasks().getByName("check").dependsOn(verifyTask);
    } catch (Exception e) {
      System.out.println("Error: applying the verifyThirdPartyLibs plugin requires Java-Plugin");
    }
  }

}
