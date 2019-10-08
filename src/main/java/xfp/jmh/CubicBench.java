package xfp.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import xfp.java.accumulators.Accumulator;
import xfp.java.accumulators.EFloatAccumulator;
import xfp.java.polynomial.EFloatCubic;
import xfp.java.polynomial.Polynomial;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.test.Common;

/** Benchmark axpy implementations.
 *
 *<pre>
 * j xfp.jmh.CubicBench
 * </pre>
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-07
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class CubicBench {

  //--------------------------------------------------------------

  //@Param({"exponential",})
  //@Param({"finite",})
  //@Param({"gaussian",})
  //@Param({"laplace",})
  //@Param({"uniform",})
  @Param({"exponential","finite","gaussian","laplace","uniform",})
  String generator;
  Generator gen;

  Polynomial e;
  // exact value(s)
  double[] truth;

  @Param({
    "xfp.java.polynomial.RationalFloatCubic",
    "xfp.java.polynomial.BigFloatCubic",
    //"xfp.jmh.polynomial.BigFloat0Cubic",
    //"xfp.java.polynomial.DoubleCubic",
    //"xfp.java.polynomial.EFloatCubic",
  })
  String cName;
  Polynomial c;

  //--------------------------------------------------------------

  @Param({
    //"33554433",
    //"8388609",
    //"4194303",
    //"2097153",
    //"1048575",
    //"524289",
    "131071",
  })
  int dim;

  double a0;
  double a1;
  double a2;
  double a3;
  double[] x;

  // estimated value(s)
  double[] p;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public static final double[] operation (final Polynomial imp,
                                          final double[] x) {
    return imp.doubleValue(x); }

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(cName,dim)</code> pair.
   */
  @Setup(Level.Trial)
  public final void trialSetup () {
    gen = Generators.make(generator,dim); }

  @Setup(Level.Invocation)
  public final void invocationSetup () {
    a0 = gen.nextDouble();
    a1 = gen.nextDouble();
    a2 = gen.nextDouble();
    a3 = gen.nextDouble();
    x = (double[]) gen.next();
    e = EFloatCubic.make(a0,a1,a2,a3);
    assert e.isExact();
    truth = operation(e,x);
    c = Common.makeCubic(cName,a0,a1,a2,a3); }

  private final Accumulator acc = EFloatAccumulator.make();

  @TearDown(Level.Invocation)
  public final void invocationTeardown () {
    assert 
    0.0 == acc.clear().addL1Distance(truth,p).doubleValue(); }

  @Benchmark
  public final double[] bench () {
    p = operation(c,x);
    return p; }

  //--------------------------------------------------------------

  public static final void main (final String[] args)  {
    Defaults.run("Cub"
      + "icBench"); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
