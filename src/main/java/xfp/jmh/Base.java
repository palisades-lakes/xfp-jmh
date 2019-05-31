package xfp.jmh;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import xfp.java.accumulators.BigFloatAccumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;

/** Benchmark <code>double[]</code> sums.
 * 
 * <pre>
 * java -cp target\benchmarks.jar xfp.jmh.Base
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-05-28
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
//@Threads(value=4)
//@BenchmarkMode(Mode.All)
///@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class Base {

  //--------------------------------------------------------------

  public static final void save (final double x, 
                                 final List data) {
    data.add(Double.valueOf(x)); }

  private static final DateTimeFormatter DTF = 
    DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

  public static final String now () {
    return LocalDateTime.now().format(DTF); }

  //--------------------------------------------------------------

  @Param({
    //"33554433",
    //"8388609",
    //"2097153",
    "524289",
    //"131071",
  })
  int dim;

  double[] x0;
  double[] x1;

  Accumulator exact;
  List<Double> truth = new ArrayList<Double>();

  @Param({
    //"xfp.java.accumulators.DistilledAccumulator",
    "xfp.java.accumulators.RationalAccumulator",
    "xfp.java.accumulators.RationalFloatAccumulator",
    "xfp.java.accumulators.BigFloatAccumulator",
    //"xfp.java.accumulators.KahanAccumulator",
    //"xfp.java.accumulators.DoubleAccumulator",
    //"xfp.java.accumulators.RationalFloat0Accumulator",
    //"xfp.java.accumulators.RationalFloatBIAccumulator",
    //"xfp.java.accumulators.BigFloatAccumulator6",
    //"xfp.jmh.accumulators.BigFloatAccumulator5",
    //"xfp.jmh.accumulators.BigFloatAccumulator4",
    //"xfp.jmh.accumulators.BigFloatAccumulator3",
    //"xfp.jmh.accumulators.BigFloatAccumulator2",
    //"xfp.jmh.accumulators.BigFloatAccumulator1",
    //"xfp.jmh.accumulators.BigFloatAccumulator0",
    //"xfp.java.accumulators.ZhuHayesAccumulator",
    //"xfp.java.accumulators.ZhuHayesAccumulator0",
    //"xfp.jmh.accumulators.ZhuHayesGCAccumulator",
    //"xfp.jmh.accumulators.ZhuHayesGCBranch",
    //"xfp.jmh.accumulators.ZhuHayesBranch",
    //"xfp.jmh.accumulators.BigDecimalAccumulator",
    //"xfp.jmh.accumulators.BigFractionAccumulator",
    //"xfp.jmh.accumulators.DoubleFmaAccumulator",
    //"xfp.jmh.accumulators.KahanFmaAccumulator",
    //"xfp.jmh.accumulators.EFloatAccumulator",
    //"xfp.jmh.accumulators.ERationalAccumulator",
    //"xfp.jmh.accumulators.FloatAccumulator",
    //"xfp.jmh.accumulators.FloatFmaAccumulator",
    //"xfp.jmh.accumulators.RatioAccumulator",
    //"xfp.jmh.accumulators.MutableRationalAccumulator",
  })
  String accumulator;
  Accumulator acc;
  List<Double> est = new ArrayList<Double>();

  //--------------------------------------------------------------

  private static final String SEED0 = 
    "seeds/Well44497b-2019-01-05.txt";

  private static final String SEED1 = 
    "seeds/Well44497b-2019-01-07.txt";

  public static final Map<String,IntFunction<Generator>> 
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

  public static final Generator makeGenerator (final String name,
                                               final int dim) {
    return factories.get(name).apply(dim); }

  //@Param({"finite","uniform","exponential",})
  @Param({"gaussian",})
  //@Param({"uniform",})
  String generator;
  Generator gen;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public abstract double operation (final Accumulator ac,
                                    final double[] z0,
                                    final double[] z1);

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(accumulator,dim)</code> pair.
   */
  @Setup(Level.Trial)  
  public final void trialSetup () {
    gen = makeGenerator(generator,dim);
    truth.clear();
    est.clear();
    exact = BigFloatAccumulator.make();
    assert exact.isExact();
    acc = Common.makeAccumulator(accumulator); }  

  @Setup(Level.Invocation)  
  public final void invocationSetup () {
    x0 = (double[]) gen.next();
    x1 = (double[]) gen.next();
    save(operation(exact,x0,x1),truth); }  

  // not needed while testing exact methods
  //  @TearDown(Level.Trial)  
  //  public final void teardownTrial () {
  //    //System.out.println("teardownTrial");
  //    final int n = truth.size();
  //    assert n == est.size(); 
  //    final String aname = Classes.className(acc);
  //    final String bname = 
  //      Classes.className(this).replace("_jmhType","");
  //    final File parent = new File("output/" + bname);
  //    parent.mkdirs();
  //    final File f = new File(parent,
  //      aname + "-" + generator + "-" + dim + "-" + now() + ".csv");
  //    PrintWriter pw = null;
  //    try {
  //      pw = new PrintWriter(f);
  //      pw.println("generator,benchmark,accumulator,dim,truth,est");
  //      for (int i=0;i<n;i++) {
  //        pw.println(
  //          generator + "," + bname + "," + aname + "," + dim + "," 
  //            + truth.get(i) + "," + est.get(i)); } }
  //    catch (final FileNotFoundException e) {
  //      throw new RuntimeException(e); } 
  //    finally { if (null != pw) { pw.close(); } } }

  @Benchmark
  public final double bench () { 
    final double pred = operation(acc,x0,x1);
    save(pred,est);
    return pred; }

  //--------------------------------------------------------------
  /** <pre>
   * java -cp target\benchmarks.jar xfp.jmh.Base
   * </pre> 
   */

  public static void main (final String[] args) 
    throws RunnerException {
    final File parent = new File("output");
    parent.mkdirs();
    final File csv = 
      new File(parent,"Sums" + "-" + now() + ".csv");
    final Options opt = 
      new OptionsBuilder()
      //.mode(Mode.All)
      .mode(Mode.AverageTime)
      .timeUnit(TimeUnit.MILLISECONDS)
      //.include("Dot|L2|Sum")
      .include("Sum")
      //.resultFormat(ResultFormatType.JSON)
      //.result("output/Sums" + "-" + now() + ".json")
      .resultFormat(ResultFormatType.CSV)
      .result(csv.getPath())
      .threads(1)
      .shouldFailOnError(true)
      .shouldDoGC(true)
      .jvmArgs(
        "-Xmx4g","-Xms4g","-Xmn2g",
        "-XX:+UseFMA",
        "-Xbatch","-server")
      .build();
    new Runner(opt).run(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
