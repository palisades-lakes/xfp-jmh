package xfp.jmh.accumulators;

import static java.lang.Double.isFinite;
import static xfp.java.numbers.Doubles.EXPONENT_MASK;
import static xfp.java.numbers.Doubles.MINIMUM_SUBNORMAL_EXPONENT;
import static xfp.java.numbers.Doubles.SIGN_MASK;
import static xfp.java.numbers.Doubles.STORED_SIGNIFICAND_BITS;
import static xfp.java.numbers.Doubles.STORED_SIGNIFICAND_MASK;

import xfp.java.numbers.BigInteger;

import xfp.java.accumulators.Accumulator;
import xfp.java.numbers.Rational;

/** Naive sum of <code>double</code> values with BigInteger pair 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-21
 */
public final class MutableRationalAccumulator 

implements 
Accumulator<MutableRationalAccumulator>, 
Comparable<MutableRationalAccumulator> {

  //--------------------------------------------------------------
  // instance fields and methods
  //--------------------------------------------------------------

  private BigInteger _numerator;
  public final BigInteger numerator () { return _numerator; }
  private BigInteger _denominator;
  public final BigInteger denominator () { return _denominator; }

  //--------------------------------------------------------------
  /** From apache commons math4 BigFraction.
   * <p>
   * Create a fraction given the double value.
   * <p>
   * This constructor behaves <em>differently</em> from
   * {@link #BigFraction(double, double, int)}. It converts the 
   * double value exactly, considering its internal bits 
   * representation. This works for all values except NaN and 
   * infinities and does not requires any loop or convergence 
   * threshold.
   * </p>
   * <p>
   * Since this conversion is exact and since double numbers are 
   * sometimes approximated, the fraction created may seem strange 
   * in some cases. For example, calling 
   * <code>new BigFraction(1.0 / 3.0)</code> does <em>not</em> 
   * create the fraction 1/3, but the fraction 
   * 6004799503160661 / 18014398509481984, because the double 
   * number passed to the constructor is not exactly 1/3
   * (this number cannot be stored exactly in IEEE754).
   * </p>
   * @see #BigFraction(double, double, int)
   * @param x the double value to convert to a fraction.
   * @exception IllegalArgumentException if value is not finite
   */

  private static final BigInteger[] toRatio (final double x) {
    if (! isFinite(x)) {
      throw new IllegalArgumentException(
        "toRatio"  + " cannot handle "+ x); }

    final BigInteger numerator;
    final BigInteger denominator;

    // compute m and k such that x = m * 2^k
    final long bits     = Double.doubleToLongBits(x);
    final long sign     = bits & SIGN_MASK;
    final long exponent = bits & EXPONENT_MASK;
    long m              = bits & STORED_SIGNIFICAND_MASK;

    if (exponent == 0) { // subnormal or zero
      if (0L == m) {
        numerator   = BigInteger.ZERO;
        denominator = BigInteger.ONE; }
      else {
        if (sign != 0L) { m = -m; }
        numerator   = BigInteger.valueOf(m);
        denominator = 
          BigInteger.ZERO.setBit(-MINIMUM_SUBNORMAL_EXPONENT); } }
    else { // normal
      // add the implicit most significant bit
      m |= (1L << STORED_SIGNIFICAND_BITS); 
      if (sign != 0L) { m = -m; }
      int k = 
        ((int) (exponent >> STORED_SIGNIFICAND_BITS)) 
        + MINIMUM_SUBNORMAL_EXPONENT - 1;
      while (((m & (STORED_SIGNIFICAND_MASK - 1L)) != 0L) 
        &&
        ((m & 0x1L) == 0L)) {
        m >>= 1; 
        ++k; }
      if (k < 0) { 
        numerator   = BigInteger.valueOf(m);
        denominator = BigInteger.ZERO.flipBit(-k); } 
      else {
        numerator   = BigInteger.valueOf(m)
          .multiply(BigInteger.ZERO.flipBit(k));
        denominator = BigInteger.ONE; } } 

    return new BigInteger[]{ numerator, denominator}; }

  //--------------------------------------------------------------

  public final MutableRationalAccumulator reduce () {
    if (_numerator == BigInteger.ZERO) {
      _denominator = BigInteger.ONE; }
    else {
      final BigInteger gcd = _numerator.gcd(_denominator);
      if (gcd.compareTo(BigInteger.ONE) > 0) {
        _numerator = _numerator.divide(gcd);
        _denominator = _denominator.divide(gcd); } }
    return this; }

  private final MutableRationalAccumulator add (final BigInteger n,
                                                final BigInteger d) {
    if (0 == _numerator.signum()) {
      _numerator = n;
      _denominator = d; }
    else {
      _numerator = 
        _numerator.multiply(d).add(n.multiply(_denominator));
      _denominator = _denominator.multiply(d); }
    return this; }

  //--------------------------------------------------------------
  // Accumulator interface
  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final boolean noOverflow () { return true; }

  @Override
  public final Object value () { 
    return Rational.valueOf(numerator(),denominator()); }

  @Override
  public final double doubleValue () { 
    return 
      Rational.valueOf(numerator(),denominator()).doubleValue(); }

  @Override
  public final float floatValue () { 
    return 
      Rational.valueOf(numerator(),denominator()).floatValue(); }

  @Override
  public final MutableRationalAccumulator clear () { 
    _numerator = BigInteger.ZERO;
    _denominator = BigInteger.ONE;
    return this; }

  @Override
  public final MutableRationalAccumulator add (final double z) { 
    assert Double.isFinite(z);
   // would be nice to have multiple value return...
    final BigInteger[] nd = toRatio(z);
    return add(nd[0],nd[1])
      .reduce()
      ; }

  @Override
  public final MutableRationalAccumulator add2 (final double z) { 
    assert Double.isFinite(z);
    final BigInteger[] nd = toRatio(z);
    return 
      add(nd[0].multiply(nd[0]),nd[1].multiply(nd[1])).reduce(); }

  @Override
  public final MutableRationalAccumulator addProduct (final double z0,
                                                      final double z1) { 
    assert Double.isFinite(z0);
    assert Double.isFinite(z1);
    final BigInteger[] nd0 = toRatio(z0);
    final BigInteger[] nd1 = toRatio(z1);
    return add(
      nd0[0].multiply(nd1[0]),
      nd0[1].multiply(nd1[1]))
      .reduce()
      ; }

  //--------------------------------------------------------------
  // Comparable methods
  //--------------------------------------------------------------

  @Override
  public final int compareTo (final MutableRationalAccumulator o) {
    final BigInteger n0d1 = _numerator.multiply(o._denominator);
    final BigInteger n1d0 = o._numerator.multiply(_denominator);
    return n0d1.compareTo(n1d0); }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final String toString () {
    return 
      "(" + _numerator.toString(0x10) 
      + " / " + _denominator.toString(0x10) 
      + ")"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private MutableRationalAccumulator (final BigInteger numerator,
                                      final BigInteger denominator) { 
    super(); 
    assert 0 != denominator.signum();

    if (denominator.signum() < 0) {
      _numerator = numerator.negate();
      _denominator = denominator.negate(); } 
    else {
      _numerator = numerator;
      _denominator = denominator; } 
    reduce(); }

  public static final MutableRationalAccumulator valueOf (final BigInteger n,
                                                          final BigInteger d) {
    return new MutableRationalAccumulator(n,d); }

  public static final MutableRationalAccumulator valueOf (final long n,
                                                          final long d) {
    return valueOf(BigInteger.valueOf(n),BigInteger.valueOf(d)); }

  public static final MutableRationalAccumulator valueOf (final int n,
                                                          final int d) {
    return valueOf(BigInteger.valueOf(n),BigInteger.valueOf(d)); }

  public static final MutableRationalAccumulator make () {
    return valueOf(BigInteger.ZERO,BigInteger.ONE); }

  public static final MutableRationalAccumulator valueOf (final double z) {
    return make().add(z); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
