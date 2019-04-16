package xfp.jmh;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
import xfp.java.prng.Generator;
import xfp.java.test.Common;

/** Benchmark <code>double[]</code> sums.
 * 
 * <pre>
 * java -cp target\benchmarks.jar xfp.jmh.IFast
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-16
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class IFast {

  //--------------------------------------------------------------

  @Param({
    //"8193",
    "4097",
    "2049",
    "1537",
    "1025",
    "513",
  })
  int dim;
  double[] x0;
  double[] x1;

  //--------------------------------------------------------------

  @Param({
    "xfp.jmh.accumulators.IFastAccumulator",
    "xfp.java.accumulators.ZhuHayesGCAccumulator",
    "xfp.java.accumulators.ZhuHayesAccumulator",
    "xfp.jmh.accumulators.ZhuHayesGCBranch",
    "xfp.jmh.accumulators.ZhuHayesBranch",
    "xfp.java.accumulators.DoubleAccumulator",
    "xfp.jmh.accumulators.KahanAccumulator"
  })
  String accumulator;
  Accumulator acc;
  
  //--------------------------------------------------------------

  //@Param({"finite","uniform","exponential","gaussian"})
  @Param({"uniform",})
  String generator;
  Generator gen;

  //--------------------------------------------------------------

  @Setup(Level.Trial)  
  public final void trialSetup () {
    gen = Base.makeGenerator(generator,dim);
    acc = Common.makeAccumulator(accumulator); }  

  @Setup(Level.Invocation)  
  public final void invocationSetup () {
    x0 = (double[]) gen.next(); 
    x1 = (double[]) gen.next(); }  

  @Benchmark
  public final double sum () { 
    return acc.clear().addAll(x0).addAll(x1).doubleValue(); }

  @Benchmark
  public final double dot () { 
    return acc.clear().addProducts(x0,x1).doubleValue(); }

  @Benchmark
  public final double l2 () { 
    return acc.clear().add2All(x0).add2All(x1).doubleValue(); }

  //--------------------------------------------------------------
  // java -cp target\benchmarks.jar xfp.jmh.IFast

  public static void main (final String[] args) throws RunnerException {
    final File parent = new File("output");
    parent.mkdirs();
    final File csv = 
      new File(parent,"IFast" + "-" + Base.now() + ".csv");
    final Options opt = 
      new OptionsBuilder()
      .mode(Mode.AverageTime)
      .timeUnit(TimeUnit.MICROSECONDS)
      .include("dot|l2|sum")
      .resultFormat(ResultFormatType.CSV)
      .result(csv.getPath())
      .threads(1)
      .shouldFailOnError(true)
//      .shouldDoGC(true)
      .build();
    new Runner(opt).run(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
