package de.kolatanet.utils.basemodel;

import java.util.Collection;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for {@link Library}, more to come ?
 *
 * @author Leon Kolata
 */
public class TestLibrary {

  /**
   * Simple smoke test for equals function.
   */
  @Test
  public void testSmoking() {
    Collection<String> empty = Collections.emptyList();

    Library systemUnderTest = new Library("test", "test", "1.0", empty, empty);

    Library sameObject = systemUnderTest;

    Library same = new Library("test", "test", "1.0", empty, empty);

    Library notSame = new Library("testing", "testing", "2.0", empty, empty);

    Assert.assertTrue("equality object test", systemUnderTest.equals(sameObject));
    Assert.assertTrue("equality test", systemUnderTest.equals(same));
    Assert.assertFalse("not equal test", systemUnderTest.equals(notSame));
  }


}
