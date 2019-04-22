package xfp.jmh.test.numbers;

import org.apache.commons.math3.fraction.BigFraction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import xfp.java.numbers.Numbers;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of BigFractions. 
 * <p>
 * Failed, hacky attempts to correct BigFraction.doubelValue(),
 * so that rounds correctly (half-even), including
 * roundtrip consistency:
 *  <code>new BigFraction(double).doubleValue()</code>
 *  should return the <code>double</code> you start with.
 *  May revive in the future...
 * <pre>
 * mvn -Dtest=xfp/java/test/numbers/BigFractionTest test > BigFractionTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-22
 */

public final class BigFractionTest {

  // TODO: BigFraction.doubleValue doesn't round to nearest. 
  // Test below fails with both random double -> BigFraction
  // and random long,long -> BigFraction.

  @SuppressWarnings({ "static-method" })
  @Test
  public final void testRounding () {

    Assertions.assertThrows(
      AssertionFailedError.class,
      () -> {
        Common.doubleRoundingTests(
          (i0,i1) -> new BigFraction(i0,i1),
          x -> new BigFraction(x),
          Numbers::doubleValue,
          (q0,q1) -> ((BigFraction) q0).subtract((BigFraction) q1).abs(),
          Object::toString); },
      "BigFraction doesn't round correctly"); 
    
    Assertions.assertThrows(
      AssertionFailedError.class,
      () -> {
        Common.floatRoundingTests(
          (i0,i1) -> new BigFraction(i0,i1),
          x -> new BigFraction(x),
          Numbers::floatValue,
          (q0,q1) -> ((BigFraction) q0).subtract((BigFraction) q1).abs(),
          Object::toString); },
      "BigFraction doesn't round correctly"); 
    
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
