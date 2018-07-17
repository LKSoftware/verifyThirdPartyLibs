package de.kolatanet.utils.basemodel;

import java.util.ArrayList;
import java.util.List;

public class LibraryExclusionJson {

  List<Exclusion> libraries = new ArrayList<>();

  public List<Exclusion> getLibraries() {
    return libraries;
  }

  public class Exclusion {

    String dependency;

    String version;

    String reason;

    String creator;

    public String getDependency() {
      return dependency;
    }

    public String getVersion() {
      return version;
    }

    public String getReason() {
      return reason;
    }

    public String getCreator() {
      return creator;
    }
  }
}
