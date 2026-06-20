package mt.interop;

import java.util.concurrent.atomic.AtomicLong;

public final class MTInteropDiagnostics {

    private static final AtomicLong methodCacheHit = new AtomicLong();
    private static final AtomicLong methodCacheMiss = new AtomicLong();

    private static final AtomicLong constructorCacheHit = new AtomicLong();
    private static final AtomicLong constructorCacheMiss = new AtomicLong();

    private static final AtomicLong negativeMethodHit = new AtomicLong();
    private static final AtomicLong negativeConstructorHit = new AtomicLong();

    private MTInteropDiagnostics() {
    }

    // ---------------------------
    // Méthodes (invocations)
    // ---------------------------

    static void methodCacheHit(boolean negative) {
        methodCacheHit.incrementAndGet();
        if (negative) negativeMethodHit.incrementAndGet();
    }

    static void methodCacheMiss() {
        methodCacheMiss.incrementAndGet();
    }

    // ---------------------------
    // Constructeurs
    // ---------------------------

    static void constructorCacheHit(boolean negative) {
        constructorCacheHit.incrementAndGet();
        if (negative) negativeConstructorHit.incrementAndGet();
    }

    static void constructorCacheMiss() {
        constructorCacheMiss.incrementAndGet();
    }

    // ---------------------------
    // Reset
    // ---------------------------

    public static void reset() {
        methodCacheHit.set(0);
        methodCacheMiss.set(0);
        constructorCacheHit.set(0);
        constructorCacheMiss.set(0);
        negativeMethodHit.set(0);
        negativeConstructorHit.set(0);
    }

    // ---------------------------
    // Dump
    // ---------------------------

    public static String dump() {
        return """
                ===== miniTalk Java Interop Cache =====
                
                Methods:
                  hits              : %d
                  misses            : %d
                  negative hits     : %d
                
                Constructors:
                  hits              : %d
                  misses            : %d
                  negative hits     : %d
                
                ======================================
                """.formatted(
                methodCacheHit.get(),
                methodCacheMiss.get(),
                negativeMethodHit.get(),
                constructorCacheHit.get(),
                constructorCacheMiss.get(),
                negativeConstructorHit.get()
        );
    }
}
