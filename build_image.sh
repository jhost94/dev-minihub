#!/usr/bin/env bash

### Variables
IMAGE_VERSION="0.1.0"
IMAGE_NAME="jhub-mini-dev"
BUILD_JAVA=0
DRY_RUN=0
BUILD_OPTIONS=""

### Functions
usage() {
    r=0
    if [ $# -gt 0 ]; then
        r=1
        echo "$1"
    fi
    cat <<EOF
Usage: $0 [options]
Options:
  -q        --quiet      Run docker build in quiet mode and prints out it's output.
  -b        --build      Run maven build before building the image.
  -d        --dry        Dry run. Doesn't execute any docker command, instead prints them.
  -h        --help       Show this message.
EOF
    exit $r
}

### Main execution
while [[ $# -gt 0 ]]; do
    case "$1" in
        -b|--build)
            BUILD_JAVA=1
            shift;;
        -q|--quiet)
            BUILD_OPTIONS="$BUILD_OPTIONS -q"
            shift;;
        -h|--help)
            usage
            ;;
        -d|--dry)
            DRY_RUN=1
            shift
            ;;
        *)
            usage "Unknown command $1"
            ;;
    esac
done

if [ $BUILD_JAVA -eq 1 ]; then
    if [[ $DRY_RUN == 1 ]]; then
        echo "mvn clean package"
    else
        mvn clean package
    fi
fi

if [[ $DRY_RUN == 1 ]]; then
    echo "docker build $BUILD_OPTIONS -t $IMAGE_NAME:$IMAGE_VERSION ."
else
    docker build $BUILD_OPTIONS -t $IMAGE_NAME:$IMAGE_VERSION .
fi
