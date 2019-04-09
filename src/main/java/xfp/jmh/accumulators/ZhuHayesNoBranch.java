package xfp.jmh.accumulators;

//----------------------------------------------------------------
/** <em>NOT</em> thread safe!
 * <p>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-09
 */

public final class ZhuHayesNoBranch extends ZhuHayes {

  @Override
  protected final void twoSum (final double x0, 
                               final double x1) {

    assert Double.isFinite(x0) && Double.isFinite(x1) :
      Double.toHexString(x0) + " + " + Double.toHexString(x1);

    sumTwo = x0 + x1;
    final double z = sumTwo - x0;
    errTwo = (x0 - (sumTwo - z)) + (x1 - z);

    assert Double.isFinite(sumTwo) && Double.isFinite(errTwo) :
      Double.toHexString(sumTwo) + " + " + Double.toHexString(errTwo); 
  }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ZhuHayesNoBranch () { super(); }

  public static final ZhuHayesNoBranch make () {
    return new ZhuHayesNoBranch(); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
