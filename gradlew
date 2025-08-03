#\!/bin/sh

# Gradle Wrapper Script for Hello World Android App
# =================================================
# This script allows you to run Gradle without installing it manually

# Determine the Java command to use to start the JVM
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ \! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# For now, we'll create a simplified wrapper that uses the system gradle
# In a real project, this would download and use the specific Gradle version

echo "📱 Hello World Android Build Wrapper"
echo "Using system Gradle for Termux compatibility..."

# Check if gradle is available
if command -v gradle >/dev/null 2>&1; then
    gradle "$@"
else
    echo "❌ Gradle not found. Please install Gradle first."
    echo "Run: pkg install gradle"
    exit 1
fi
