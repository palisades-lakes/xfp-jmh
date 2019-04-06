package xfp.jmh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import org.apache.commons.rng.UniformRandomProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import xfp.java.Classes;
import xfp.java.accumulators.Accumulator;
import xfp.java.accumulators.RBFAccumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;

/** Benchmark <code>double[]</code> sums.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar "Dot|L2|Sum" -rf csv  -rff output\Sums.csv
 * java -cp target\benchmarks.jar xfp.jmh.Base
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-06
 */

@SuppressWarnings("unchecked")
@State(Scope.Benchmark)
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
  // TODO: gather class slots into a 'state' object
  
  private static final Accumulator exact = RBFAccumulator.make();

  public static final List<String> accumulators = 
    List.of( 
      "xfp.jmh.accumulators.ZhuHayesOnlineExactBranch",
      "xfp.jmh.accumulators.ZhuHayesOnlineExactNoBranch",
      //"xfp.java.accumulators.BigDecimalAccumulator",
      //"xfp.jmh.accumulators.BigFractionAccumulator",
      "xfp.java.accumulators.DoubleAccumulator",
      //"xfp.java.accumulators.DoubleFmaAccumulator",
      "xfp.jmh.accumulators.KahanAccumulator"
      //"xfp.jmh.accumulators.KahanFmaAccumulator",
      //"xfp.jmh.accumulators.EFloatAccumulator",
      //"xfp.jmh.accumulators.ERationalAccumulator",
      //"xfp.java.accumulators.FloatAccumulator",
      //"xfp.java.accumulators.FloatFmaAccumulator",
      //"xfp.jmh.accumulators.RatioAccumulator",
      //"xfp.java.accumulators.MutableRationalAccumulator",
      //"xfp.java.accumulators.RationalAccumulator",
      //"xfp.java.accumulators.RBFAccumulator",
      );

  public static Accumulator accumulator;

  public static final int[] dims = 
  { 
//    65536,
//    262144,
    1048576,
//    4194304,
//    16777216,
  };
  public static int dim;

  private static final String SEED = 
    "seeds/Well44497b-2019-01-05.txt";

  public static final 
  List<IntFunction<Generator>> 
  generatorFactories = 
  List.of(
//    new IntFunction<Generator>() {
//      @Override
//      public final Generator apply (final int d) {
//        final UniformRandomProvider urp = PRNG.well44497b(SEED);
//        final int emax = Common.deMax(d)/2;
//        return 
//          Doubles.shuffledGenerator(
//            Doubles.zeroSumGenerator(
//              Doubles.finiteGenerator(d,urp,emax)),
//            urp); } 
//    },
    // new IntFunction<Generator>() {
    //   @Override
    //   public final Generator apply (final int d) {
    //     final UniformRandomProvider urp = PRNG.well44497b(SEED);
    //     final int emax = Common.deMax(d)/2;
    //     final double dmax = (1<<emax);
    //     return 
    //       Doubles.shuffledGenerator(
    //         Doubles.zeroSumGenerator(
    //           Doubles.uniformGenerator(d,urp,-dmax,dmax)),
    //         urp); } 
    // },
    // new IntFunction<Generator>() {
    //   @Override
    //   public final Generator apply (final int dim) {
    //     final UniformRandomProvider urp = PRNG.well44497b(SEED);
    //     final int emax = Common.deMax(dim)/2;
    //     final double dmax = (1<<emax);
    //     return 
    //       Doubles.shuffledGenerator(
    //         Doubles.zeroSumGenerator(
    //           Doubles.gaussianGenerator(dim,urp,0.0,dmax)),
    //         urp); } 
    // },
    new IntFunction<Generator>() {
      @Override
      public final Generator apply (final int d) {
        final UniformRandomProvider urp = PRNG.well44497b(SEED);
        final int emax = Common.deMax(d)/2;
        final double dmax = (1<<emax);
        return 
          Doubles.shuffledGenerator(
            Doubles.zeroSumGenerator(
              Doubles.exponentialGenerator(d,urp,0.0,dmax)),
            urp); } 
      // },
      //   new IntFunction<Generator>() {
      //     @Override
      //     public final Generator apply (final int dim) {
      //       final UniformRandomProvider urp = PRNG.well44497b(SEED);
      //     final int emax = Common.deMax(dim)/2;
      //     final double dmax = (1<<emax);
      //     return 
      //       Doubles.shuffledGenerator(
      //         Doubles.zeroSumGenerator(
      //           Doubles.laplaceGenerator(dim,urp,0.0,dmax)),
      //         urp); } 
    });

  public static Generator generator;

  public static String folder; // for output

  //--------------------------------------------------------------
  // trial/invocation state

  double[] x0;
  double[] x1;
  List<Double> truth;
  List<Double> est;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public abstract double operation (final Accumulator ac,
                                    final double[] z0,
                                    final double[] z1);

  //--------------------------------------------------------------

  @Setup(Level.Trial)  
  public final void trialSetup () {
    truth = new ArrayList<Double>();
    est = new ArrayList<Double>();
    exact.clear();
    accumulator.clear(); }  

  @Setup(Level.Invocation)  
  public final void invocationSetup () {
    // fresh data for each call
    x0 = (double[]) generator.next();
    x1 = (double[]) generator.next();
    save(operation(exact,x0,x1),truth); }  

  @TearDown(Level.Trial)  
  public final void teardownTrial () {
    //System.out.println("teardownTrial");
    final int n = truth.size();
    assert n == est.size(); 
    final String aname = Classes.className(accumulator);
    final String bname = 
      Classes.className(this).replace("_jmhType","");
    final String gname = generator.name();
    final File parent = new File(folder + "/" + bname);
    parent.mkdirs();
    final File f = new File(parent,
      aname + "-" + gname + "-" + now() + ".csv");
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(f);
      pw.println("generator,benchmark,algorithm,dim,truth,est");
      for (int i=0;i<n;i++) {
        pw.println(
          gname + "," + bname + "," + aname + "," + dim + "," 
            + truth.get(i) + "," + est.get(i)); } }
    catch (final FileNotFoundException e) {
      throw new RuntimeException(e); } 
    finally { if (null != pw) { pw.close(); } } }

  @Benchmark
  public final double bench () { 
    final double pred = operation(accumulator,x0,x1);
    save(pred,est);
    return pred; }

  //--------------------------------------------------------------

  public static void main (final String[] args) 
    throws RunnerException {
    final ChainedOptionsBuilder b = 
      new OptionsBuilder()
      .mode(Mode.AverageTime)
      .timeUnit(TimeUnit.MILLISECONDS)
      //.include("Dot|L2|Sum")
      .include("Sum")
      .resultFormat(ResultFormatType.CSV)
      .threads(1)
      .shouldFailOnError(true);
    for (final String as : accumulators) {
      accumulator = Common.makeAccumulator(as);
      System.out.println(accumulator);
      for (final int d : dims) {
        dim = d;
        System.out.println(dim);
        for (final IntFunction<Generator> gf : generatorFactories) {
          generator = gf.apply(dim);
          System.out.println(generator.name());
          folder = "output" 
            + "/" + Classes.className(accumulator) 
            + "/" + dim 
            + "/" + generator.name();
          System.out.println(folder);
          new File(folder).mkdirs();
          new Runner(
            b
            .result(folder + "/Sums" + "-" + now() + ".csv")
            .build())
          .run(); } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
