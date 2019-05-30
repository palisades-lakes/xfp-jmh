package xfp.jmh.numbers;

import java.math.BigDecimal;
import java.util.Objects;

import xfp.java.exceptions.Exceptions;
import xfp.java.numbers.Doubles;
import xfp.java.numbers.Floats;
import xfp.java.numbers.Numbers;
import xfp.java.numbers.Ringlike;

/** A sign times a {@link UnNatural0} significand times 2 to a
 * <code>int</code> exponent.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-05-30
 */

public final class BigFloat6
extends Number
implements Ringlike<BigFloat6> {

  private static final long serialVersionUID = 1L;

  //--------------------------------------------------------------
  // instance fields and methods
  //--------------------------------------------------------------

  private final boolean _nonNegative;
  public final boolean nonNegative () { return _nonNegative; }

  private final int _exponent;
  public final int exponent () { return _exponent; }

  // must always be non-negative
  private final UnNatural0 _significand;
  public final UnNatural0 significand () { return _significand; }

  //--------------------------------------------------------------

  //  private static final boolean isNegative (final UnNatural0 i) {
  //    return 0 > i.signum(); }

  private static final boolean isZero (final UnNatural0 i) {
    return i.equals(UnNatural0.ZERO); }

  public final boolean isZero () { return isZero(significand()); }

  public final boolean isOne () {
    return BigFloat6.ONE.equals(this); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat6 negate () {
    if (isZero()) { return this; }
    return valueOf(! nonNegative(),significand(),exponent()); }

  //--------------------------------------------------------------
  /** big float significands adjusted to the same exponent
   */

  private static final BigFloat6 add (final boolean n1,
                                      final long t1,
                                      final int leftShift,
                                      final boolean n0,
                                      final UnNatural0 t0,
                                      final int e) {
    //assert t0.signum() >= 0;
    if (n0 ^ n1) { // different signs
      final int c01 = t0.compareTo(t1,leftShift);
      if (0 == c01) { return ZERO; }
      // t1 > t0
      if (0 > c01) {
        return valueOf(n1, t0.subtractFrom(t1,leftShift), e); }
      // t0 > t1
      return valueOf(n0,t0.subtract(t1,leftShift),e); }
    return valueOf(n0,t0.add(t1,leftShift),e); }

  //--------------------------------------------------------------

  private final BigFloat6 add (final boolean n1,
                               final long t1,
                               final int e1) {

    if (0 == t1) { return this; }
    //assert 0 < t1;

    final boolean n0 = nonNegative();
    final UnNatural0 t0 = significand();
    final int e0 = exponent();

    // adjust significands to the same exponent
    final int de = e1 - e0;
    if (de >= 0) { return add(n1,t1,de,n0,t0,e0); }
    return add(n1,t1,0,n0,t0.shiftLeft(-de),e1); }

  //--------------------------------------------------------------

  public final BigFloat6 add (final double z) {
    //assert Double.isFinite(z);
    return add(
      Doubles.nonNegative(z),
      Doubles.significand(z),
      Doubles.exponent(z)); }

  //--------------------------------------------------------------

  private final BigFloat6 add (final boolean n1,
                               final UnNatural0 t1,
                               final int e1) {

    if (isZero(t1)) { return this; }
    //assert 0 < t1.signum();

    final boolean n0 = nonNegative();
    final UnNatural0 t0 = significand();
    final int e0 = exponent();

    final UnNatural0 t02,t12;
    final int e2;
    final int de = e1 - e0;
    if (de > 0) {
      t02 = t0; t12 = t1.shiftLeft(de); e2 = e0; }
    else if (de < 0) {
      t02 = t0.shiftLeft(-de); t12 = t1; e2 = e1; }
    else {
      t02 = t0; t12 = t1; e2 = e1; }

    if (n0 ^ n1) { // different signs
      final int c01 = t02.compareTo(t12);
      if (0 == c01) { return ZERO; }
      // t12 > t02
      if (0 > c01) { return valueOf(n1,t12.subtract(t02),e2); }
      // t02 > t12
      return valueOf(n0,t02.subtract(t12),e2); }

    return valueOf(n0,t02.add(t12),e2); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat6 add (final BigFloat6 q) {
    if (isZero()) { return q; }
    if (q.isZero()) { return this; }
    return add(q.nonNegative(),q.significand(),q.exponent()); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat6 subtract (final BigFloat6 q) {
    if (isZero()) { return q.negate(); }
    if (q.isZero()) { return this; }
    return add(
      ! q.nonNegative(),
      q.significand(),
      q.exponent()); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat6 abs () {
    if (nonNegative()) { return this; }
    return negate(); }

  //--------------------------------------------------------------

  private final BigFloat6 multiply (final boolean nonNegative,
                                    final UnNatural0 t,
                                    final int e) {
    return valueOf(
      (! (nonNegative() ^ nonNegative)),
      significand().multiply(t),
      Math.addExact(exponent(),e)); }

  @Override
  public final BigFloat6 multiply (final BigFloat6 q) {
    //    if (isZero() ) { return ZERO; }
    //    if (q.isZero()) { return ZERO; }
    //    if (q.isOne()) { return this; }
    //    if (isOne()) { return q; }
    return
      multiply(q.nonNegative(),q.significand(),q.exponent()); }

  public final BigFloat6 multiply (final double z) {
    return
      multiply(
        Doubles.nonNegative(z),
        UnNatural0.valueOf(Doubles.significand(z)),
        Doubles.exponent(z)); }

  //--------------------------------------------------------------
  // TODO: optimize!

  public final BigFloat6 add2 (final double z) {
    //assert Double.isFinite(z);
    final BigFloat6 q = valueOf(z);
    return add(q.multiply(q)); }

  //--------------------------------------------------------------
  // TODO: optimize!

  public final BigFloat6 addProduct (final double z0,
                                     final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    return add(valueOf(z0).multiply(z1)); }

  //--------------------------------------------------------------
  // Number methods
  //--------------------------------------------------------------
  /** Returns the low order bits of the truncated quotient.
   *
   * TODO: should it really truncate or round instead? Or
   * should there be more explicit round, floor, ceil, etc.?
   */
  @Override
  public final int intValue () {
    throw Exceptions.unsupportedOperation(this,"longValue"); }

  /** Returns the low order bits of the truncated quotient.
   *
   * TODO: should it really truncate or round instead? Or
   * should there be more explicit round, floor, ceil, etc.?
   */
  @Override
  public final long longValue () {
    throw Exceptions.unsupportedOperation(this,"longValue"); }

  //--------------------------------------------------------------

  private static final boolean roundUp (final UnNatural0 s,
                                        final int e) {
    //Debug.println("roundUp");
    //Debug.println("s=" + s.toString(0x10));
    //Debug.println("e=" + e);
    //Debug.println("s.testBit(e-1)=" + s.testBit(e-1));
    if (! s.testBit(e-1)) { return false; }
    for (int i=e-2;i>=0;i--) {
      //Debug.println("i=" + i + ", s.testBit(i)=" + s.testBit(i));
      if (s.testBit(i)) { return true; } }
    return s.testBit(e); }

  //--------------------------------------------------------------
  /** Adjust exponent from viewing signifcand as an integer
   * to significand as a binary 'decimal'.
   * <p>
   * TODO: make integer/fractional significand consistent
   * across all classes. Probably convert to integer
   * interpretation everywhere.
   */

  private static final float floatMergeBits (final boolean nonNegative,
                                             final int exponent,
                                             final int significand) {
    final int sh = Numbers.hiBit(significand);
    if (sh > Doubles.SIGNIFICAND_BITS) {
      return (nonNegative
        ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY); }

    final int e =
      ((sh == Floats.SIGNIFICAND_BITS)
        ? exponent + Floats.STORED_SIGNIFICAND_BITS
          : Floats.SUBNORMAL_EXPONENT);

    return Floats.unsafeBits(nonNegative,e,significand); }

  /** @return closest half-even rounded <code>float</code>
   */

  @Override
  public final float floatValue () {
    final boolean nn = nonNegative();
    final UnNatural0 s0 = significand();
    final int e0 = exponent();
    if (isZero(s0)) { return (nn ? 0.0F : -0.0F); }
    //assert (0 < s0.signum());

    final int eh = UnNatural0.hiBit(s0);
    final int es =
      Math.max(Floats.MINIMUM_EXPONENT_INTEGRAL_SIGNIFICAND - e0,
        Math.min(
          Floats.MAXIMUM_EXPONENT_INTEGRAL_SIGNIFICAND - e0 -1,
          eh - Floats.SIGNIFICAND_BITS));
    if (0 == es) {
      return floatMergeBits(nn,e0,s0.intValue()); }
    if (0 > es) {
      final int e1 = e0 + es;
      final int s1 = (s0.intValue() << -es);
      return floatMergeBits(nn,e1,s1); }
    if (eh <= es) { return (nn ? 0.0F : -0.0F); }
    // eh > es > 0
    final boolean up = roundUp(s0,es);
    // TODO: faster way to select the right bits as a int?
    final int s1 = s0.shiftRight(es).intValue();
    final int e1 = e0 + es;
    if (up) {
      final int s2 = s1 + 1;
      if (Numbers.hiBit(s2) > Floats.SIGNIFICAND_BITS) { // carry
        // lost bit has to be zero, since there was just a carry
        final int s3 = (s2 >> 1);
        final int e3 = e1 + 1;
        return floatMergeBits(nn,e3,s3); }
      // no carry
      return floatMergeBits(nn,e1,s2); }
    // round down
    return floatMergeBits(nn,e1,s1); }

  //--------------------------------------------------------------
  /** Adjust exponent from viewing signifcand as an integer
   * to significand as a binary 'decimal'.
   * <p>
   * TODO: make integer/fractional significand consistent
   * across all classes. Probably convert to integer
   * interpretation everywhere.
   */

  private static final double doubleMergeBits (final boolean nonNegative,
                                               final int exponent,
                                               final long significand) {
    //assert 0L < significand;
    final int sh = Numbers.hiBit(significand);
    if (sh > Doubles.SIGNIFICAND_BITS) {
      return (nonNegative
        ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY); }

    final int e =
      ((sh == Doubles.SIGNIFICAND_BITS)
        ? exponent + Doubles.STORED_SIGNIFICAND_BITS
          : Doubles.SUBNORMAL_EXPONENT);

    return Doubles.unsafeBits(nonNegative,e,significand); }

  //--------------------------------------------------------------
  /** @return closest half-even rounded <code>double</code>
   */

  @Override
  public final double doubleValue () {
    final boolean nn = nonNegative();
    final UnNatural0 s0 = significand();
    final int e0 = exponent();
    //Debug.println();
    //Debug.println("nn= " + nn);
    //Debug.println("s0= " + s0.toString(0x10));
    //Debug.println("e0= " + e0);
    if (isZero(s0)) { return (nn ? 0.0 : -0.0); }
    //assert (0 < s0.signum());

    final int eh = UnNatural0.hiBit(s0);
    final int es =
      Math.max(Doubles.MINIMUM_EXPONENT_INTEGRAL_SIGNIFICAND - e0,
        Math.min(
          Doubles.MAXIMUM_EXPONENT_INTEGRAL_SIGNIFICAND - e0 -1,
          eh - Doubles.SIGNIFICAND_BITS));
    //Debug.println("eh=" + eh);
    //Debug.println("es=" + es);
    if (0 == es) {
      return doubleMergeBits(nn,e0,s0.longValue()); }
    if (0 > es) {
      final int e1 = e0 + es;
      final long s1 = (s0.longValue() << -es);
      return doubleMergeBits(nn,e1,s1); }
    if (eh <= es) { return (nn ? 0.0 : -0.0); }
    // eh > es > 0
    final boolean up = roundUp(s0,es);
    //Debug.println("up=" + up);
    // TODO: faster way to select the right bits as a long?
    //Debug.println("s1=" + s0.shiftRight(es).toString(0x10));
    final long s1 = s0.shiftRight(es).longValue();
    //Debug.println("s1=" + Long.toHexString(s1) + " (long)");
    final int e1 = e0 + es;
    //Debug.println("e1=" + e1);
    if (up) {
      final long s2 = s1 + 1L;
      //Debug.println("s2=" + Long.toHexString(s2));
      //Debug.println("hiBit(s1)=" + UnNatural0.hiBit(s1));
      //Debug.println("hiBit(s2)=" + UnNatural0.hiBit(s2));
      if (Numbers.hiBit(s2) > Doubles.SIGNIFICAND_BITS) { // carry
        // lost bit has to be zero, since there was just a carry
        final long s3 = (s2 >> 1);
        final int e3 = e1 + 1;
        //Debug.println("s3=" + Long.toHexString(s3));
        //Debug.println("hiBit(s3)=" + UnNatural0.hiBit(s3));
        return doubleMergeBits(nn,e3,s3); }
      // no carry
      return doubleMergeBits(nn,e1,s2); }
    // round down
    return doubleMergeBits(nn,e1,s1); }

  //--------------------------------------------------------------
  // Comparable methods
  //--------------------------------------------------------------

  @Override
  public final int compareTo (final BigFloat6 q) {

    if (nonNegative() && (! q.nonNegative())) { return 1; }
    if ((! nonNegative()) && q.nonNegative()) { return -1; }
    // same signs
    final UnNatural0 t0 = significand();
    final UnNatural0 t1 = q.significand();
    final int e0 = exponent();
    final int e1 = q.exponent();
    final int c;
    if (e0 <= e1) { c = t0.compareTo(t1.shiftLeft(e1-e0)); }
    else { c = t0.shiftLeft(e0-e1).compareTo(t1); }
    return (nonNegative() ? c : -c); }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  public final boolean equals (final BigFloat6 q) {
    if (this == q) { return true; }
    if (null == q) { return false; }
    // assuming reduced
    return
      (nonNegative() == q.nonNegative())
      &&
      (exponent() == q.exponent())
      &&
      significand().equals(q._significand); }

  @Override
  public boolean equals (final Object o) {
    if (!(o instanceof BigFloat6)) { return false; }
    return equals((BigFloat6) o); }

  @Override
  public int hashCode () {
    int h = 17;
    h = (31*h) + (nonNegative() ? 0 : 1);
    h = (31*h) + exponent();
    h = (31*h) + Objects.hash(significand());
    return h; }

  @Override
  public final String toString () {
    assert (0 == UnNatural0.loBit(significand()))
    || isZero(significand()) :
      significand().toString()
      + "\nlo= " + UnNatural0.loBit(significand());
    return
      (nonNegative() ? "" : "-")
      + "0x" + significand().toString()
      + "p" + exponent(); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BigFloat6 (final boolean nonNegative,
                     final UnNatural0 t0,
                     final int e0) {
    //super();
    if (isZero(t0)) {
      _nonNegative = true;
      _significand = UnNatural0.ZERO;
      _exponent = 0; }
    else {
      final int e1 = Math.max(0,UnNatural0.loBit(t0));
      _nonNegative = nonNegative;
      if (e1 == 0) {
        _significand = t0;
        _exponent = e0;  }
      else {
        _significand = t0.shiftRight(e1);
        _exponent = Math.addExact(e0,e1);  }
      //      assert 0 == UnNatural0.loBit(_significand)
      //        : "lowBit= " + UnNatural0.loBit(_significand)
      //        +"; rightShift= " + e1;
    } }

  //--------------------------------------------------------------

  public static final BigFloat6 valueOf (final boolean nonNegative,
                                         final UnNatural0 t,
                                         final int e) {
    if (isZero(t)) { return ZERO; }
    //assert 0 < t.signum();
    return new BigFloat6(nonNegative,t,e); }

  public static final BigFloat6 valueOf (final long t,
                                         final int e) {
    if (0L < t) {
      return valueOf(true,UnNatural0.valueOf(t),e); }
    return valueOf(false,UnNatural0.valueOf(-t),e); }

  public static final BigFloat6 valueOf (final int t,
                                         final int e) {
    if (0 < t) {
      return valueOf(true,UnNatural0.valueOf(t),e); }
    return valueOf(false,UnNatural0.valueOf(-t),e); }

  //--------------------------------------------------------------

  private static final BigFloat6 valueOf (final boolean nonNegative,
                                          final int e0,
                                          final long t0)  {
    if (0L == t0) { return ZERO; }
    return valueOf(nonNegative,UnNatural0.valueOf(t0),e0); }

  public static final BigFloat6 valueOf (final double x)  {
    return valueOf(
      Doubles.nonNegative(x),
      Doubles.exponent(x),
      Doubles.significand(x)); }

  //--------------------------------------------------------------

  private static final BigFloat6 valueOf (final boolean nonNegative,
                                          final int e0,
                                          final int t0)  {
    if (0 == t0) { return ZERO; }
    return valueOf(nonNegative,UnNatural0.valueOf(t0),e0); }

  public static final BigFloat6 valueOf (final float x)  {
    return valueOf(
      Floats.nonNegative(x),
      Floats.exponent(x),
      Floats.significand(x)); }

  //--------------------------------------------------------------

  public static final BigFloat6 valueOf (final byte t)  {
    if (0 < t) {
      return valueOf(true,UnNatural0.valueOf(t),0); }
    return valueOf(false,UnNatural0.valueOf(-t),0); }

  public static final BigFloat6 valueOf (final short t)  {
    if (0 < t) {
      return valueOf(true,UnNatural0.valueOf(t),0); }
    return valueOf(false,UnNatural0.valueOf(-t),0); }

  public static final BigFloat6 valueOf (final int t)  {
    if (0 < t) {
      return valueOf(true,UnNatural0.valueOf(t),0); }
    return valueOf(false,UnNatural0.valueOf(-t),0); }

  public static final BigFloat6 valueOf (final long t)  {
    if (0 < t) {
      return valueOf(true,UnNatural0.valueOf(t),0); }
    return valueOf(false,UnNatural0.valueOf(-t),0); }

  //--------------------------------------------------------------

  public static final BigFloat6 valueOf (final Double x)  {
    return valueOf(x.doubleValue()); }

  public static final BigFloat6 valueOf (final Float x)  {
    return valueOf(x.floatValue()); }

  public static final BigFloat6 valueOf (final Byte x)  {
    return valueOf(x.byteValue()); }

  public static final BigFloat6 valueOf (final Short x)  {
    return valueOf(x.shortValue()); }

  public static final BigFloat6 valueOf (final Integer x)  {
    return valueOf(x.intValue()); }

  public static final BigFloat6 valueOf (final Long x)  {
    return valueOf(x.longValue()); }

  public static final BigFloat6 valueOf (final BigDecimal x)  {
    throw Exceptions.unsupportedOperation(null,"valueOf",x); }
  //    return valueOf(x, UnNatural0.ONE); }

  public static final BigFloat6 valueOf (final UnNatural0 x)  {
    return valueOf(true,x,0); }

  public static final BigFloat6 valueOf (final Number x)  {
    if (x instanceof BigFloat6) { return (BigFloat6) x; }
    if (x instanceof Double) { return valueOf((Double) x); }
    if (x instanceof Float) { return valueOf((Float) x); }
    if (x instanceof Byte) { return valueOf((Byte) x); }
    if (x instanceof Short) { return valueOf((Short) x); }
    if (x instanceof Integer) { return valueOf((Integer) x); }
    if (x instanceof Long) { return valueOf((Long) x); }
    if (x instanceof UnNatural0) { return valueOf((UnNatural0) x); }
    if (x instanceof BigDecimal) { return valueOf((BigDecimal) x); }
    throw Exceptions.unsupportedOperation(null,"valueOf",x); }

  public static final BigFloat6 valueOf (final Object x)  {
    return valueOf((Number) x); }

  //--------------------------------------------------------------

  public static final BigFloat6 ZERO =
    new BigFloat6(true,UnNatural0.ZERO,0);

  public static final BigFloat6 ONE =
    new BigFloat6(true,UnNatural0.ONE,0);

  public static final BigFloat6 TWO =
    new BigFloat6(true,UnNatural0.ONE,1);

  public static final BigFloat6 TEN =
    new BigFloat6(true,UnNatural0.valueOf(5),1);

  public static final BigFloat6 MINUS_ONE =
    new BigFloat6(false,UnNatural0.ONE,0);

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
