package xfp.jmh.test.accumulators;

import java.util.List;

/** Shared data/code for accumulator tests.
 * Not instantiable. Class slots/methods only.
 *
 * @author mcdonald dot john dot alan at gmail dot com
 * @version 2019-07-22
 */
public final class Shared {

  public static final int TEST_DIM = 256; //(1 * 64 * 1024) - 1;
  //1 << 14;

  public static final List<String> accumulators () {
    return
      List.of(
        "xfp.jmh.accumulators.BigFloat0Accumulator"
        //,
        //"xfp.jmh.accumulators.IFastAccumulator",
        //"xfp.jmh.accumulators.ZhuHayesGCAccumulator",
        //"xfp.jmh.accumulators.ZhuHayesGCBranch",
        //"xfp.jmh.accumulators.ZhuHayesBranch"
        // ,
        // "xfp.java.accumulators.RationalFloatAccumulator"
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
        // "xfp.jmh.accumulators.KahanFmaAccumulator",
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
