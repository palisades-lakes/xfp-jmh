package xfp.jmh.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Test;

import xfp.java.test.Common;
import xfp.java.accumulators.ERationalAccumulator;

//----------------------------------------------------------------
/** Test summation algorithms.
 * <p>
 * <pre>
 * mvn test -Dtest=xfp/java/test/accumulators/BigFloatAccumulatorTest > BigFloatAccumulatorTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-12
 */

public final class BigFloatAccumulatorTest {

  //--------------------------------------------------------------
  private static final int DIM = 256;
  private static final List<String> accumulators =
    List.of("xfp.jmh.accumulators.BigFloatAccumulator0");
  @SuppressWarnings("static-method")
  @Test
  public final void tests () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println(Classes.className(this));
    Common.sumTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      ERationalAccumulator.make());
    Common.l2Tests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      ERationalAccumulator.make());
    Common.dotTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      ERationalAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
