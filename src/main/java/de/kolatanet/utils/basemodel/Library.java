package de.kolatanet.utils.basemodel;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a library model containing all necessary info about the library and it's origin.
 *
 * @author Leon Kolata
 */
public class Library implements Serializable {

  private static final long serialVersionUID = -5179591433114199485L;

  private final String groupId;

  private final String artifactId;

  private final String version;

  private final Collection<String> comments = new HashSet<>();

  private final Collection<String> dependencyScope = new HashSet<>();

  private Path file;

  private Collection<String> licenses = new HashSet<>();

  private Collection<String> originsInProjects = new HashSet<>();

  /**
   * Creates a library with all necessary info.
   */
  public Library(String groupId,
      String artifactId,
      String version,
      Collection<String> licenses,
      Collection<String> originsInProjects) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.licenses = licenses;
    this.originsInProjects = originsInProjects;
  }

  /**
   * Creates a new simple library.
   */
  public Library(String groupId, String artifactId, String version) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
  }

  /**
   * Returns a Library from a dependency string "groupId:artifactId:version". Initial object has
   * empty lists for license and origins.
   */
  public static Library libraryFromString(String library) {
    String[] libraryBlocks = library.split(":");
    Collection<String> empty = Collections.emptyList();
    return new Library(libraryBlocks[0], libraryBlocks[1], libraryBlocks[2], empty, empty);
  }

  /**
   * Adds a License to the library.
   */
  public Library addLicense(String license) {
    if (!licenses.contains(license)) {
      licenses.add(license);
    }
    return this;
  }

  /**
   * Adds a project as origin for the library.
   */
  public Library addOriginInProject(String origin) {
    if (!originsInProjects.contains(origin)) {
      originsInProjects.add(origin);
    }
    return this;
  }

  /**
   * Adds a comment to the library.
   */
  public Library addComment(String comment) {
    if (!comments.contains(comment)) {
      comments.add(comment);
    }
    return this;
  }


  /**
   * Sets the representing file of the library if present.
   */
  public Library setFile(Path file) {
    this.file = file;
    return this;
  }

  /**
   * Sets the dependencyScope of a library.
   */
  public Library addDependencyScope(String dependencyScope) {
    this.dependencyScope.add(dependencyScope);
    return this;
  }

  /**
   * Returns the groupId.
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * Returns the artifactId.
   */
  public String getArtifactId() {
    return artifactId;
  }

  /**
   * Returns the version.
   */
  public String getVersion() {
    if (version == null) {
      return "undefined";
    }
    return version;
  }

  /**
   * Returns all origins of the library.
   */
  public Collection<String> getOriginsInProjects() {
    return originsInProjects;
  }

  /**
   * Returns the dependencyScope
   */
  public Collection<String> getDependencyScope() {
    return dependencyScope;
  }

  /**
   * Returns a string formatted as dependency "groupId:artifactId:version"
   */
  public String asDependency() {
    return groupId + ':' + artifactId + ':' + version;
  }

  /**
   * Generated equals method.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Library that = (Library) o;

    return Objects.equals(groupId,that.groupId) &&
        Objects.equals(artifactId, that.artifactId) &&
        Objects.equals(version, that.version);
  }

  /**
   * Generated hashCode method.
   */
  @Override
  public int hashCode() {
    int result = groupId != null ? groupId.hashCode() : 0;
    result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
    result = 31 * result + (version != null ? version.hashCode() : 0);
    return result;
  }

  /**
   * Returns the object as String.
   */
  @Override
  public String toString() {
    return "Library{" + "groupId='" + groupId + '\'' + ", artifactId='" + artifactId + '\''
        + ", version='"
        + version + '\'' + ", file=" + file +  ", licenses=" + licenses + ", comments=" + comments
        + ", originsInProjects=" + originsInProjects + '}';
  }
}
