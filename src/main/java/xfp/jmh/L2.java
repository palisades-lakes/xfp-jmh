package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** Benchmark <code>double[]</code> sums.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Sum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-03
 */

//@SuppressWarnings("unchecked")
//@State(Scope.Thread)
//@BenchmarkMode(Mode.AverageTime)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)

public class L2 extends Base {

  @Override
  public final double compute (final Accumulator ac,
                               final double[] z0,
                               final double[] z1) { 
    return a.clear().add2All(x0).add2All(x1).doubleValue(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
