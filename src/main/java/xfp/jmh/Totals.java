package xfp.jmh;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.Totals
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-14
 */

public final class Totals {
  public static final void main (final String[] args)  {
    Defaults.run("TotalL2Distance");
    Defaults.run("TotalL2Norm");
    Defaults.run("TotalDot");
    Defaults.run("TotalSum");
    Defaults.run("TotalL1Distance");
    Defaults.run("TotalL1Norm"); } }
