#!/bin/bash

# Fail if somthing is wrong
set -e

help() {
    echo "Usage: $0 [--server] [--webui]"
    echo "Options:"
    echo "  --server   Run the backend server"
    echo "  --webui    Run the web UI"
    echo "  -h, --help Show this help message"
}

if [ $# -eq 0 ]; then
    help
    exit 1
fi

SCRIPT_RELEVANT_PATH=$( dirname $BASH_SOURCE[0] )

RUN_BACKEND=false
RUN_WEBUI=false

while [ "${1:-}" != "" ]; do
  case "$1" in
    --server)
        RUN_BACKEND=true
        ;;
    --webui)
        RUN_WEBUI=true
        ;;
    -h | --help)
        help
        exit 1
        ;;    
  esac
  shift
done

if [ "$RUN_BACKEND" = true ]; then
    # Run the backend agent
    pushd ${SCRIPT_RELEVANT_PATH}/agent
        python main.py
    popd
fi

if [ "$RUN_WEBUI" = true ]; then
    # Run the web UI
    pushd ${SCRIPT_RELEVANT_PATH}/web/WebUI
        ./gradlew webApp:wasmJsBrowserDevelopmentRun -q --console=plain
    popd
fi