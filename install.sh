#! /bin/bash
apt-get update
xargs -a packages.txt apt-get install -y

# fix perl warnings
locale-gen en_US.UTF-8

# F#
curl -sSL https://dot.net/v1/dotnet-install.sh | bash /dev/stdin --channel Current

# Kotlin
curl -sSL https://github.com/JetBrains/kotlin/releases/download/v1.6.10/kotlin-compiler-1.6.10.zip -O
unzip kotlin-compiler-1.6.10.zip
