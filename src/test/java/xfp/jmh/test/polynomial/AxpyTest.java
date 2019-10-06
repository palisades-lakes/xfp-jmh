package xfp.jmh.test.polynomial;

import org.junit.jupiter.api.Test;

import xfp.jmh.polynomial.BigFloat0Axpy;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of axpy calculators.
 * <p>
 * <pre>
 * mvn -c -Dtest=xfp/jmh/test/polynomial/AxpyTest test > AT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-03
 */

public final class AxpyTest {

  
  @SuppressWarnings({ "static-method" })
  @Test
  public final void bigFloat0Axpy () { 
    Common.daxpy(new BigFloat0Axpy()); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
