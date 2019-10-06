package xfp.jmh.test.polynomial;

import org.junit.jupiter.api.Test;

import xfp.java.test.Common;
import xfp.jmh.polynomial.BigFloat0Cubic;

//----------------------------------------------------------------
/** Test desired properties of axpy calculators.
 * <p>
 * <pre>
 * mvn -c -Dtest=xfp/jmh/test/polynomial/CubicTest test > CT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-05
 */

public final class CubicTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bigFloatCubic () { 
    Common.cubic(BigFloat0Cubic.class); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
