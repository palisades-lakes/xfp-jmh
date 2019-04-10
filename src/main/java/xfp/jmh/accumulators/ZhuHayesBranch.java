package xfp.jmh.accumulators;

import xfp.java.numbers.Doubles;

//----------------------------------------------------------------
/** <em>NOT</em> thread safe!
 * <p>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-09
 */

public final class ZhuHayesBranch extends ZhuHayes {

  @Override
  protected final void twoSum (final double x0, 
                               final double x1) {

    // might get +/- Infinity due to overflow
//    assert Double.isFinite(x0) && Double.isFinite(x1) :
//      Double.toHexString(x0) + " + " + Double.toHexString(x1);

    sumTwo = x0 + x1;
    if (Doubles.biasedExponent(x0) > Doubles.biasedExponent(x1)) {
      errTwo = x1 - (sumTwo - x0); }
    else {
      errTwo = x0 - (sumTwo - x1); }

    // overflow to infinite SumTwo is possible
    // TODO: handle better
//    assert Double.isFinite(sumTwo) && Double.isFinite(errTwo) :
//      "\n" 
//      + Double.toHexString(x0) + " + " + Double.toHexString(x1)
//      + "\n=>\n"
//      + Double.toHexString(sumTwo) + " + " + Double.toHexString(errTwo); 
  }

  //--------------------------------------------------------------
  // Accumulator interface
  //--------------------------------------------------------------

  // construction
  //--------------------------------------------------------------

  private ZhuHayesBranch () { super(); }

  public static final ZhuHayesBranch make () {
    return new ZhuHayesBranch(); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
