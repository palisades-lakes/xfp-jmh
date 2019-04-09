package xfp.jmh.test.accumulators;

import static xfp.java.test.Common.generators;
import static xfp.java.test.Common.makeAccumulators;
import static xfp.java.test.Common.sumTests;
import static xfp.jmh.test.accumulators.Shared.accumulators;

import org.junit.jupiter.api.Test;

import xfp.java.Debug;
import xfp.java.accumulators.RBFAccumulator;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn -Dtest=xfp/jmh/test/accumulators/SumTest test > SumTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-06
 */

public final class SumTest {

  @SuppressWarnings("static-method")
  @Test
  public final void sum () {
    Debug.DEBUG=false;
    Debug.println();
    Debug.println("sum");
    sumTests(
      generators(Shared.TEST_DIM),
      makeAccumulators(accumulators()),
      RBFAccumulator.make()); }

//  @SuppressWarnings("static-method")
//  @Test
//  public final void l2 () {
//    Debug.DEBUG = false;
//    Debug.println();
//    Debug.println("l2");
//    l2Tests(
//      generators(Shared.TEST_DIM),
//      makeAccumulators(accumulators()),
//      RBFAccumulator.make()); }
//
//  @SuppressWarnings("static-method")
//  @Test
//  public final void dot () {
//    Debug.DEBUG=false;
//    Debug.println();
//    Debug.println("dot");
//    dotTests(
//      generators(Shared.TEST_DIM),
//      makeAccumulators(accumulators()),
//      RBFAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
