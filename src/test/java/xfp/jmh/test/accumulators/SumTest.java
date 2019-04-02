package xfp.jmh.test.accumulators;

import org.junit.jupiter.api.Test;

import xfp.java.Classes;
import xfp.java.Debug;
import xfp.java.accumulators.RBFAccumulator;
import xfp.java.test.Common;
import xfp.jmh.Shared;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/numbers/SumTest test > SumTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-01
 */

public final class SumTest {

  @Test
  public final void sumTests () {
    Debug.DEBUG=false;
    Debug.println();
    Debug.println(Classes.className(this));
    Common.sumTests(
      Common.generators(Shared.DIM),
      Common.makeAccumulators(Shared.accumulators()),
      RBFAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
