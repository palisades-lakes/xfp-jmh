package xfp.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import xfp.java.linear.BigFractionsN;
import xfp.java.linear.Dn;
import xfp.java.linear.Qn;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.prng.PRNG;

// java -ea --illegal-access=warn -jar target/benchmarks.jar

/** Benchmark algebraic structure tests.
 * 
 * <pre>
 * java -Xmx8g -Xms8g -ea -jar target\benchmarks.jar Sum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-05
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Sum {

  private static final int DIM = 32 * 1024;
  
  private final Generator gd = 
    Generators.finiteDoubleGenerator(
      DIM,PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
  
  public final double[] x0 = (double[]) gd.next();
  public final double[] x1 = (double[]) gd.next();
  
  public final double trueSum = Qn.naiveSum(x0);
  public final double trueDot = Qn.naiveDot(x0,x1);
  
  //--------------------------------------------------------------

  @Benchmark
  public final double dnNaiveSum () { return Dn.naiveSum(x0); }

  @Benchmark
  public final double bfNaiveSum () {return BigFractionsN.naiveSum(x0); }

  @Benchmark
  public final double qnNaiveSum () {return Qn.naiveSum(x0); }

  @Benchmark
  public final double dnNaiveDot () { return Dn.naiveDot(x0,x1); }

  @Benchmark
  public final double dnFmaDot () { return Dn.fmaDot(x0,x1); }

  @Benchmark
  public final double bfNaiveDot () { return BigFractionsN.naiveDot(x0,x1); }

  @Benchmark
  public final double qnNaiveDot () {return Qn.naiveDot(x0,x1); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
