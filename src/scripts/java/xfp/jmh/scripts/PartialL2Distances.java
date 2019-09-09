package xfp.jmh.scripts;

import xfp.java.accumulators.Accumulator;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.jmh.accumulators.BigFloatAccumulator0;

/** Benchmark partial l2 norms
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/jmh/scripts/PartialL2Distances.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-08
 */
@SuppressWarnings("unchecked")
public final class PartialL2Distances {

  public static final void main (final String[] args) {
    final int dim = (1*1024*1024) - 1;
    final int trys = 8 * 1024;
    final Generator g = Generators.make("exponential",dim);
    //final Generator g = Generators.make("finite",dim);
    //final Generator g = Generators.make("gaussian",dim);
    //final Generator g = Generators.make("laplace",dim);
    //final Generator g = Generators.make("uniform",dim);
    final Accumulator a = BigFloatAccumulator0.make();
    assert a.isExact();
    for (int i=0;i<trys;i++) {
      final double[] x0 = (double[]) g.next();
      final double[] x1 = (double[]) g.next();
      final double[] z = a.partialL2Distances(x0,x1); 
      assert ! Double.isNaN(z[dim-1]);} }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
