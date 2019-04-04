package xfp.jmh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.rng.UniformRandomProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

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
 * java -ea -jar target\benchmarks.jar "Dot|L2|Sum" -rf csv  -rrf output\Sums.csv
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-03
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
@Threads(value=4)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class Base {

  //--------------------------------------------------------------
  private static final UniformRandomProvider URP = 
    PRNG.well44497b("seeds/Well44497b-2019-01-05.txt");

  public static final void save (final double x, 
                                 final List data) {
    data.add(Double.valueOf(x)); }
  
  private static final DateTimeFormatter DTF = 
    DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
  
  private static final String now () {
    return LocalDateTime.now().format(DTF); }
  //--------------------------------------------------------------

  @Param({
    "65536",
//    "262144",
//    "1048576",
//    "4194304",
//    "16777216",
  })
  int dim;
  Generator g;
  double[] x0;
  double[] x1;
  
  Accumulator exact;
  List<Double> truth;

  @Param({
    //"xfp.java.accumulators.BigDecimalAccumulator",
    //"xfp.jmh.accumulators.BigFractionAccumulator",
    "xfp.java.accumulators.DoubleAccumulator",
    //"xfp.java.accumulators.DoubleFmaAccumulator",
//    "xfp.jmh.accumulators.KahanAccumulator",
    "xfp.jmh.accumulators.KahanFmaAccumulator",
    //"xfp.jmh.accumulators.EFloatAccumulator",
    //"xfp.jmh.accumulators.ERationalAccumulator",
    //"xfp.java.accumulators.FloatAccumulator",
    //"xfp.java.accumulators.FloatFmaAccumulator",
    //"xfp.jmh.accumulators.RatioAccumulator",
    //"xfp.java.accumulators.MutableRationalAccumulator",
    //"xfp.java.accumulators.RationalAccumulator",
//    "xfp.java.accumulators.RBFAccumulator",
  })
  String className;
  Accumulator a;
  List<Double> est;

  //--------------------------------------------------------------

  public abstract double compute (final Accumulator ac,
                                  final double[] z0,
                                  final double[] z1);

  //--------------------------------------------------------------

  @Setup(Level.Trial)  
  public final void trialSetup () {
    final int emax = Common.deMax(dim)/2;
    final double dmax = (1<<emax);
    g = 
      Doubles.shuffledGenerator(
        Doubles.zeroSumGenerator(
          Doubles.exponentialGenerator(dim,URP,0.0,dmax)),
        URP);
    truth = new ArrayList();
    est = new ArrayList();
    exact = RBFAccumulator.make();
    assert exact.isExact();
    a = Common.makeAccumulator(className); }  

  @Setup(Level.Invocation)  
  public final void invocationSetup () {
    x0 = (double[]) g.next();
    x1 = (double[]) g.next();
    save(compute(exact,x0,x1),truth); }  

  @TearDown(Level.Trial)  
  public final void teardownTrial () {
    //System.out.println("teardownTrial");
    final int n = truth.size();
    assert n == est.size(); 
    final File parent = 
      new File("output/" + Classes.className(this));
    parent.mkdirs();
    final File f = new File(parent,
      Classes.className(a) 
      + "-" + dim
      + "-" + now() + ".txt");
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(f);
      pw.println("dim truth est");
      for (int i=0;i<n;i++) {
        pw.println(
          dim + " " + truth.get(i) + " " + est.get(i)); } }
    catch (final FileNotFoundException e) {
      throw new RuntimeException(e); } 
    finally { if (null != pw) { pw.close(); } } }

  @Benchmark
  public final double bench () { 
    final double pred = compute(a,x0,x1);
    save(pred,est);
    return pred; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
