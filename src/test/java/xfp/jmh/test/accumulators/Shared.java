package xfp.jmh.test.accumulators;

import java.util.List;

/** Shared data/code for accumulator tests.
 * Not instantiable. Class slots/methods only.
 * 
 * @author mcdonald dot john dot alan at gmail dot com
 * @version 2019-04-08
 */
public final class Shared {

  public static final int TEST_DIM = 32 * 1024 * 1024; //1 << 14;
  
  public static final List<String> accumulators () {
    return
      List.of(
        //"xfp.jmh.accumulators.ZhuHayesBranch",
        "xfp.jmh.accumulators.ZhuHayesNoBranch"
        //,
//        "xfp.jmh.accumulators.BigDecimalAccumulator",
        // Too slow to keep testing
        //"xfp.jmh.accumulators.BigFractionAccumulator",
//        "xfp.java.accumulators.DoubleAccumulator",
//        "xfp.java.accumulators.DoubleFmaAccumulator",
        // Same as non-strict, just slower
        //"xfp.jmh.accumulators.StrictDoubleAccumulator",
        //"xfp.jmh.accumulators.StrictDoubleFmaAccumulator",
//        "xfp.jmh.accumulators.EFloatAccumulator",
        // Too slow to keep testing
        //"xfp.jmh.accumulators.ERationalAccumulator",
//        "xfp.jmh.accumulators.FloatAccumulator",
//        "xfp.jmh.accumulators.FloatFmaAccumulator",
//        "xfp.jmh.accumulators.KahanAccumulator",
//        "xfp.jmh.accumulators.KahanFmaAccumulator",
        // Too slow to keep testing
        //"xfp.jmh.accumulators.MutableRationalAccumulator",
//        "xfp.jmh.accumulators.RatioAccumulator",
        // Too slow to keep testing
        //"xfp.java.accumulators.RationalAccumulator",
//        "xfp.java.accumulators.RBFAccumulator"
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
