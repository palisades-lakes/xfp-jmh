package xfp.jmh.scripts;

import xfp.java.accumulators.Accumulator;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.jmh.accumulators.BigFloat0Accumulator;

/** Benchmark partial sums.
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/jmh/scripts/PartialSums.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-07
 */
@SuppressWarnings("unchecked")
public final class PartialSums {

  public static final void main (final String[] args) {
    final int dim = (1*1024*1024) - 1;
    final int trys = 8 * 1024;
    //final Generator g = Generators.make("exponential",dim);
    //final Generator g = Generators.make("finite",dim);
    //final Generator g = Generators.make("gaussian",dim);
    //final Generator g = Generators.make("laplace",dim);
    final Generator g = Generators.make("uniform",dim);
    final Accumulator a = BigFloat0Accumulator.make();
    assert a.isExact();
    for (int i=0;i<trys;i++) {
      final double[] x = (double[]) g.next();
      final double[] s = a.partialSums(x);
      assert ! Double.isNaN(s[dim-1]); } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
