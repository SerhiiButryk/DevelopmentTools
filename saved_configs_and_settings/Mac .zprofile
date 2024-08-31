echo "DEBUG> Running .zprofile"

# Install path for homebrew
eval "$(/opt/homebrew/bin/brew shellenv)"

# Other configurations

ANDROID_PLATFORM_TOOLS="/Users/sbutr/Library/Android/sdk/platform-tools"
ANDROID_HOME="/Users/sbutr/Library/Android/sdk/"

export ANDROID_HOME

# JAVA 11
# JAVA_HOME="/Library/Java/JavaVirtualMachines/zulu-11.jdk/Contents/Home" 
# JAVA 17
#JAVA_HOME="/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home" 
# JAVA 17
# JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" 
#export JAVA_HOME

#DOXYGEN_HOME="/Applications/Doxygen.app/Contents/Resources"
#LOCAL_SCRIPTS="/Users/sbutr/Desktop/Tools"

# Setting PATH for Python 3.12
# The original version is saved in .bash_profile.pysave
#PYTHON="/Library/Frameworks/Python.framework/Versions/3.12/bin"

PATH="$ANDROID_PLATFORM_TOOLS:$PATH"
export PATH
