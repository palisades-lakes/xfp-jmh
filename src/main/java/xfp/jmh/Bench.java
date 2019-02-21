package xfp.jmh;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.Benchmark;

import xfp.java.algebra.OneSetOneOperation;
import xfp.java.algebra.OneSetTwoOperations;
import xfp.java.algebra.Set;
import xfp.java.algebra.TwoSetsTwoOperations;
import xfp.java.prng.PRNG;
import xfp.java.prng.Seeds;

// java --illegal-access=warn -jar target/benchmarks.jar

@SuppressWarnings("unchecked")
public class Bench {

  //--------------------------------------------------------------

  private static final int TRYS = 1023;

  private static final boolean 
  twoSetsTwoOperationsTests (final TwoSetsTwoOperations space) {

    final Map<Set,List> lawLists = space.linearSpaceLaws();

    final OneSetOneOperation elements = 
      (OneSetOneOperation) space.elements();
    final Supplier vg = 
      space.elements().generator( 
        PRNG.well44497b(
          Seeds.seed("seeds/Well44497b-2019-01-09.txt")));
    for(final Object law : lawLists.get(elements)) {
      for (int i=0; i<TRYS; i++) {
        if (! ((Predicate) law).test(vg)) {
          throw new RuntimeException(law + " failed."); } } } 

    final OneSetTwoOperations scalars = 
      (OneSetTwoOperations) space.scalars();
    final Supplier sg = 
      space.scalars().generator( 
        PRNG.well44497b(
          Seeds.seed("seeds/Well44497b-2019-01-11.txt")));
    for(final Object law : lawLists.get(scalars)) {
      for (int i=0; i<TRYS; i++) {
        if (! ((Predicate) law).test(sg)) {
          throw new RuntimeException(law + " failed."); } } } 

    for(final Object law : lawLists.get(space)) {
      for (int i=0; i<TRYS; i++) {
        if (! ((BiPredicate) law).test(vg,sg))  {
          throw new RuntimeException(law + " failed."); } } } 

    return true; }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Benchmark
  public final boolean qn () {
    for (final int n : new int[] { 1, 3, 13, 127, 1023}) {
      System.out.println(n);
      final TwoSetsTwoOperations qn = TwoSetsTwoOperations.getQn(n);
      if (! twoSetsTwoOperationsTests(qn)) { return false; } } 
    return true; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
