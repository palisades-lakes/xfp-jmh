package xfp.jmh.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Test;

import xfp.java.Classes;
import xfp.java.Debug;
import xfp.jmh.accumulators.RationalAccumulator;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn test -Dtest=xfp/java/test/accumulators/RationalFloatAccumulatorTest > RationalFloatAccumulatorTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-14
 */

public final class RationaFloatAccumulatorTest {

  //--------------------------------------------------------------
  private static final int DIM = 2 * 1024;
  private static final List<String> accumulators =
    List.of("xfp.java.accumulators.RationalFloatAccumulator");
  @Test
  public final void tests () {
    Debug.DEBUG = false;
    Debug.println();
    Debug.println(Classes.className(this));
    Common.sumTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      RationalAccumulator.make()); 
    Common.l2Tests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      RationalAccumulator.make()); 
    Common.dotTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      RationalAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
