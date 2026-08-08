#!/usr/bin/env bash

### Variables
DOCKER_USER="jhost94"
IMAGE_VERSION="0.1.0"
IMAGE_NAME="jhub-mini-dev"
BUILD_SCRIPT="$(pwd)/build_image.sh"
DO_BUILD=0
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
            DO_BUILD=1
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

if [ $DO_BUILD -eq 1 ]; then
    if [[ $DRY_RUN == 1 ]]; then
        $BUILD_SCRIPT -n $DOCKER_USER/$IMAGE_NAME -v $IMAGE_VERSION -d
    else
        $BUILD_SCRIPT -n $DOCKER_USER/$IMAGE_NAME -v $IMAGE_VERSION
    fi
fi

if [[ $DRY_RUN == 1 ]]; then
    echo "docker push $DOCKER_USER/$IMAGE_NAME:$IMAGE_VERSION"
else
    docker push $DOCKER_USER/$IMAGE_NAME:$IMAGE_VERSION
fi
