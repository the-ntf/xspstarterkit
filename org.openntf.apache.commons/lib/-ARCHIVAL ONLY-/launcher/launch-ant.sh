#!/bin/sh

# -----------------------------------------------------------------------------
#
# Sample script for launching Ant using the Launcher
#
# -----------------------------------------------------------------------------

# Resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`
if [ -r "$PRGDIR"/settings.sh ]; then
  . "$PRGDIR"/settings.sh
fi

# Execute the Launcher using the "ant" target
exec "$JAVA_HOME"/bin/java -classpath "$PRGDIR" LauncherBootstrap -verbose ant "$@"
