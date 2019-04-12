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
 * @version 2019-04-12
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class IFast {

  //--------------------------------------------------------------

  @Param({
    //"8193",
    "4097",
    "2049",
    //"1537",
    "1025",
    "513",
  })
  int dim;
  double[] x;

  //--------------------------------------------------------------

  @Param({
    "xfp.jmh.accumulators.IFastAccumulator",
    "xfp.java.accumulators.ZhuHayesNoGCAccumulator",
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
    x = (double[]) gen.next();  }  

  @Benchmark
  public final double bench () { 
    return acc.clear().addAll(x).doubleValue(); }

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
      .include("IFast")
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
