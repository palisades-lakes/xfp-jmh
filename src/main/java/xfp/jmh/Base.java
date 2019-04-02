package xfp.jmh;

import java.util.concurrent.TimeUnit;

import org.apache.commons.rng.UniformRandomProvider;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import xfp.java.accumulators.Accumulator;
import xfp.java.accumulators.RBFAccumulator;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;

// java -ea --illegal-access=warn -jar target/benchmarks.jar

/** Benchmark double sums.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Sum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-02
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class Base {

  //--------------------------------------------------------------
  private static final int DIM = 1024 * 1024;
  private static final UniformRandomProvider URP = 
    PRNG.well44497b("seeds/Well44497b-2019-01-05.txt");
  private static final int emax = Common.deMax(DIM)/2;
  private static final double dmax = (1<<emax);
  public static final Generator G = 
    Doubles.exponentialGenerator(DIM,URP,0.0,dmax);
  //--------------------------------------------------------------

  double[] x0;
  double[] x1;
  double trueSum;
  double trueDot;
  double trueL2;

  @Param({
//    "xfp.java.accumulators.BigDecimalAccumulator",
//    "xfp.jmh.accumulators.BigFractionAccumulator",
    "xfp.java.accumulators.DoubleAccumulator",
//    "xfp.java.accumulators.DoubleFmaAccumulator",
    "xfp.jmh.accumulators.KahanAccumulator",
//    "xfp.jmh.accumulators.EFloatAccumulator",
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
  public final void setup () {
    x0 = (double[]) G.next();
    x1 = (double[]) G.next();
    final Accumulator a0 = RBFAccumulator.make();
    assert a0.isExact();
    trueSum = a0.clear().addAll(x0).addAll(x1).doubleValue();
    trueDot = a0.clear().addProducts(x0,x1).doubleValue();
    trueL2 = a0.clear().add2All(x0).add2All(x1).doubleValue();
    a = Common.makeAccumulator(className); }  


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
