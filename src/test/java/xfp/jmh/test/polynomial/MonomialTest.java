package xfp.jmh.test.polynomial;

import org.junit.jupiter.api.Test;

import xfp.jmh.polynomial.MonomialBigFloat0;
import xfp.jmh.polynomial.MonomialDoubleBF0;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of monimial calculators.
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/polynomial/MonomialTest test > MT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-10
 */

public final class MonomialTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void dbf () { 
    Common.monomial(MonomialDoubleBF0.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bf () { 
    Common.monomial(MonomialBigFloat0.class); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
