package xfp.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/** Benchmark double dot products
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar RationalDot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-02
 */
@SuppressWarnings("unchecked")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RationalDot extends Base {

  //--------------------------------------------------------------

  @Benchmark
  public final double dot () { 
    final double pred = 
      a.clear().addProducts(x0,x1).doubleValue();
    final double residual = trueDot - pred; 
    return residual; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
