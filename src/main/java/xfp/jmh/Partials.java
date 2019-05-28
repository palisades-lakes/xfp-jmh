package xfp.jmh;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import org.apache.commons.rng.UniformRandomProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import xfp.java.accumulators.Accumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;

/** Benchmark partial sums.
 * <pre>
 * java -cp target\benchmarks.jar xfp.jmh.Partials
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-05-28
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class Partials {

  //--------------------------------------------------------------

  @Param({
    //"33554433",
    //"8388609",
    //"2097153",
    "524289",
    //"131071",
      })
  int dim;

  double[] x;
  double[] s;

  //--------------------------------------------------------------

  @Param({
    //"xfp.jmh.accumulators.IFastAccumulator",
    "xfp.java.accumulators.DoubleAccumulator",
    "xfp.java.accumulators.KahanAccumulator",
    "xfp.java.accumulators.BigFloatAccumulator",
    "xfp.java.accumulators.RationalFloatAccumulator",
    "xfp.java.accumulators.RationalFloat0Accumulator",
    "xfp.java.accumulators.RationalFloatBIAccumulator",
    //"xfp.java.accumulators.DistilledAccumulator",
    //"xfp.java.accumulators.DistilledAccumulator3",
    //"xfp.java.accumulators.DistilledAccumulator32",
    //"xfp.java.accumulators.DistilledAccumulator64",
    //"xfp.java.accumulators.BigFloatAccumulator",
    //"xfp.jmh.accumulators.BigDecimalAccumulator",
    //"xfp.java.accumulators.RationalFloatAccumulator",
    //"xfp.java.accumulators.ZhuHayesAccumulator",
    //"xfp.jmh.accumulators.ZhuHayesGCAccumulator",
    //"xfp.jmh.accumulators.ZhuHayesGCBranch",
    //"xfp.jmh.accumulators.ZhuHayesBranch",
  })
  String accumulator;
  Accumulator acc;

   //--------------------------------------------------------------

  private static final String SEED0 = 
    "seeds/Well44497b-2019-01-09.txt";

  public static final Map<String,IntFunction<Generator>> 
  factories = 
  Map.of(
    "uniform",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return 
          Doubles.sortedGenerator(
            Doubles.zeroSumGenerator(
              Doubles.uniformGenerator(dim,urp0,-dmax,dmax))); } 
    },
    "finite",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final int emax = Common.deMax(dim)/2;
        return 
          Doubles.sortedGenerator(
            Doubles.zeroSumGenerator(
              Doubles.finiteGenerator(dim,urp0,emax))); } 
    },
    "exponential",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return 
          Doubles.sortedGenerator(
            Doubles.zeroSumGenerator(
              Doubles.exponentialGenerator(dim,urp0,0.0,dmax))); } 
    },
    "gaussian",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return 
          Doubles.sortedGenerator(
            Doubles.zeroSumGenerator(
              Doubles.gaussianGenerator(dim,urp0,0.0,dmax))); } 
    },
    "laplace",
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int dim) {
        final UniformRandomProvider urp0 = PRNG.well44497b(SEED0);
        final int emax = Common.deMax(dim)/2;
        final double dmax = (1<<emax);
        return 
          Doubles.sortedGenerator(
            Doubles.zeroSumGenerator(
              Doubles.laplaceGenerator(dim,urp0,0.0,dmax))); } 
    });

  public static final Generator makeGenerator (final String name,
                                               final int dim) {
    return factories.get(name).apply(dim); }

  //@Param({"finite","uniform","exponential","gaussian","laplace"})
  @Param({"finite",})
  String generator;
  Generator gen;

  //--------------------------------------------------------------

  @Setup(Level.Trial)  
  public final void trialSetup () {
    gen = makeGenerator(generator,dim);
    acc = Common.makeAccumulator(accumulator); }  

  @Setup(Level.Invocation)  
  public final void invocationSetup () {
    x = (double[]) gen.next(); 
    s = new double[x.length]; }  

  @Benchmark
  public final double[] partials () { 
    // must be that some split is better than no split?
    final int n = x.length;
    assert 1 < n;
    acc.clear(); 
    for (int i=0;i<n;i++) { s[i] = acc.add(x[i]).doubleValue(); }
    return s; }

  //--------------------------------------------------------------
  // java -cp target\benchmarks.jar xfp.jmh.Partials

  public static void main (final String[] args) 
    throws RunnerException {
    
    final File parent = new File("output");
    parent.mkdirs();
    final File csv = 
      new File(parent,"Partials" + "-" + Base.now() + ".csv");
    final Options opt = 
      new OptionsBuilder()
      .mode(Mode.AverageTime)
      .timeUnit(TimeUnit.MILLISECONDS)
      .include("partials")
      .resultFormat(ResultFormatType.CSV)
      .result(csv.getPath())
      .threads(1)
      .shouldFailOnError(true)
      .shouldDoGC(true)
      .build();
    new Runner(opt).run(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
