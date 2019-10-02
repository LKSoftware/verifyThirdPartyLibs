package de.kolatanet.utils.filter;

import de.kolatanet.utils.basemodel.Library;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * TODO: what is the purpose of this class?
 * @author Leon Kolata
 */
public class GroupIdFilter implements Predicate<Library> {

  private final Collection<String> groupIds;

  /**
   * Loads the properties and creates a list of ignored groupIds to filter.
   */
  public GroupIdFilter(Collection<String> ids) {
    groupIds = ids;
  }

  @Override
  public boolean test(Library library) {
    return groupIds.contains(library.getGroupId());
  }
}
