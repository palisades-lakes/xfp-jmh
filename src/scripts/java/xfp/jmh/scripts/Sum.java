package xfp.jmh.scripts;

import java.util.Map;
import java.util.function.IntFunction;

import org.apache.commons.rng.UniformRandomProvider;

import xfp.java.accumulators.Accumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;
import xfp.jmh.accumulators.ZhuHayesBranch;

/** Benchmark accumulators tests.
 *
 * <pre>
 * jy --source 11 -ea src/scripts/java/xfp/jmh/scripts/TotalSum.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-08
 */
@SuppressWarnings("unchecked")
public final class Sum {

  private static final String SEED0 =
    "seeds/Well44497b-2019-01-05.txt";

  private static final String SEED1 =
    "seeds/Well44497b-2019-01-07.txt";

  private static final
  Map<String,IntFunction<Generator>>
  factories =
  Map.of(
    "uniform",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final UniformRandomProvider urp1 = PRNG.well44497b(SEED1);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return
          Doubles.shuffledGenerator(
            Doubles.zeroSumGenerator(
              Doubles.uniformGenerator(dim,urp0,-dmax,dmax)),
            urp1); }
    },
    "finite",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final UniformRandomProvider urp1 = PRNG.well44497b(SEED1);
        final int emax = Common.deMax(dim)/2;
        System.out.println("emax=" + emax);
        return
          Doubles.shuffledGenerator(
            Doubles.zeroSumGenerator(
              Doubles.finiteGenerator(dim,urp0,emax)),
            urp1); }
    },
    "exponential",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final UniformRandomProvider urp1 = PRNG.well44497b(SEED1);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return
          Doubles.shuffledGenerator(
            Doubles.zeroSumGenerator(
              Doubles.exponentialGenerator(dim,urp0,0.0,dmax)),
            urp1); }
    },
    "gaussian",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final UniformRandomProvider urp1 = PRNG.well44497b(SEED1);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return
          Doubles.shuffledGenerator(
            Doubles.zeroSumGenerator(
              Doubles.gaussianGenerator(dim,urp0,0.0,dmax)),
            urp1); }
    },
    "laplace",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final UniformRandomProvider urp1 = PRNG.well44497b(SEED1);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return
          Doubles.shuffledGenerator(
            Doubles.zeroSumGenerator(
              Doubles.laplaceGenerator(dim,urp0,0.0,dmax)),
            urp1); }
    });

  //--------------------------------------------------------------

  public static final void main (final String[] args)
  //    throws InterruptedException
  {

    final int dim = 64*1024*1024;
    final int trys = 128;
    final Generator g = factories.get("finite").apply(dim);
    final double[] x = (double[]) g.next();
    //    final Accumulator exact = RationalFloatAccumulator.make();
    //    final double truth = exact.addAll(x).doubleValue();
    final Accumulator a = ZhuHayesBranch.make();
    assert a.isExact();
    //Thread.sleep(16*1024);
    final long t = System.nanoTime();
    for (int i=0;i<trys;i++) {
      a.clear();
      a.addAll(x);
      final double z = a.doubleValue();
      if (0.0 != z) {
        System.out.println(Double.toHexString(0.0)
          + " != " + Double.toHexString(z)); } }
    System.out.printf("total secs: %8.2f\n",
      Double.valueOf((System.nanoTime()-t)*1.0e-9));
    //Thread.sleep(16*1024);
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
