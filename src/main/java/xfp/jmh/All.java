package xfp.jmh;

/** <pre>
 * j xfp.jmh.All
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-08-03
 */

public final class All {
  public static final void main (final String[] args)  {
    Defaults.run("TotalSum");
    Defaults.run("TotalDot");
    Defaults.run("TotalL1Norm");
    Defaults.run("TotalL2Norm");
    Defaults.run("TotalL2Distance");
    Defaults.run("TotalL1Distance");
    Defaults.run("PartialSums");
    Defaults.run("PartialDots");
    Defaults.run("PartialL1s");
    Defaults.run("PartialL2s");
    Defaults.run("PartialL2Distances");
    Defaults.run("PartialL1Distances");
  } }
