#!/bin/bash
set -e

export DISPLAY=:99
Xvfb :99 -screen 0 1024x768x16 &

echo "hnelson:x:$(id -u):$(id -g)::/home/studio:/bin/bash" >> /etc/passwd

exec "$@"

