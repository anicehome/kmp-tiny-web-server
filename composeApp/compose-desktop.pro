

# Project classes
-keep class io.github.anicehome.webserver.database.** {*;}
-keepclassmembers enum * { *; }

-dontwarn okio.AsyncTimeout$Watchdog


# SQLite
-keep class androidx.sqlite.** { *; }

### Coroutines ###

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
# The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
-dontwarn java.lang.ClassValue

-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean