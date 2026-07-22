#!/usr/bin/env bash

### Variables
IMAGE_VERSION="0.1.0"
IMAGE_NAME="jhub-mini-dev"
CONTAINER_NAME="jhub_minihub_dev"
BUILD_SCRIPT="$(pwd)/build_image.sh"
NETWORK_JHUB_MAIN="jhub_main"
NETWORK_JHUB_MINI="jhub_mini_internal"
NETWORK_JHUB_MINI_DEV="jhub_mini_dev"
HOST_PORT=3001
CONTAINER_PORT=8080
DO_BUILD=0
DRY_RUN=0

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
  -b        --build      Build the image before running.
  -d        --dry        Dry run. Doesn't execute any docker command, instead prints them.
  -h        --help       Show this message.
EOF
    exit $r
}

create_network() {
    name=$1
    internal=0
    if [[ -z $name ]]; then
        echo "Error: no name for network when creating network"
        exit 1
    fi
    if [[ -n $2 && $2 == 1 ]]; then
        internal=1
    fi

    if [[ $internal == 1 ]]; then
        if [[ $DRY_RUN == 1 ]]; then
            echo "docker network create --internal $name"
        else
            docker network create --internal $name
        fi
    else
        if [[ $DRY_RUN == 1 ]]; then
            echo "docker network create $name"
        else
            docker network create $name
        fi
    fi
}

### Main execution
while [[ $# -gt 0 ]]; do
    case "$1" in
        -b|--build)
            DO_BUILD=1; shift;;
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
    $BUILD_SCRIPT
fi

jhub_net_main=$(docker network ls -f "name=$NETWORK_JHUB_MAIN" -q)
jhub_net_mini=$(docker network ls -f "name=$NETWORK_JHUB_MINI" -q)
jhub_net_dev=$(docker network ls -f "name=$NETWORK_JHUB_MINI_DEV" -q)



if [[ -z $jhub_net_main ]]; then create_network $NETWORK_JHUB_MAIN; fi
if [[ -z $jhub_net_mini ]]; then create_network $NETWORK_JHUB_MINI 1; fi
if [[ -z $jhub_net_dev ]]; then create_network $NETWORK_JHUB_MINI_DEV 1; fi


if [[ $DRY_RUN == 1 ]]; then
    echo "docker run --rm -d --name \"$CONTAINER_NAME\" $IMAGE_NAME:$IMAGE_VERSION"
else
    docker run --rm -d \
        --name "$CONTAINER_NAME" \
        --network $NETWORK_JHUB_MAIN \
        --network $NETWORK_JHUB_MINI \
        --network $NETWORK_JHUB_MINI_DEV \
        -p $HOST_PORT:$CONTAINER_PORT \
        -e APPLICATION_DOCUMENTATION_SERVER_URL=http://localhost:$HOST_PORT \
        $IMAGE_NAME:$IMAGE_VERSION
fi
