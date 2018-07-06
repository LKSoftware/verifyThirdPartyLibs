package de.kolatanet.utils;

import org.gradle.api.Plugin;
import org.gradle.api.Project;


public class VerifyPlugin implements Plugin<Project>
{

  private static final String TASK_NAME_VERIFY = "verify";

  @Override
  public void apply(Project project)
  {
    project.getTasks().create(TASK_NAME_VERIFY, VerifyTask.class);
  }
}
