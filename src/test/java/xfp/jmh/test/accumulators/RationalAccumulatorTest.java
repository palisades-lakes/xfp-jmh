package xfp.jmh.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Test;

import xfp.java.Classes;
import xfp.java.Debug;
import xfp.java.test.Common;
import xfp.jmh.accumulators.ERationalAccumulator;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn -q clean test -Dtest=xfp/jmh/test/accumulators/RationalAccumulatorTest > RationalAccumulatorTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-22
 */

public final class RationalAccumulatorTest {

  //--------------------------------------------------------------
  private static final int DIM = 256;
  private static final List<String> accumulators =
    List.of("xfp.jmh.accumulators.RationalAccumulator");
  @Test
  public final void tests () {
    Debug.DEBUG = false;
    Debug.println();
    Debug.println(Classes.className(this));
    Common.sumTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      ERationalAccumulator.make()); 
//    Common.l2Tests(
//      Common.generators(DIM),
//      Common.makeAccumulators(accumulators),
//      ERationalAccumulator.make()); 
//    Common.dotTests(
//      Common.generators(DIM),
//      Common.makeAccumulators(accumulators),
//      ERationalAccumulator.make());
    }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
