package xfp.jmh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import xfp.java.accumulators.Accumulator;
import xfp.java.accumulators.RationalAccumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;

/** Benchmark double dot products
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar RationalDot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-29
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RationalDot {

  //--------------------------------------------------------------
  /** See {@link Integer#numberOfLeadingZeros(int)}. */
  private static final int ceilLog2 (final int k) {
    return Integer.SIZE - Integer.numberOfLeadingZeros(k-1); }

  /** Maximum exponent for double generation such that a float sum 
   * of <code>dim</code> <code>double</code>s will be finite
   * (with high enough probability).
   */
  private static final int feMax (final int dim) { 
    final int d = Float.MAX_EXPONENT - ceilLog2(dim);
    //System.out.println("emax=" + d);
    return d; }

  private static final int DIM = 256 * 1024;
  private static final UniformRandomProvider URP = 
    PRNG.well44497b("seeds/Well44497b-2019-01-05.txt");
  private static final Generator G = 
    Doubles.finiteGenerator(DIM,URP,feMax(DIM)/2);

  //--------------------------------------------------------------

  double[] x0;
  double[] x1;
  double trueDot;

  @Param({
    //    "xfp.java.accumulators.BigDecimalAccumulator",
    //    "xfp.jmh.accumulators.BigFractionAccumulator",
    "xfp.java.accumulators.DoubleAccumulator",
    "xfp.java.accumulators.DoubleFmaAccumulator",
    "xfp.jmh.accumulators.KahanAccumulator",
    "xfp.jmh.accumulators.EFloatAccumulator",
    //    "xfp.jmh.accumulators.ERationalAccumulator",
    //    "xfp.java.accumulators.FloatAccumulator",
    //    "xfp.java.accumulators.FloatFmaAccumulator",
    //    "xfp.jmh.accumulators.RatioAccumulator",
    //    "xfp.java.accumulators.MutableRationalAccumulator",
    //    "xfp.java.accumulators.RationalAccumulator",
    "xfp.java.accumulators.RBFAccumulator",
  })
  String className;
  Accumulator a;

  @Setup(Level.Trial)  
  public final void setup () 
    throws ClassNotFoundException, 
    IllegalAccessException, 
    IllegalArgumentException, 
    InvocationTargetException, 
    NoSuchMethodException, 
    SecurityException {
    x0 = (double[]) G.next();
    x1 = (double[]) G.next();
    final Accumulator a0 = RationalAccumulator.make();
    a0.addProducts(x0,x1);
    trueDot = a0.doubleValue(); 
    final Class c = Class.forName(className);
    //System.out.println(c); 
    final Method m = c.getMethod("make");
    a = (Accumulator) m.invoke(null); }  

  //--------------------------------------------------------------

  @Benchmark
  public final double dot () { 
    a.addProducts(x0,x1);
    return Math.abs(trueDot - a.doubleValue()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
