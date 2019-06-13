package xfp.jmh;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.Totals
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-12
 */

public final class Totals {

  public static final void main (final String[] args) 
    throws RunnerException {
    new Runner(Defaults.options("Total","Total"))
    .run(); }
}
