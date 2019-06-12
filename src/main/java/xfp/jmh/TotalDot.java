package xfp.jmh;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.TotalDot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-12
 */

public class TotalDot extends Base {

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) { 
    return ac.clear().addProducts(z0,z1).doubleValue(); }

  public static final void main (final String[] args) 
    throws RunnerException {
    new Runner( 
      Defaults.options("TotalDot","TotalDot"))
    .run(); }
}
