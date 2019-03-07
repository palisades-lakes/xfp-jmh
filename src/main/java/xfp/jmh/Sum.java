package xfp.jmh;

import java.util.concurrent.TimeUnit;

import org.apache.commons.rng.UniformRandomProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import xfp.java.linear.BigDecimalsN;
import xfp.java.linear.BigFractionsN;
import xfp.java.linear.Dn;
import xfp.java.linear.ERationalsN;
import xfp.java.linear.Fn;
import xfp.java.linear.Qn;
import xfp.java.linear.RatiosN;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.prng.PRNG;

// java -ea --illegal-access=warn -jar target/benchmarks.jar

/** Benchmark algebraic structure tests.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Sum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-06
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Sum {

  private static final int DIM = 2 * 1024;
  private static final int DELTA = 2 +
    ((Doubles.MAXIMUM_BIASED_EXPONENT  
    - 30 
    + Integer.numberOfLeadingZeros(2*DIM)) / 2);
  
  private final UniformRandomProvider urp = 
    PRNG.well44497b("seeds/Well44497b-2019-01-05.txt");
  private final Generator g = 
    Generators.finiteDoubleGenerator(DIM,urp,DELTA);
  
  public final double[] x00 = (double[]) g.next();
  public final double[] x0 = Dn.concatenate(x00,Dn.get(DIM).negate(x00));
  public final double[] x11 = (double[]) g.next();
  public final double[] x1 = Dn.concatenate(x11,x11);
  
  public final double trueSum = Qn.naiveSum(x0);
  public final double trueDot = Qn.naiveDot(x0,x1);
  
  //--------------------------------------------------------------

  @Benchmark
  public final double dnNaiveSum () { 
    return Math.abs(trueSum - Dn.naiveSum(x0)) 
      / (1.0 + trueSum); }

  @Benchmark
  public final double fnNaiveSum () { 
    return Math.abs(trueSum - Fn.naiveSum(x0)) 
      / (1.0 + trueSum); }

  @Benchmark
  public final double rationNaiveSum () { 
    return Math.abs(trueSum - RatiosN.naiveSum(x0)) 
      / (1.0 + trueSum); }

  @Benchmark
  public final double ernNaiveSum () { 
    return Math.abs(trueSum - ERationalsN.naiveSum(x0)) 
      / (1.0 + trueSum); }

  @Benchmark
  public final double bfNaiveSum () {
    return Math.abs(trueSum - BigFractionsN.naiveSum(x0)) 
      / (1.0 + trueSum); }

  @Benchmark
  public final double bdNaiveSum () {
    return Math.abs(trueSum - BigDecimalsN.naiveSum(x0)) 
      / (1.0 + trueSum); }

  @Benchmark
  public final double qnNaiveSum () {
    return Math.abs(trueSum - Qn.naiveSum(x0)) 
      / (1.0 + trueSum); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
