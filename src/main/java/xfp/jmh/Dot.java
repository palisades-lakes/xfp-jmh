package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** Benchmark <code>double[]</code> dot products.
 * 
 * <pre>
 * java -ea -jar target\benchmarks.jar Dot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-04
 */

public class Dot extends Base {

  @Override
  public final double compute (final Accumulator ac,
                               final double[] z0,
                               final double[] z1) { 
    return ac.clear().addProducts(x0,x1).doubleValue(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
