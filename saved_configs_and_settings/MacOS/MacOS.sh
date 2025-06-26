#!/usr/bin/env zsh

help() {
    echo "Help:"
    echo "Handy script to configure MacOS machine"
    echo "-i|--installapps      - Install apps using Homebrew"
    echo "-d|--installdotfiles  - Install dot files"
    echo "-a|-all               - Do all tasks"
    echo "-h|-help              - This help"
}

PYTHON_DOWNLOAD="https://www.python.org/ftp/python/3.13.5/python-3.13.5-macos11.pkg"
ANDROID_STUDIO_DOWNLOAD="https://developer.android.com/studio"

JAVA_BIN_PATH="/usr/local/opt/openjdk@21/bin"
JAVA_HOME_PATH="/usr/local/opt/openjdk@21"
VISUAL_CODE_PATH="/Applications/Visual Studio Code.app/Contents/Resources/app/bin"

INSTALL_DOT_FILES=0
INSTALL_APPS=0

# Define an array of packages to install using Homebrew.
packages=(
    "git"
    "midnight-commander"
    "openjdk@21"
    "zsh-syntax-highlighting"
)

# Define an array of packages to install with --cask option
usecask=(
    "iterm2"
    "google-chrome"
    "visual-studio-code"
    "postman"
)

dotfiles=(
    ".zprofile"
    ".zshrc"
)

if [[ $# -eq 0 ]]; then
  echo "No arguments provided."
  help
  exit 1
fi

case "$OSTYPE" in
  darwin*) ;;
  *) {
    echo "Cannot run on this OS: $OSTYPE. Stopped."
    exit 1
  } ;;
esac

while [[ "$#" -gt 0 ]]; do
  case "$1" in
    -i|--installapps)
      INSTALL_APPS=1
      shift
      ;;
    -d|--installdotfiles)
      INSTALL_DOT_FILES=1
      shift
      ;;
    -h|-help)
      help
      shift
      break
      ;;
    -a|-all)
      INSTALL_DOT_FILES=1
      INSTALL_APPS=1
      shift
      break
      ;;  
    *)
      echo "Invalid option: $1" >&2
      help
      exit 1
      ;;
  esac
done

install_apps() {
    
    for package in "${packages[@]}"; do
        if brew list --formula | grep -q "^$package\$"; then
            echo "$package is already installed. Skipping..."
        else
            echo "Installing $package..."
            
            brew install "$package"
        fi
    done

    for usecask in "${usecask[@]}"; do
        if brew list --formula | grep -q "^$usecask\$"; then
            echo "$usecask is already installed. Skipping..."
        else    
            echo "Installing $usecask using cask option..."

            brew install --cask "$usecask"
        fi
    done

    echo "Installing Oh my ZSH"

    sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"

    echo "Now, install python3"

    open $PYTHON_DOWNLOAD

    read -k "?Enter any key to continue..."

    echo "Now, install Android Studio"
    
    open $ANDROID_STUDIO_DOWNLOAD

    read -k "?Enter any key to continue..."

}

install_dotfiles() {

    for file in "${dotfiles[@]}"; do

        cp -vf $file ~/

    done
}


if [ "$INSTALL_APPS" -eq 1 ]; then

    # Install Homebrew if it isn't already installed
    if ! command -v brew &>/dev/null; then
        echo "Homebrew not installed. Installing Homebrew."
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    else
        echo "Homebrew is already installed."
    fi

    # Verify brew is now accessible
    if ! command -v brew &>/dev/null; then
        echo "Failed to configure Homebrew in PATH. Please add Homebrew to your PATH manually."
        exit 1
    fi

    # Update Homebrew and Upgrade any already-installed formulae
    brew update
    brew upgrade
    brew upgrade --cask
    brew cleanup

    echo "Install software using Homebrew:"

    for package in "${packages[@]}"; do
        echo "$package"
    done

    while true; do
        read "?Prompt: Do you want to proceed? (y/n) " yn
        case $yn in
            [Yy]* ) echo "Proceeding..."; install_apps break;;
            [Nn]* ) echo "Skipping..."; break;;
            * ) echo "Please answer yes or no.";;
        esac
    done

    # Update Homebrew and Upgrade any already-installed formulae
    brew update
    brew upgrade
    brew upgrade --cask
    brew cleanup

fi

if [ "$INSTALL_DOT_FILES" -eq 1 ]; then

    install_dotfiles

    echo "Setting env pathes for"
    echo "Java 21"
    echo "Visual Code"
    echo "Android"

    read -k "?Enter eny key to continue..."

    sed -i '' "s|# Java|# Java 21|g" ~/.zprofile
    sed -i '' "s|JAVA_HOME=\"\"|JAVA_HOME=\"$JAVA_HOME_PATH\"|g" ~/.zprofile
    sed -i '' "s|JAVA_BIN=\"\"|JAVA_BIN=\"$JAVA_BIN_PATH\"|g" ~/.zprofile

    sed -i '' "s|VISUAL_CODE=\"\"|VISUAL_CODE=\"$VISUAL_CODE_PATH\"|g" ~/.zprofile

    pushd ~/Library/Android/sdk
        new_path="$(pwd)"
        sed -i '' "s|ANDROID_HOME=\"\"|ANDROID_HOME=\"$new_path\"|g" ~/.zprofile
    popd

    pushd ~/Library/Android/sdk/platform-tools
        new_path="$(pwd)"
        sed -i '' "s|ANDROID_PLATFORM_TOOLS=\"\"|ANDROID_PLATFORM_TOOLS=\"$new_path\"|g" ~/.zprofile
    popd

    echo "DONE"

fi

echo "ALL DONE !!!"



