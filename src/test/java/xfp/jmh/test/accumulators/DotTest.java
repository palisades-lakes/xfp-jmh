package xfp.jmh.test.accumulators;

import org.junit.jupiter.api.Test;

import xfp.java.Classes;
import xfp.java.Debug;
import xfp.java.accumulators.RBFAccumulator;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/numbers/DotTest test > DotTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-02
 */

//no actual tests here (yet)

public final class DotTest {

  @Test
  public final void dotTests () {
    Debug.DEBUG=false;
    Debug.println();
    Debug.println(Classes.className(this));
    Common.dotTests(
      Common.generators(Shared.DIM),
      Common.makeAccumulators(Shared.accumulators()),
      RBFAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
