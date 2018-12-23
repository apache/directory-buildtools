#!/bin/bash
set -e

echo "user:x:$(id -u):$(id -g)::/home/user:/bin/bash" >> /etc/passwd
export HOME=/home/user

exec "$@"

