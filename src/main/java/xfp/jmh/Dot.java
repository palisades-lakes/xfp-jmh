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

/** Benchmark double dot products
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Dot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-05
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Dot {

  private static final int DIM = 1 * 1024;
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
  
  public final double trueDot = Qn.naiveDot(x0,x1);
  
  //--------------------------------------------------------------

  @Benchmark
  public final double fnNaiveDot () { 
    return  Math.abs(trueDot - Fn.naiveDot(x0,x1)) 
      / (1.0 + trueDot); }

  @Benchmark
  public final double fnFmaDot () { 
    return  Math.abs(trueDot - Fn.fmaDot(x0,x1)) 
      / (1.0 + trueDot); }

  @Benchmark
  public final double dnNaiveDot () { 
    return  Math.abs(trueDot - Dn.naiveDot(x0,x1)) 
      / (1.0 + trueDot); }

  @Benchmark
  public final double dnFmaDot () { 
    return  Math.abs(trueDot - Dn.fmaDot(x0,x1)) 
  / (1.0 + trueDot); }

  @Benchmark
  public final double bdNaiveDot () { 
    return  Math.abs(trueDot - BigDecimalsN.naiveDot(x0,x1)) 
  / (1.0 + trueDot); }

  @Benchmark
  public final double bfNaiveDot () { 
    return  Math.abs(trueDot - BigFractionsN.naiveDot(x0,x1)) 
  / (1.0 + trueDot); }

  @Benchmark
  public final double rationNaiveDot () { 
    return  Math.abs(trueDot - RatiosN.naiveDot(x0,x1)) 
  / (1.0 + trueDot); }

  @Benchmark
  public final double erNaiveDot () { 
    return  Math.abs(trueDot - ERationalsN.naiveDot(x0,x1)) 
  / (1.0 + trueDot); }

  @Benchmark
  public final double qnNaiveDot () { 
    return  Math.abs(trueDot - Qn.naiveDot(x0,x1)) 
  / (1.0 + trueDot); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
