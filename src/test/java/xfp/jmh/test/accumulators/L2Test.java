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
 * mvn -q -Dtest=xfp/java/test/accumulators/SumTest test > SumTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-02
 */

public final class L2Test {

  @Test
  public final void l2Tests () {
    Debug.DEBUG = false;
    Debug.println();
    Debug.println(Classes.className(this));
    Common.l2Tests(
      Common.generators(Shared.DIM),
      Common.makeAccumulators(Shared.accumulators()),
      RBFAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
