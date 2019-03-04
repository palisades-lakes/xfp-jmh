package xfp.jmh;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import com.google.common.collect.ImmutableMap;

import xfp.java.algebra.Set;
import xfp.java.algebra.Sets;
import xfp.java.algebra.Structure;
import xfp.java.algebra.TwoSetsOneOperation;
import xfp.java.linear.BigDecimalsN;
import xfp.java.linear.BigFractionsN;
import xfp.java.linear.Dn;
import xfp.java.linear.ERationalsN;
import xfp.java.linear.Fn;
import xfp.java.linear.Qn;
import xfp.java.linear.RatiosN;
import xfp.java.prng.PRNG;
import xfp.java.prng.Seeds;

// java --illegal-access=warn -jar target/benchmarks.jar

@SuppressWarnings("unchecked")
public class Bench {

  //--------------------------------------------------------------

  private static final boolean testMembership (final Set set,
                                               final int ntrys) {
    final Supplier g = 
      set.generator( 
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b(
            Seeds.seed("seeds/Well44497b-2019-01-05.txt"))));
    for (int i=0; i<ntrys; i++) {
      final Object x = g.get();
      if (! set.contains(x)) { return false; } } 
    return true; }

  private static final boolean testEquivalence (final Set set,
                                                final int ntrys) {
    final Supplier g = 
      set.generator( 
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b(
            Seeds.seed("seeds/Well44497b-2019-01-07.txt"))));
    for (int i=0; i<ntrys; i++) {
      if (! Sets.isReflexive(set,g)) { return false; }
      if (! Sets.isSymmetric(set,g)) { return false; } }
    return true; }

  private static final boolean setTests (final Set set,
                                         final int ntrys) {
    return
      testMembership(set,ntrys)
      &&
      testEquivalence(set,ntrys); }

  //--------------------------------------------------------------

  private static final boolean 
  structureTests (final Structure s,
                  final int ntrys) {
    final Map<Set,Supplier> generators = 
      s.generators(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b(
            Seeds.seed("seeds/Well44497b-2019-01-09.txt"))));
    for(final Predicate law : s.laws()) {
      for (int i=0; i<ntrys; i++) {
        if (! law.test(generators)) { return false; } } }
    return true; }

  //--------------------------------------------------------------

  private static final int TRYS = 1;
  private static final int[] DIMENSIONS = 
    new int[] { 16 * 1024, };

  public static final boolean 
  spaceTest (final TwoSetsOneOperation space) {
    if (! setTests(space,TRYS)) { return false; }
    if (! structureTests(space,TRYS)) { return false; } 
    return true; }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean bdnTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(BigDecimalsN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean erTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(ERationalsN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean bfnTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(BigFractionsN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean rationTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(RatiosN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean qnTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(Qn.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean dnTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(Dn.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean fnTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(Fn.space(n))) { return false; } }
    return true; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
