package xfp.jmh.test.accumulators;

import java.util.List;

/** Shared data/code for accumulator tests.
 * Not instantiable. Class slots/methods only.
 * 
 * @author mcdonald dot john dot alan at gmail dot com
 * @version 2019-04-09
 */
public final class Shared {

  public static final int TEST_DIM = (1 * 1024 * 1024) - 1; 
  //1 << 14;

  public static final List<String> accumulators () {
    return
      List.of(
        "xfp.jmh.accumulators.ZhuHayesBranch"
        ,
        "xfp.jmh.accumulators.ZhuHayesNoBranch"
        ,
        "xfp.java.accumulators.DoubleAccumulator"
        ,
        "xfp.jmh.accumulators.KahanAccumulator"
        // ,
        // "xfp.java.accumulators.RBFAccumulator"
        // ,
        // // Same as non-strict, just slower
        // "xfp.jmh.accumulators.StrictDoubleAccumulator",
        // "xfp.jmh.accumulators.StrictDoubleFmaAccumulator",
        // // overflow unless values more limited
        // "xfp.jmh.accumulators.FloatAccumulator",
        // "xfp.jmh.accumulators.FloatFmaAccumulator",
        // // Too slow to keep testing
        // "xfp.jmh.accumulators.BigDecimalAccumulator",
        // "xfp.jmh.accumulators.BigFractionAccumulator",
        // "xfp.java.accumulators.DoubleFmaAccumulator",
        // "xfp.jmh.accumulators.EFloatAccumulator",
        // "xfp.jmh.accumulators.ERationalAccumulator",
        // "xfp.jmh.accumulators.KahanFmaAccumulator",
        // "xfp.jmh.accumulators.MutableRationalAccumulator",
        // "xfp.java.accumulators.RationalAccumulator"
        // //,
        // // Broken in many ways.
        // // Doesn't overflow to infinity, or accumulate extreme
        // // values correctly.
        // // Slow as well.
        // //"xfp.jmh.accumulators.RatioAccumulator"
        ); }

  //--------------------------------------------------------------
  // disable constructor
  //--------------------------------------------------------------

  private Shared () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
