package xfp.jmh.test.numbers;

import org.junit.jupiter.api.Test;

import com.upokecenter.numbers.ERational;

import xfp.java.test.Common;
import xfp.jmh.numbers.ERationals;

//----------------------------------------------------------------
/** Test desired properties of ERational. 
 * <p>
 * <pre>
 * mvn -q clean -Dtest=xfp/jmh/test/numbers/ERationalTest test > ERationalTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-22
 */

public final class ERationalTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void testRounding () {
    Common.doubleRoundingTests(
      ERationals::toERational,
      ERational::FromDouble,
      q -> ((ERational) q).ToDouble(),
      (q0,q1) -> ((ERational) q0).Subtract((ERational) q1).Abs(),
      q -> ERationals.toHexString((ERational) q)); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
