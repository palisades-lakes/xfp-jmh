package xfp.jmh.scripts;

import static java.lang.Double.isFinite;
import static xfp.java.numbers.Numbers.description;
import static xfp.java.numbers.Doubles.*;

import java.math.BigInteger;

import xfp.java.numbers.Doubles;
import xfp.java.numbers.Rational;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.prng.PRNG;

/** BigInteger pair: divide and round to double.
 * 
 * <pre>
 * j --source 11 src/scripts/java/xfp/java/scripts/DivideDouble.java > divide.txt 2>&1
 * </pre>
 * Profiling:
 * <pre>
 * jy --source 11 src/scripts/java/xfp/java/scripts/DivideDouble.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-30
 */

@SuppressWarnings("unchecked")
public final class DivideDouble {

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

  public static final BigInteger[] toRatio (final double x) {
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

  public static final double roundingTest (final BigInteger n,
                                           final BigInteger d) {
    try {
      final double z = Rational.valueOf(n,d).doubleValue();
      Debug.println(Double.toHexString(z) + " :D");
      final double ze = Debug.ToDouble(n,d);
      assert ze == z : 
        "\n" 
        + Double.toHexString(ze) + " :E\n"
        + Double.toHexString(z); 
      return z; } 
    catch (final Throwable t) {
      System.err.println("failed on:");
      System.err.println(description("n",n)); 
      System.err.println(description("d",d)); 
      throw t; } }

  public static final double roundingTest (final long n,
                                           final long d) {
    final double z = roundingTest(
      BigInteger.valueOf(n),
      BigInteger.valueOf(d));
    return z; } 

  public static final double roundingTest (final double x) {
    //    Debug.println();
    //    Debug.println("roundingTest(" 
    //      + Double.toHexString(x) + ")");
    //    Debug.println("signBit=" + signBit(x));
    //    Debug.println("significand=" 
    //      + Long.toHexString(fullSignificand(x)));
    //    Debug.println("significand=" 
    //      + Long.toBinaryString(fullSignificand(x)));
    //    Debug.println("significand=" 
    //      + Long.toBinaryString(SIGNIFICAND_MASK));
    //    Debug.println("unbiasedExp=" 
    //      + Doubles.unbiasedExponent(x));
    final BigInteger[] nd = toRatio(x);
    final BigInteger n = nd[0];
    final BigInteger d = nd[1];
    try {
      final double z = roundingTest(n,d);
      assert z == x : 
        "E:\n" 
        + Double.toHexString(x) + "\n"
        + Double.toHexString(z); 
      return z; } 
    catch (final Throwable t) {
      System.err.println("failed on x= " + Double.toHexString(x)); 
      throw t; } }

  //--------------------------------------------------------------

  private static final int TRYS = 16 * 1024;

  public static final void fromBigIntegersRoundingTest () {
    final Generator gn = 
      Generators.bigIntegerGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    final Generator gd = 
      Generators.positiveBigIntegerGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"));
    for (int i=0;i<TRYS/64;i++) {
      final BigInteger n = (BigInteger) gn.next();
      final BigInteger d = (BigInteger) gd.next();
      roundingTest(n,d); } }

  public static final void fromLongsRoundingTest () {
    final Generator gn = 
      Generators.longGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    final Generator gd = 
      Generators.positiveLongGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"));
    for (int i=0;i<TRYS;i++) {
      // some longs will not be exactly representable as doubles
      final long n = gn.nextLong();
      final long d = gd.nextLong();
      roundingTest(n,d); } }

  public static final void finiteDoubleRoundingTest () {
    final Generator g = 
      Doubles.finiteGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    for (int i=0;i<TRYS;i++) {
      final double x = g.nextDouble();
      roundingTest(x); } }

  public static final void normalDoubleRoundingTest () {
    final Generator g = 
      Doubles.normalGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"));
    for (int i=0;i<TRYS;i++) {
      final double x = g.nextDouble();
      roundingTest(x); } }

  public static final void subnormalDoubleRoundingTest () {
    final Generator g = 
      Doubles.subnormalGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-09.txt"));
    for (int i=0;i<TRYS;i++) {
      final double x = g.nextDouble();
      roundingTest(x); } }

  //--------------------------------------------------------------

  public static final void main (final String[] args) {
    final long t = System.nanoTime();
    Debug.DEBUG = false;
    // test numbers outside double range
    final BigInteger[] nd = 
      toRatio(Double.MAX_VALUE);
    final BigInteger a = nd[0].multiply(BigInteger.TEN);
    final BigInteger b = nd[1];
    roundingTest(a,b);
    roundingTest(a.negate(),b);
    roundingTest(b,a);
    roundingTest(b.negate(),a);
    final BigInteger a2 = a.multiply(BigInteger.TWO);
    roundingTest(a2,b);
    roundingTest(a2.negate(),b);
    roundingTest(b,a2);
    roundingTest(b.negate(),a2);
    final BigInteger a10 = a.multiply(BigInteger.TEN);
    roundingTest(a10,b);
    roundingTest(a10.negate(),b);
    roundingTest(b,a10);
    roundingTest(b.negate(),a10);
    //DEBUG=true;
    roundingTest(0x0.0000000000001p-1022);
    roundingTest(0x0.1000000000001p-1022);
    roundingTest(0x0.1000000000000p-1022);
    roundingTest(0x1.0000000000001p-1022);
    roundingTest(0x1.0000000000000p-1022);
    roundingTest(0x0.0000000000001p-1022);
    roundingTest(0x0.033878c4999b7p-1022);
    roundingTest(0x1.33878c4999b6ap-1022);
    roundingTest(-0x1.76c4ebe6d57c8p-924);
    roundingTest(0x1.76c4ebe6d57c8p-924);
    roundingTest(-0x1.76c4ebe6d57c8p924);
    roundingTest(0x1.76c4ebe6d57c8p924);
    roundingTest(1L,3L);
    fromLongsRoundingTest();
    normalDoubleRoundingTest();
    subnormalDoubleRoundingTest();
    //finiteDoubleRoundingTest(); 
    fromBigIntegersRoundingTest(); 
    System.out.printf("total secs: %8.2f\n",
      Double.valueOf((System.nanoTime()-t)*1.0e-9)); 
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
