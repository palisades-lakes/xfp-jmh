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
import xfp.java.polynomial.Axpy;
import xfp.java.polynomial.EFloatAxpy;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.test.Common;

/** Benchmark axpy implementations.
 *
 *<pre>
 * j xfp.jmh.AxpyBench
 * </pre>
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-03
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class AxpyBench {

  //--------------------------------------------------------------

  //@Param({"exponential",})
  @Param({"finite",})
  //@Param({"gaussian",})
  //@Param({"laplace",})
  //@Param({"uniform",})
  //@Param({"exponential","finite","gaussian","laplace","uniform",})
  String generator;
  Generator gen;

  Axpy exact;
  // exact value(s)
  double[] truth;

  @Param({
    "xfp.java.polynomial.BigFloatAxpy",
    "xfp.jmh.polynomial.BigFloat0Axpy",
    "xfp.java.polynomial.DoubleAxpy",
    //"xfp.java.polynomial.EFloatAxpy",
  })
  String axpyName;
  Axpy axpy;

  //--------------------------------------------------------------

  @Param({
    //"33554433",
    //"8388609",
    //"4194303",
    //"2097153",
    "1048575",
    //"524289",
    //"131071",
  })
  int dim;

  double[] x0;
  double[] x1;
  double[] x2;

  // estimated value(s)
  double[] p;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public static final double[] operation (final Axpy imp,
                                          final double[] z0,
                                          final double[] z1,
                                          final double[] z2) {
    return imp.daxpy(z0,z1,z2); }

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(cName,dim)</code> pair.
   */
  @Setup(Level.Trial)
  public final void trialSetup () {
    gen = Generators.make(generator,dim);
    exact = new EFloatAxpy();
    assert exact.isExact();
    axpy = Common.makeAxpy(axpyName); }

  @Setup(Level.Invocation)
  public final void invocationSetup () {
    x0 = (double[]) gen.next();
    x1 = (double[]) gen.next();
    x2 = (double[]) gen.next();
    truth = operation(exact,x0,x1,x2); }

  private final Accumulator acc = EFloatAccumulator.make();

  @TearDown(Level.Invocation)
  public final void invocationTeardown () {
    assert 
    0.0 == acc.clear().addL1Distance(truth,p).doubleValue(); }

  @Benchmark
  public final double[] bench () {
    p = operation(axpy,x0,x1,x2);
    return p; }

  //--------------------------------------------------------------

  public static final void main (final String[] args)  {
    Defaults.run("AxpyBench"); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
