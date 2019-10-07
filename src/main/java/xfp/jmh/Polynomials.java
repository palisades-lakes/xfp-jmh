package xfp.jmh;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.Polynomials
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-07
 */

public final class Polynomials {
  public static final void main (final String[] args)  {
    Defaults.run("AxpyBench");
    Defaults.run("QuadraticBench");
    Defaults.run("CubicBench");
  } }
