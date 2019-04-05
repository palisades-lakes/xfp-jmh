package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** Benchmark <code>double[]</code> sum.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Sum -rf csv  -rff output\Sums
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-05
 */

public class Sum extends Base {

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) { 
    return ac.clear().addAll(x0).addAll(x1).doubleValue(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
