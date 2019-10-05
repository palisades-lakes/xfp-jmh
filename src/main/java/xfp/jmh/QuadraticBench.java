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
import xfp.java.polynomial.Quadratic;
import xfp.java.polynomial.EFloatQuadratic;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.test.Common;

/** Benchmark axpy implementations.
 *
 *<pre>
 * j xfp.jmh.QuadraticBench
 * </pre>
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-04
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class QuadraticBench {

  //--------------------------------------------------------------

  //@Param({"exponential",})
  //@Param({"finite",})
  //@Param({"gaussian",})
  //@Param({"laplace",})
  @Param({"uniform",})
  //@Param({"exponential","finite","gaussian","laplace","uniform",})
  String generator;
  Generator gen;

  Quadratic e;
  // exact value(s)
  double[] truth;

  @Param({
    "xfp.java.polynomial.BigFloatQuadratic",
    "xfp.jmh.polynomial.BigFloat0Quadratic",
    "xfp.java.polynomial.DoubleQuadratic",
    "xfp.java.polynomial.EFloatQuadratic",
  })
  String qName;
  Quadratic q;

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
  double[] x;

  // estimated value(s)
  double[] p;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public static final double[] operation (final Quadratic imp,
                                          final double[] x) {
    return imp.doubleValue(x); }

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(qName,dim)</code> pair.
   */
  @Setup(Level.Trial)
  public final void trialSetup () {
    gen = Generators.make(generator,dim); }

  @Setup(Level.Invocation)
  public final void invocationSetup () {
    a0 = gen.nextDouble();
    a1 = gen.nextDouble();
    a2 = gen.nextDouble();
    x = (double[]) gen.next();
    e = EFloatQuadratic.make(a0,a1,a2);
    assert e.isExact();
    truth = operation(e,x);
    q = Common.makeQuadratic(qName,a0,a1,a2); }

  private final Accumulator acc = EFloatAccumulator.make();

  @TearDown(Level.Invocation)
  public final void invocationTeardown () {
    assert 
    0.0 == acc.clear().addL1Distance(truth,p).doubleValue(); }

  @Benchmark
  public final double[] bench () {
    p = operation(q,x);
    return p; }

  //--------------------------------------------------------------

  public static final void main (final String[] args)  {
    Defaults.run("QuadraticBench"); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------