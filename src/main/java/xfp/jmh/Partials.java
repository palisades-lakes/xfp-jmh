package xfp.jmh;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.Partials
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-24
 */

public final class Partials {
  public static final void main (final String[] args)  {
    Defaults.run("PartialSums");
    Defaults.run("PartialL1s");
    Defaults.run("PartialL2s");
    Defaults.run("PartialDots");
    Defaults.run("PartialL2Distances");
    Defaults.run("PartialL1Distances");
  } }
