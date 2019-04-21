package xfp.jmh.accumulators;

import com.upokecenter.numbers.EInteger;
import com.upokecenter.numbers.ERational;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with ERational 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-20
 */
public final class ERationalAccumulator 

implements Accumulator<ERationalAccumulator> {

  private ERational _sum;

  //--------------------------------------------------------------

  private static final ERational reduce (final ERational q) {
    final EInteger n0 = q.getNumerator();
    final EInteger d0 = q.getDenominator();
    final int e = Math.min(n0.GetLowBit(),d0.GetLowBit());
    final EInteger n1 = n0.ShiftRight(e);
    final EInteger d1 = d0.ShiftRight(e);
    final EInteger gcd = n1.Gcd(d1);
    //if (EInteger.getOne().equals(gcd)) { return q; }
    final EInteger n2 = n1.Divide(gcd);
    final EInteger d2 = d1.Divide(gcd);
    return ERational.Create(n2,d2); }

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final boolean noOverflow () { return true; }

  @Override
  public final Object value () { return _sum; }

  @Override
  public final double doubleValue () { return _sum.ToDouble(); }

  @Override
  public final ERationalAccumulator clear () { 
    _sum = ERational.Zero;
    return this; }

  @Override
  public final ERationalAccumulator add (final double z) { 
    assert Double.isFinite(z);
    _sum = reduce(_sum.Add(ERational.FromDouble(z)));
    return this; }

  @Override
  public final ERationalAccumulator add2 (final double z) {
    assert Double.isFinite(z);
    final ERational zz = ERational.FromDouble(z);
    _sum = reduce(_sum.Add(zz.Multiply(zz)));
    return this; }

  @Override
  public final ERationalAccumulator addProduct (final double z0,
                                                final double z1) { 
    assert Double.isFinite(z0);
    assert Double.isFinite(z1);
    _sum = reduce(
      _sum.Add(
        ERational.FromDouble(z0)
        .Multiply(ERational.FromDouble(z1))));
    return this; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  public static final String toString (final ERational q) {
    return 
      q.getNumerator().ToRadixString(0x10) 
      + " / "
//      + "\n/\n"
      + q.getDenominator().ToRadixString(0x10); }
    
  @Override
  public final String toString () {
    return toString(_sum); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ERationalAccumulator () { super(); clear(); }

  public static final ERationalAccumulator make () {
    return new ERationalAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
