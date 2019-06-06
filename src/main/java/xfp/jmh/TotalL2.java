package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** Benchmark <code>double[]</code> squared l2 norm.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar TotalL2
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-12
 */

public class TotalL2 extends Base {

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) { 
    return ac.clear().add2All(z0).add2All(z1).doubleValue(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
