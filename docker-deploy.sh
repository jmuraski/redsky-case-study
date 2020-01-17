#!/bin/sh
set -e
export APP_NAME=case-study

RED='\033[0;31m'
NC='\033[0m' # No Color

PWD=$(pwd)

if  [ "$1" == "stop" ]
then
  echo "Stopping and removing all apps from compose file"
  docker-compose -f docker/docker-compose.yml down
  exit 0
fi

rm -rf $PWD/logs
mkdir -p $PWD/logs

./gradlew  clean build

docker-compose -f docker/docker-compose.yml build $APP_NAME

if [ "$1" == "restart" ]
then
  echo "Stoping $APP_NAME"
  docker-compose -f docker/docker-compose.yml kill $APP_NAME
  echo "Removing $APP_NAME"
  docker-compose -f docker/docker-compose.yml rm -f $APP_NAME
  echo "Launching $APP_NAME in detached mode and connecting to logs"
  docker-compose -f docker/docker-compose.yml up -d $APP_NAME
  docker-compose -f docker/docker-compose.yml logs -f $APP_NAME
else
  echo "Stopping and removing all apps from compose file"
  docker-compose -f docker/docker-compose.yml down
fi

if [ "$1" == "all" ]
then
  echo "Launching containers attached to all"
  docker-compose -f docker/docker-compose.yml up
elif [ "$1" == "app" ]
then
  echo "Launching containers detached, connecting to app logs"
  docker-compose -f docker/docker-compose.yml up -d
  docker-compose -f docker/docker-compose.yml logs -f $APP_NAME
elif [ "$1" == "detach" ] || [ "$1" == "none" ]
then
  echo "Launching containers detached, connecting to app logs"
  docker-compose -f docker/docker-compose.yml up -d
elif [ "$1" != "restart" ]
then
  echo "Launching containers detached"
  docker-compose -f docker/docker-compose.yml up -d
fi

exit 0

