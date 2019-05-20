package xfp.jmh.test.numbers;

import org.junit.jupiter.api.Test;

import xfp.java.test.Common;
import xfp.jmh.numbers.BigInteger;

//----------------------------------------------------------------
/** Test desired properties of integer implementations.
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/jmh/test/numbers/BigIntegerTest test > BigIntegerTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-29
 */

public final class BigIntegerTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bigInteger () {
    Common.integerTest(
      (z) -> BigInteger.valueOf(z),
      (z) -> ((BigInteger) z).jmBigIntegerValue()); }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
