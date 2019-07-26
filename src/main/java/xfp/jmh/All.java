package xfp.jmh;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.All
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-25
 */

public final class All {
  public static final void main (final String[] args)  {
    Defaults.run("TotalSum");
    Defaults.run("TotalL1Norm");
    Defaults.run("TotalL2Norm");
    Defaults.run("TotalDot");
    Defaults.run("TotalL2Distance");
    Defaults.run("TotalL1Distance");
    Defaults.run("PartialSums");
    Defaults.run("PartialL1s");
    Defaults.run("PartialL2s");
    Defaults.run("PartialDots");
    Defaults.run("PartialL2Distances");
    Defaults.run("PartialL1Distances");
  } }
