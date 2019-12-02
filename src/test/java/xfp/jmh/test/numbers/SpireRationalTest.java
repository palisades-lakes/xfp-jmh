package xfp.jmh.test.numbers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import spire.math.Rational;
import xfp.java.test.Common;
import xfp.jmh.numbers.SpireRationals;

//----------------------------------------------------------------
/** Test desired properties of ERational.
 * <p>
 * <pre>
 * mvn -c clean -Dtest=xfp/jmh/test/numbers/SpireRationalTest test > SpireRationalTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-12-01
 */

public final class SpireRationalTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void testRounding () {

    Assertions.assertThrows(
      AssertionFailedError.class,
      () -> {
    Common.doubleRoundingTests(
      SpireRationals::toRational,
      SpireRationals::toRational,
      q -> ((Rational) q).doubleValue(),
      (q0,q1) -> ((Rational) q0).$minus((Rational) q1).abs(),
      q -> q.toString(), 
      Common::compareTo, Common::compareTo); },
      "spire.math.Rational doesn't round to double correctly");

    Common.floatRoundingTests(
      SpireRationals::toRational,
      SpireRationals::toRational,
      q -> ((Rational) q).floatValue(),
      (q0,q1) -> ((Rational) q0).$minus((Rational) q1).abs(),
      q -> q.toString(), 
      Common::compareTo, Common::compareTo); 

  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
