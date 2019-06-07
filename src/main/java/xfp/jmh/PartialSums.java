package xfp.jmh;

import java.util.Arrays;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.PartialSums
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-06
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class PartialSums extends Base {

  //--------------------------------------------------------------

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) { 
    p0 = acc.clear().partialSums(z0);
    p1 = acc.clear().partialSums(z1);
    final double l20 = 
      ((null==true0) ? 0.0 : // for initialization
        exact.clear().addL2Distance(true0,p0).doubleValue()); 
    final double l21 = 
      ((null==true1) ? 0.0 : // for initialization
        exact.clear().addL2Distance(true1,p1).doubleValue());
    if (acc.isExact()) { assert 0.0==l20; assert 0.0==l21; }
    return l20 + l21; }

  public static final void main (final String[] args) 
    throws RunnerException {
    System.err.println("args=" + Arrays.toString(args));
    final Options opt = 
      Defaults.options("PartialSums","PartialSums");
    System.err.println(opt.toString());
    new Runner(opt).run(); }
}
