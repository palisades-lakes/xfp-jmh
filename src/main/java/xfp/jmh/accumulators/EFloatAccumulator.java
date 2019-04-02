package xfp.jmh.accumulators;

import com.upokecenter.numbers.EFloat;
import com.upokecenter.numbers.EInteger;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with EFloat 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-02
 */
public final class EFloatAccumulator 

implements Accumulator<EFloatAccumulator> {

  private EFloat _sum;

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------

  /** @see Double#toHextString(double)
   */
  public static final String toHexString (final EFloat x) {
    if (x.IsNaN()) { return "NaN"; }
    final int s = x.signum();
    final String ss = (s < 0) ? "-" : "";
    if (x.IsInfinity()) { return ss + "Infinity"; }
    if (x.isZero()) { return ss + "0x0.0p0"; }
    final EInteger e = x.getExponent();
    final EInteger t = x.getUnsignedMantissa();
    return 
      ss + "0x" + t.ToRadixString(0x10) + "p" +
      e.toString(); }

  /** @see Double#toHextString(double)
   */
  public final String toHexString () {
    return toHexString(_sum); }

  //--------------------------------------------------------------
  // Accumulator methods
  //--------------------------------------------------------------
  @Override
  public final boolean isExact () { return true; }


  @Override
  public final double doubleValue () { 
    return _sum.ToDouble(); }

  @Override
  public final EFloatAccumulator clear () { 
    _sum = EFloat.Zero;
    return this; }

  @Override
  public final EFloatAccumulator add (final double z) { 
    _sum = _sum.Add(EFloat.FromDouble(z));
    return this; }

  @Override
  public final EFloatAccumulator add2 (final double z) { 
    final EFloat zz = EFloat.FromDouble(z);
    _sum = _sum.Add(zz.Multiply(zz));
    return this; }

  @Override
  public final EFloatAccumulator addProduct (final double z0,
                                             final double z1) { 
    _sum = _sum.Add(
      EFloat.FromDouble(z0)
      .Multiply(
        EFloat.FromDouble(z1)));
    return this; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final String toString () {
    return _sum.toString(); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private EFloatAccumulator () { super(); clear(); }

  public static final EFloatAccumulator make () {
    return new EFloatAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------