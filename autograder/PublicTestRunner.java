import java.lang.reflect.*;
import java.util.*;

/**
 * PublicTestRunner is a lightweight (no JUnit) local public-test harness.
 *
 * It is designed to run locally with plain javac/java.
 *
 * Usage:
 *   java PublicTestRunner <impl> <case> <op> <mode>
 *
 *   impl: RA | L
 *   case: typical | empty
 *   op  : union | intersection | difference
 *   mode: same | mixed
 *
 * Exit code 0 = PASS; nonzero = FAIL.
 *
 * Staff grading uses an instructor-controlled copy of this public runner.
 */
public class PublicTestRunner {

    private enum Impl { RA, L }
    private enum Case { TYPICAL, EMPTY }
    private enum Op { UNION, INTERSECTION, DIFFERENCE }
    private enum Mode { SAME, MIXED }

    public static void main(String[] args) {
        try {
            if (args.length == 1 && args[0].equalsIgnoreCase("null-args")) {
                runNullArgumentChecks();
                System.exit(0);
            }

            if (args.length != 4) {
                usage();
                System.exit(2);
            }

            Impl impl = parseImpl(args[0]);
            Case kase = parseCase(args[1]);
            Op op = parseOp(args[2]);
            Mode mode = parseMode(args[3]);

            runCase(impl, kase, op, mode);
            System.exit(0);
        } catch (AssertionError ae) {
            System.err.println(ae.getMessage());
            System.exit(1);
        } catch (Throwable t) {
            System.err.println("UNEXPECTED ERROR: " + t.getClass().getName() + ": " + t.getMessage());
            t.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void usage() {
        System.err.println("Usage: java PublicTestRunner <impl> <case> <op> <mode>");
        System.err.println("  impl: RA | L");
        System.err.println("  case: typical | empty");
        System.err.println("  op  : union | intersection | difference");
        System.err.println("  mode: same | mixed");
        System.err.println("Or:    java PublicTestRunner null-args");
    }

    private static Impl parseImpl(String s) {
        if (s.equalsIgnoreCase("RA")) return Impl.RA;
        if (s.equalsIgnoreCase("L")) return Impl.L;
        throw new AssertionError("Bad impl: " + s);
    }

    private static Case parseCase(String s) {
        if (s.equalsIgnoreCase("typical")) return Case.TYPICAL;
        if (s.equalsIgnoreCase("empty")) return Case.EMPTY;
        throw new AssertionError("Bad case: " + s);
    }

    private static Op parseOp(String s) {
        if (s.equalsIgnoreCase("union")) return Op.UNION;
        if (s.equalsIgnoreCase("intersection")) return Op.INTERSECTION;
        if (s.equalsIgnoreCase("difference")) return Op.DIFFERENCE;
        throw new AssertionError("Bad op: " + s);
    }

    private static Mode parseMode(String s) {
        if (s.equalsIgnoreCase("same")) return Mode.SAME;
        if (s.equalsIgnoreCase("mixed")) return Mode.MIXED;
        throw new AssertionError("Bad mode: " + s);
    }

    private static Object newBag(Impl impl) {
        try {
            if (impl == Impl.RA) {
                return Class.forName("ResizableArrayBag").getDeclaredConstructor().newInstance();
            } else {
                return Class.forName("LinkedBag").getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new AssertionError("Could not instantiate " + impl + " bag: " + e);
        }
    }

    private static void addAll(Object bag, Integer[] items) {
        for (Integer x : items) {
            invoke(bag, "add", new Class<?>[]{Object.class}, new Object[]{x});
        }
    }

    private static Object invoke(Object target, String methodName, Class<?>[] paramTypes, Object[] args) {
        try {
            Method m = null;

            // First try an exact signature (common when using Object in reflection)
            try {
                m = target.getClass().getMethod(methodName, paramTypes);
            } catch (NoSuchMethodException ignored) {}

            // Fallback: find any method with same name and param count
            if (m == null) {
                for (Method cand : target.getClass().getMethods()) {
                    if (cand.getName().equals(methodName) && cand.getParameterCount() == args.length) {
                        m = cand;
                        break;
                    }
                }
            }

            if (m == null) {
                throw new NoSuchMethodException(target.getClass().getName() + "." + methodName + "(...)");
            }

            return m.invoke(target, args);
        } catch (InvocationTargetException ite) {
            // Re-throw the underlying exception so tests can reason about it.
            Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            if (cause instanceof Error) throw (Error) cause;
            throw new RuntimeException(cause);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer[] toIntegerArray(Object bag) {
        Object res = invoke(bag, "toArray", new Class<?>[]{}, new Object[]{});
        if (!(res instanceof Object[])) {
            throw new AssertionError("toArray() did not return an Object[]");
        }
        Object[] arr = (Object[]) res;
        Integer[] out = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = (Integer) arr[i];
        }
        return out;
    }

    private static Map<Integer, Integer> multiset(Integer[] arr) {
        Map<Integer, Integer> m = new HashMap<>();
        for (Integer x : arr) {
            m.put(x, m.getOrDefault(x, 0) + 1);
        }
        return m;
    }

    private static void assertMultisetEquals(String label, Integer[] expected, Object actualBag) {
        Map<Integer, Integer> exp = multiset(expected);
        Map<Integer, Integer> act = multiset(toIntegerArray(actualBag));
        if (!exp.equals(act)) {
            throw new AssertionError(label + " FAILED\nExpected multiset: " + exp + "\nActual multiset  : " + act);
        }
    }

    private static void assertBagUnchanged(String label, Object bag, Integer[] original) {
        Map<Integer, Integer> exp = multiset(original);
        Map<Integer, Integer> act = multiset(toIntegerArray(bag));
        if (!exp.equals(act)) {
            throw new AssertionError(label + " MUTATED INPUT BAG\nExpected multiset: " + exp + "\nActual multiset  : " + act);
        }
    }

    private static void runCase(Impl impl, Case kase, Op op, Mode mode) {
        Integer[] bag0 = (kase == Case.TYPICAL)
                ? new Integer[]{3, 1, 2, 3}
                : new Integer[]{};
        Integer[] bag1 = (kase == Case.TYPICAL)
                ? new Integer[]{3, 3, 1, 4, 3}
                : new Integer[]{3, 3, 1, 4, 3};

        // Expected outputs from your provided BagT.java
        Integer[] exp_0U0 = (kase == Case.TYPICAL)
                ? new Integer[]{1, 1, 2, 2, 3, 3, 3, 3}
                : new Integer[]{};
        Integer[] exp_0U1 = (kase == Case.TYPICAL)
                ? new Integer[]{1, 1, 2, 3, 3, 3, 3, 3, 4}
                : new Integer[]{1, 3, 3, 3, 4};
        Integer[] exp_1U1 = (kase == Case.TYPICAL)
                ? new Integer[]{1, 1, 3, 3, 3, 3, 3, 3, 4, 4}
                : new Integer[]{1, 1, 3, 3, 3, 3, 3, 3, 4, 4};

        Integer[] exp_0n1 = (kase == Case.TYPICAL)
                ? new Integer[]{1, 3, 3}
                : new Integer[]{};

        Integer[] exp_0m1 = (kase == Case.TYPICAL)
                ? new Integer[]{2}
                : new Integer[]{};
        Integer[] exp_1m0 = (kase == Case.TYPICAL)
                ? new Integer[]{3, 4}
                : new Integer[]{1, 3, 3, 3, 4};
        Integer[] exp_empty = new Integer[]{};

        Impl otherImpl = (mode == Mode.SAME) ? impl : (impl == Impl.RA ? Impl.L : Impl.RA);

        Object a = newBag(impl);      // bag0 in "this" impl
        Object b = newBag(impl);      // bag1 in "this" impl
        addAll(a, bag0);
        addAll(b, bag1);

        Object otherA = (mode == Mode.SAME) ? a : newBag(otherImpl);
        Object otherB = (mode == Mode.SAME) ? b : newBag(otherImpl);
        if (mode == Mode.MIXED) {
            addAll(otherA, bag0);
            addAll(otherB, bag1);
        }

        // snapshots for immutability checks
        Integer[] a0 = bag0.clone();
        Integer[] b0 = bag1.clone();
        Integer[] otherA0 = bag0.clone();
        Integer[] otherB0 = bag1.clone();

        String labelPrefix = impl + " / " + kase + " / " + op + " / " + mode;

        switch (op) {
            case UNION:
                Object r1 = invoke(a, "union", new Class<?>[]{Object.class}, new Object[]{otherA});
                Object r2 = invoke(a, "union", new Class<?>[]{Object.class}, new Object[]{otherB});
                Object r3 = invoke(b, "union", new Class<?>[]{Object.class}, new Object[]{otherB});
                assertNewResult(labelPrefix + " (0 U 0)", r1, a, otherA);
                assertNewResult(labelPrefix + " (0 U 1)", r2, a, otherB);
                assertNewResult(labelPrefix + " (1 U 1)", r3, b, otherB);
                assertMultisetEquals(labelPrefix + " (0 U 0)", exp_0U0, r1);
                assertMultisetEquals(labelPrefix + " (0 U 1)", exp_0U1, r2);
                assertMultisetEquals(labelPrefix + " (1 U 1)", exp_1U1, r3);

                assertBagUnchanged(labelPrefix + " (bag0)", a, a0);
                assertBagUnchanged(labelPrefix + " (bag1)", b, b0);
                assertBagUnchanged(labelPrefix + " (other bag0)", otherA, otherA0);
                assertBagUnchanged(labelPrefix + " (other bag1)", otherB, otherB0);
                break;

            case INTERSECTION:
                Object r4 = invoke(a, "intersection", new Class<?>[]{Object.class}, new Object[]{otherB});
                assertNewResult(labelPrefix + " (0 ∩ 1)", r4, a, otherB);
                assertMultisetEquals(labelPrefix + " (0 ∩ 1)", exp_0n1, r4);

                // optional sanity: X ∩ X == X (multiset-wise)
                Object r5 = invoke(a, "intersection", new Class<?>[]{Object.class}, new Object[]{otherA});
                assertNewResult(labelPrefix + " (0 ∩ 0)", r5, a, otherA);
                assertMultisetEquals(labelPrefix + " (0 ∩ 0)", bag0, r5);

                assertBagUnchanged(labelPrefix + " (bag0)", a, a0);
                assertBagUnchanged(labelPrefix + " (bag1)", b, b0);
                assertBagUnchanged(labelPrefix + " (other bag0)", otherA, otherA0);
                assertBagUnchanged(labelPrefix + " (other bag1)", otherB, otherB0);
                break;

            case DIFFERENCE:
                Object r6 = invoke(a, "difference", new Class<?>[]{Object.class}, new Object[]{otherB}); // 0-1
                Object r7 = invoke(b, "difference", new Class<?>[]{Object.class}, new Object[]{otherA}); // 1-0
                Object r8 = invoke(a, "difference", new Class<?>[]{Object.class}, new Object[]{otherA}); // 0-0
                Object r9 = invoke(b, "difference", new Class<?>[]{Object.class}, new Object[]{otherB}); // 1-1
                assertNewResult(labelPrefix + " (0 - 1)", r6, a, otherB);
                assertNewResult(labelPrefix + " (1 - 0)", r7, b, otherA);
                assertNewResult(labelPrefix + " (0 - 0)", r8, a, otherA);
                assertNewResult(labelPrefix + " (1 - 1)", r9, b, otherB);
                assertMultisetEquals(labelPrefix + " (0 - 1)", exp_0m1, r6);
                assertMultisetEquals(labelPrefix + " (1 - 0)", exp_1m0, r7);
                assertMultisetEquals(labelPrefix + " (0 - 0)", exp_empty, r8);
                assertMultisetEquals(labelPrefix + " (1 - 1)", exp_empty, r9);

                assertBagUnchanged(labelPrefix + " (bag0)", a, a0);
                assertBagUnchanged(labelPrefix + " (bag1)", b, b0);
                assertBagUnchanged(labelPrefix + " (other bag0)", otherA, otherA0);
                assertBagUnchanged(labelPrefix + " (other bag1)", otherB, otherB0);
                break;
        }

        System.out.println("PASS: " + labelPrefix);
    }

    private static void assertNewResult(String label, Object result, Object... inputs) {
        for (Object input : inputs) {
            if (result == input) {
                throw new AssertionError(label + " FAILED: result bag aliases an input bag");
            }
        }
    }

    private static void runNullArgumentChecks() {
        Object bag = newBag(Impl.RA);
        assertThrowsIAE("RA add(null)", () -> invoke(bag, "add", new Class<?>[]{Object.class}, new Object[]{null}));
        assertThrowsIAE("RA union(null)", () -> invoke(bag, "union", new Class<?>[]{Object.class}, new Object[]{null}));
        assertThrowsIAE("RA intersection(null)", () -> invoke(bag, "intersection", new Class<?>[]{Object.class}, new Object[]{null}));
        assertThrowsIAE("RA difference(null)", () -> invoke(bag, "difference", new Class<?>[]{Object.class}, new Object[]{null}));

        Object bag2 = newBag(Impl.L);
        assertThrowsIAE("L add(null)", () -> invoke(bag2, "add", new Class<?>[]{Object.class}, new Object[]{null}));
        assertThrowsIAE("L union(null)", () -> invoke(bag2, "union", new Class<?>[]{Object.class}, new Object[]{null}));
        assertThrowsIAE("L intersection(null)", () -> invoke(bag2, "intersection", new Class<?>[]{Object.class}, new Object[]{null}));
        assertThrowsIAE("L difference(null)", () -> invoke(bag2, "difference", new Class<?>[]{Object.class}, new Object[]{null}));

        System.out.println("PASS: null argument checks");
    }



    private interface ThrowingRunnable {
        void run();
    }

    private static void assertThrowsIAE(String label, ThrowingRunnable r) {
        try {
            r.run();
            throw new AssertionError(label + " FAILED: expected IllegalArgumentException, but no exception was thrown");
        } catch (IllegalArgumentException iae) {
            String msg = iae.getMessage();
            if (msg == null || msg.trim().isEmpty()) {
                throw new AssertionError(label + " FAILED: IllegalArgumentException message must be non-empty");
            }
        }
    }
}