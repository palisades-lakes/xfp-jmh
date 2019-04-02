package xfp.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

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
public class Sum extends Base {

  @Benchmark
  public final double sum () { 
    final double pred = 
      a.clear().addAll(x0).addAll(x1).doubleValue();
    final double residual = trueSum - pred; 
    return residual; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
