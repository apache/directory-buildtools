#!/bin/bash
set -e

export DISPLAY=:99
Xvfb :99 -screen 0 1024x768x16 &

cd $HOME/src
exec "$@"

