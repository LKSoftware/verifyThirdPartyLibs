package de.kolatanet.utils.basemodel;

import java.util.ArrayList;
import java.util.Optional;


/**
 * LibraryList manages the libraries adding to prevent duplications.
 *
 * @author Leon Kolata
 */
public class LibraryList extends ArrayList<Library>
{

  private static final long serialVersionUID = -656423073232622830L;

  /**
   * Call constructor on {@link ArrayList}
   */
  public LibraryList()
  {
    super();
  }

  /**
   * Adds a dependency to the dependency list if not already added.
   *
   * @return true if dependency added.
   */
  @Override
  public boolean add(Library dependency)
  {
    Optional<Library> found = stream().filter(dep -> dep.equals(dependency)).findAny();
    if (found.isPresent())
    {
      found.get().getOriginsInProjects().addAll(dependency.getOriginsInProjects());
      found.get().getDependencyScope().addAll(dependency.getDependencyScope());
      return false;
    }
    super.add(dependency);
    return true;
  }

}
