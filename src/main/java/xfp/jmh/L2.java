package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** Benchmark <code>double[]</code> squared l2 norm.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Sum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-04
 */

public class L2 extends Base {

  @Override
  public final double compute (final Accumulator ac,
                               final double[] z0,
                               final double[] z1) { 
    return ac.clear().add2All(x0).add2All(x1).doubleValue(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
