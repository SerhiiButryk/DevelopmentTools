#!/bin/bash

# Tested on Ubuntu 22.04

# Edit to change product to reset trial period
list_of_products=(
# "IntelliJIdea"
#     "WebStorm"
#     "DataGrip"
#     "PhpStorm"
    "CLion"
    # "PyCharm"
    # "GoLand"
    # "RubyMine"
)

for product in list_of_products; do
    echo "[+] Resetting trial period for $product"

    echo "[+] Removing Evaluation Key..."
    rm -rf ~/.config/$product*/eval 2> /dev/null

    # Above path not working on latest version, Fixed below
    rm -rf ~/.config/JetBrains/$product*/eval 2> /dev/null

    echo "[+] Removing all evlsprt properties in options.xml..."
    sed -i 's/evlsprt//' ~/.config/$product*/options/other.xml 2> /dev/null

    # Above path not working on latest version, Fixed below
    sed -i 's/evlsprt//' ~/.config/JetBrains/$product*/options/other.xml 2> /dev/null

    echo
done

echo "Removing userPrefs files..."
rm -rf ~/.java/.userPrefs 2> /dev/null