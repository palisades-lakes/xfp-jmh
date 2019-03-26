package xfp.jmh.accumulators;

import com.upokecenter.numbers.ERational;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with ERational 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-11
 */
public final class ERationalSum implements Accumulator<ERationalSum> {

  private ERational _sum;

  //--------------------------------------------------------------

//  private static final ERational reduce (final ERational e) {
//    // not correct!!!
//    // ignoring flags, infinity, NaN, ..
//    final EInteger n = e.getNumerator();
//    final EInteger d = e.getDenominator();
//    final EInteger g = n.Gcd(d);
//    if (g.compareTo(EInteger.getOne()) <= 0) { return e; }
//    return ERational.Create(n.Divide(g),d.Divide(g)); }

  //--------------------------------------------------------------
  // start with only immediate needs

  @Override
  public final double doubleValue () { 
    return _sum.ToDouble(); }

  @Override
  public final ERationalSum clear () { 
    _sum = ERational.Zero;
    return this; }

  @Override
  public final ERationalSum add (final double z) { 
    _sum = _sum.Add(ERational.FromDouble(z));
    return this; }

  //  @Override
  //  public final ERationalSum addAll (final double[] z)  {
  //    for (final double zi : z) { 
  //      _sum = _sum.Add(ERational.FromDouble(zi)); }
  //    return this; }

  @Override
  public final ERationalSum addProduct (final double z0,
                                        final double z1) { 
    _sum = _sum.Add(
      ERational.FromDouble(z0)
      .Multiply(ERational.FromDouble(z1)));
    return this; }

  //@Override
  //public final ERationalSum addProducts (final double[] z0,
  //                                        final double[] z1)  {
  //    final int n = z0.length;
  //    assert n == z1.length;
  //    for (int i=0;i<n;i++) { 
  //  sum = _sum.Add(
  //    ERational.FromDouble(z0[i])
  //    .Multiply(
  //      ERational.FromDouble(z1[i])));}
  //    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ERationalSum () { super(); clear(); }

  public static final ERationalSum make () {
    return new ERationalSum(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------