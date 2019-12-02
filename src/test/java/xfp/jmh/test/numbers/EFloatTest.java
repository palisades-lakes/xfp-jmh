package xfp.jmh.test.numbers;

import org.junit.jupiter.api.Test;

import com.upokecenter.numbers.EFloat;

import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of EFloat.
 * <p>
 * <pre>
 * mvn -Dtest=xfp/java/test/numbers/EFloatTest test > EFloatTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-12-01
 */

public final class EFloatTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void testRounding () {

    Common.doubleRoundingTests(
      null,
      EFloat::FromDouble,
      q -> ((EFloat) q).ToDouble(),
      (q0,q1) -> ((EFloat) q0).Subtract((EFloat) q1).Abs(),
      Object::toString, 
      Common::compareTo, Common::compareTo);

    Common.floatRoundingTests(
      null,
      EFloat::FromSingle,
      q -> ((EFloat) q).ToSingle(),
      (q0,q1) -> ((EFloat) q0).Subtract((EFloat) q1).Abs(),
      Object::toString, 
      Common::compareTo, Common::compareTo);

  }
  //--------------------------------------------------------------
}
//--------------------------------------------------------------
