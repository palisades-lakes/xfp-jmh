package xfp.jmh.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import xfp.java.test.Common;
import xfp.java.test.accumulators.ERationalAccumulator;

//----------------------------------------------------------------
/** Test summation algorithms.
 * <p>
 * <pre>
 * mvn test -Dtest=xfp/java/test/accumulators/SpireRationalAccumulatorTest > SpireRationalAccumulatorTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-11-30
 */

public final class SpireRationalAccumulatorTest {

  //--------------------------------------------------------------
  private static final int DIM = 256;
  private static final List<String> accumulators =
    List.of("xfp.jmh.accumulators.SpireRationalAccumulator");

  @SuppressWarnings("static-method")
  @Test
  public final void tests () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println(Classes.className(this));
    Assertions.assertThrows(
      AssertionFailedError.class,
      () -> {
        Common.sumTests(
          Common.generators(DIM),
          Common.makeAccumulators(accumulators),
          ERationalAccumulator.make()); },
      "fails becasue spire.math.Rational doesn't round to double correctly");

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
