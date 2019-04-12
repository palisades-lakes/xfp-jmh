package xfp.jmh.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Test;

import xfp.java.Debug;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn clean -Dtest=xfp/jmh/test/accumulators/ZhuHayesTest test > ZhuHayesTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-10
 */

public final class ZhuHayesTest {

  private static final List<String> accumulators =
    List.of(
    "xfp.jmh.accumulators.ZhuHayesBranch",
    "xfp.jmh.accumulators.ZhuHayesNoBranch");
  
  private static final int TEST_DIM = (32 * 1024 * 1024) + 1; 

  @SuppressWarnings("static-method")
  @Test
  public final void zeroSum () {
    Debug.DEBUG=true;
    Debug.println();
    Debug.println("zeroSum");
    Common.zeroSumTests(
      Common.zeroSumGenerators(TEST_DIM),
      Common.makeAccumulators(accumulators)); }

//  @SuppressWarnings("static-method")
//  @Test
//  public final void sum () {
//    Debug.DEBUG=false;
//    Debug.println();
//    Debug.println("sum");
//    Common.sumTests(
//      Common.generators(TEST_DIM),
//      Common.makeAccumulators(accumulators),
//      RBFAccumulator.make()); }
//
//  @SuppressWarnings("static-method")
//  @Test
//  public final void l2 () {
//    Debug.DEBUG = false;
//    Debug.println();
//    Debug.println("l2");
//    Common.l2Tests(
//      Common.generators(TEST_DIM),
//      Common.makeAccumulators(accumulators),
//      RBFAccumulator.make()); }
//
//  @SuppressWarnings("static-method")
//  @Test
//  public final void dot () {
//    Debug.DEBUG=false;
//    Debug.println();
//    Debug.println("dot");
//    Common.dotTests(
//      Common.generators(TEST_DIM),
//      Common.makeAccumulators(accumulators),
//      RBFAccumulator.make()); }
//
//  @SuppressWarnings("static-method")
//  @Test
//  public final void nanSum () {
//    Debug.DEBUG=false;
//    Debug.println();
//    Debug.println("infinite");
//    Common.nonFiniteTests(
//      Common.makeAccumulators(accumulators)); }
//
//  @SuppressWarnings("static-method")
//  @Test
//  public final void infiniteSum () {
//    Debug.DEBUG=false;
//    Debug.println();
//    Debug.println("infinite");
//    Common.infinityTests(
//      Common.makeAccumulators(accumulators)); }
//
//  @SuppressWarnings("static-method")
//  @Test
//  public final void overflowSum () {
//    Debug.DEBUG=false;
//    Debug.println();
//    Debug.println("overflow");
//    Common.overflowTests(
//      Common.makeAccumulators(accumulators)); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------