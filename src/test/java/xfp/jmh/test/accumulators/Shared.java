package xfp.jmh.test.accumulators;

import java.util.List;

/** Shared data/code for accumulator tests.
 * Not instantiable. Class slots/methods only.
 *
 * @author mcdonald dot john dot alan at gmail dot com
 * @version 2019-05-28
 */
public final class Shared {

  public static final int TEST_DIM = (1 * 64 * 1024) - 1;
  //1 << 14;

  public static final List<String> accumulators () {
    return
      List.of(
        //"xfp.jmh.accumulators.RationalFloat0Accumulator",
        //"xfp.jmh.accumulators.RationalFloatBIAccumulator",
        //"xfp.java.accumulators.DoubleAccumulator",
        //"xfp.java.accumulators.KahanAccumulator"
        //"xfp.java.accumulators.BigFloatAccumulator1",
        //"xfp.java.accumulators.ZhuHayesAccumulator",
        //"xfp.jmh.accumulators.BigFloatAccumulator0",
        //"xfp.jmh.accumulators.BigFloatAccumulator1",
        //"xfp.jmh.accumulators.BigFloatAccumulator2",
        //"xfp.jmh.accumulators.BigFloatAccumulator3",
        //"xfp.jmh.accumulators.BigFloatAccumulator4",
        //"xfp.jmh.accumulators.BigFloatAccumulator5",
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
        //"xfp.jmh.accumulators.EFloatAccumulator"
        //,
        // "xfp.jmh.accumulators.ERationalAccumulator",
        // "xfp.jmh.accumulators.KahanFmaAccumulator",
        // "xfp.jmh.accumulators.MutableRationalAccumulator",
        // "xfp.jmh.accumulators.RationalAccumulator"
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
