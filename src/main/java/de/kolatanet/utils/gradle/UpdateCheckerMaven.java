package de.kolatanet.utils.gradle;

import de.kolatanet.utils.basemodel.Library;


public class UpdateCheckerMaven
{

  private static final String MAVEN_BASE_URL = "http://search.maven.org/#search|ga|1|g:\"{groupId}\"|a:\"{artifactId}\"";

  private static final String URL_GROUP = "{groupId}";

  private static final String URL_ARTIFACT = "{artifactId}";

  public String getLatestVersion(Library lib)
  {
    return retrieveMavenReport(MAVEN_BASE_URL.replace(URL_GROUP, lib.getGroupId())
                                             .replace(URL_ARTIFACT, lib.getArtifactId()));
  }

  private String retrieveMavenReport(String url)
  {
    //TODO Implement
    return "";
  }
}
