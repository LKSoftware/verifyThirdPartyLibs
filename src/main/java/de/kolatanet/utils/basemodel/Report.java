package de.kolatanet.utils.basemodel;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.Date;


/**
 * Report contaning all the dependencies as Library object. Object will be converted to json when
 * toString is called.
 */
public class Report {

  //Containing all configurations
  private final Collection<String> dependencyScopes;

  private final Collection<Library> dependencies;

  //Date of report creation
  private final Date creationDate;

  /**
   * Creates a new report with all dependencies and with the dependency scopes.
   */
  public Report(Collection<String> dependencyScopes, Collection<Library> dependencies) {
    this.dependencyScopes = dependencyScopes;
    this.dependencies = dependencies;
    creationDate = new Date();
  }

  @Override
  public String toString() {
    return new Gson().toJson(this, this.getClass());
  }

}
