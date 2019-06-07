package xfp.jmh;

import java.util.Arrays;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.TotalSum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-06
 */

public class TotalSum extends Base {

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) { 
    return ac.clear().addAll(z0).addAll(z1).doubleValue(); }


  public static final void main (final String[] args) 
    throws RunnerException {
    System.err.println("args=" + Arrays.toString(args));
    final Options opt = 
      Defaults.options("TotalSum","TotalSum");
    System.err.println(opt.toString());
    new Runner(opt).run(); }
}

