package xfp.jmh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.ListSampler;
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
import xfp.java.linear.Dn;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.jmh.accumulators.ERationalSum;

/** Benchmark double dot products
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Dot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-26
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RationalDot {

  //--------------------------------------------------------------
  //  /** See {@link Integer#numberOfLeadingZeros(int)}. */
  //  private static final int floorLog2 (final int k) {
  //    return Integer.SIZE - 1- Integer.numberOfLeadingZeros(k); }

  /** See {@link Integer#numberOfLeadingZeros(int)}. */
  private static final int ceilLog2 (final int k) {
    return Integer.SIZE - Integer.numberOfLeadingZeros(k-1); }

  // TODO: more efficient via bits?
  private static final boolean isEven (final int k) {
    return k == 2*(k/2); }

  /** Maximum exponent for double generation such that the sum 
   * of <code>dim</code> <code>double</code>s will be finite
   * (with high enough probability).
   */
  //  private static final int deMax (final int dim) { 
  //    final int d = Doubles.MAXIMUM_EXPONENT - ceilLog2(dim);
  //    System.out.println("emax=" + d);
  //    return d; }

  /** Maximum exponent for double generation such that a float sum 
   * of <code>dim</code> <code>double</code>s will be finite
   * (with high enough probability).
   */
  private static final int feMax (final int dim) { 
    final int d = Float.MAX_EXPONENT - ceilLog2(dim);
    //System.out.println("emax=" + d);
    return d; }

  private static double[] sampleDoubles (final Generator g,
                                         final UniformRandomProvider urp) {
    double[] x = (double[]) g.next();
    // exact sum is 0.0
    x = Dn.concatenate(x,Dn.minus(x));
    ListSampler.shuffle(urp,Arrays.asList(x));
    return x; }

  private static double[][] sampleDoubles (final int dim,
                                           final int n) {
    assert isEven(dim);
    final UniformRandomProvider urp = 
      PRNG.well44497b("seeds/Well44497b-2019-01-05.txt");
    final Generator g = 
      Doubles.finiteGenerator(dim,urp,feMax(dim));

    final double[][] x = new double[n][];
    for (int i=0;i<n;i++) { x[i] = sampleDoubles(g,urp); }
    return x; }

  //--------------------------------------------------------------

  private static final int DIM = 1*1024;

  double[] x0;
  double[] x1;
  double trueDot;

  @Param({
//    "xfp.java.accumulators.BigDecimalSum",
//    "xfp.jmh.accumulators.BigFractionSum",
//    "xfp.java.accumulators.DoubleSum",
//    "xfp.java.accumulators.DoubleFmaSum",
//    "xfp.jmh.accumulators.EFloatSum",
//    "xfp.jmh.accumulators.ERationalSum",
//    "xfp.java.accumulators.FloatSum",
//    "xfp.java.accumulators.FloatFmaSum",
//    "xfp.jmh.accumulators.RatioSum",
//    "xfp.java.accumulators.MutableRationalSum",
    "xfp.java.accumulators.Rational0Sum",
    "xfp.java.accumulators.RationalSum",
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
    x0 = sampleDoubles(DIM,1)[0];
    x1 = sampleDoubles(DIM,1)[0];
    final Accumulator a0 = ERationalSum.make();
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
