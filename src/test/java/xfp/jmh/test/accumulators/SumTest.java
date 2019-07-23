package xfp.jmh.test.accumulators;

import static xfp.jmh.test.accumulators.Shared.accumulators;

import org.junit.jupiter.api.Test;

import xfp.jmh.accumulators.EFloatAccumulator;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test summation algorithms.
 * <p>
 * <pre>
 * mvn clean -Dtest=xfp/jmh/test/accumulators/SumTest test > SumTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-22
 */

public final class SumTest {

  @SuppressWarnings("static-method")
  @Test
  public final void infiniteSum () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println("infinite");
    Common.infinityTests(
      Common.makeAccumulators(accumulators())); }

  @SuppressWarnings("static-method")
  @Test
  public final void overflowSum () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println("overflow");
    Common.overflowTests(
      Common.makeAccumulators(accumulators())); }

  @SuppressWarnings("static-method")
  @Test
  public final void zeroSum () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println("zeroSum");
    Common.zeroSumTests(
      Common.zeroSumGenerators(Shared.TEST_DIM),
      Common.makeAccumulators(accumulators())); }

  @SuppressWarnings("static-method")
  @Test
  public final void sum () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println("sum");
    Common.sumTests(
      Common.generators(Shared.TEST_DIM),
      Common.makeAccumulators(accumulators()),
      EFloatAccumulator.make()); }

  @SuppressWarnings("static-method")
  @Test
  public final void l2 () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println("l2");
    Common.l2Tests(
      Common.generators(Shared.TEST_DIM),
      Common.makeAccumulators(accumulators()),
      EFloatAccumulator.make()); }

  @SuppressWarnings("static-method")
  @Test
  public final void dot () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println("dot");
    Common.dotTests(
      Common.generators(Shared.TEST_DIM),
      Common.makeAccumulators(accumulators()),
      EFloatAccumulator.make()); }

  // TODO: pick expected behavior for non-finite input
  //  @SuppressWarnings("static-method")
  //  @Test
  //  public final void nanSum () {
  //    //Debug.DEBUG=false;
  //    //Debug.println();
  //    //Debug.println("infinite");
  //    Common.nonFiniteTests(
  //      Common.makeAccumulators(accumulators())); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
