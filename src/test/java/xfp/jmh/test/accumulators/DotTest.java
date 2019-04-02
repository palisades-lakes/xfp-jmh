package xfp.jmh.test.accumulators;

import java.util.List;

import org.apache.commons.rng.UniformRandomProvider;
import org.junit.jupiter.api.Test;

import xfp.java.Classes;
import xfp.java.accumulators.Accumulator;
import xfp.java.accumulators.RBFAccumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;

//----------------------------------------------------------------
/** Test summation algorithms. 
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/numbers/DotTest test > DotTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-29
 */

//no actual tests here (yet)

public final class DotTest {

  private static final int DIM = 16 * 1024;

  //--------------------------------------------------------------

  @Test
  public final void dotTests () {
    System.out.println();
    System.out.println(Classes.className(this));

    final UniformRandomProvider urp = 
      PRNG.well44497b("seeds/Well44497b-2019-01-05.txt");
    final double dMax = 1.0;//Math.sqrt(Common.dMax(DIM));
    final List<Generator> generators = 
    List.of(
      Doubles.finiteGenerator(DIM,urp,Common.deMax(DIM)/2),
      Doubles.gaussianGenerator(DIM,urp,0.0,dMax),
      Doubles.laplaceGenerator(DIM,urp,0.0,dMax),
      Doubles.uniformGenerator(DIM,urp,-dMax,dMax));

    final List<Accumulator> accumulators = 
      Common.makeAccumulators(new String[] 
        {
         "xfp.jmh.accumulators.BigDecimalAccumulator",
         "xfp.jmh.accumulators.BigFractionAccumulator",
         "xfp.java.accumulators.DoubleAccumulator",
         "xfp.java.accumulators.DoubleFmaAccumulator",
         "xfp.jmh.accumulators.KahanAccumulator",
         "xfp.jmh.accumulators.EFloatAccumulator",
         "xfp.java.accumulators.RationalAccumulator",
         "xfp.java.accumulators.FloatAccumulator",
         "xfp.java.accumulators.FloatFmaAccumulator",
         "xfp.jmh.accumulators.RatioAccumulator",
         "xfp.jmh.accumulators.MutableRationalAccumulator",
         "xfp.java.accumulators.RationalAccumulator",
         "xfp.java.accumulators.RBFAccumulator",
        });

    Common.dotTests(generators,accumulators,RBFAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
