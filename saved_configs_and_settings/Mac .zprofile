if false; then
    echo "DEBUG: Running .zprofile"
fi

# Install path for homebrew
eval "$(/opt/homebrew/bin/brew shellenv)"

# Other configurations

# Java 17
JAVA_HOME="/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home"
JAVA_BIN="/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home/bin"
export JAVA_HOME

ANDROID_PLATFORM_TOOLS="/Users/sbutr/Library/Android/sdk/platform-tools"
ANDROID_HOME="/Users/sbutr/Library/Android/sdk/"

export ANDROID_HOME

LOCAL_SCRIPTS="/Users/sbutr/Desktop/Tools"

PATH="$LOCAL_SCRIPTS:$JAVA_BIN:$JAVA_HOME:$ANDROID_PLATFORM_TOOLS:$PATH"
export PATH
