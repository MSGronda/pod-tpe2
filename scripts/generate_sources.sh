#!/bin/bash
BASE="$(pwd)"

cd ./client/target && tar -xf tpe2-g11-client-1.0-SNAPSHOT-bin.tar.gz
chmod u+x ./tpe2-g11-client-1.0-SNAPSHOT/*.sh

cd "$BASE" || exit

cd ./server/target && tar -xf tpe2-g11-server-1.0-SNAPSHOT-bin.tar.gz
chmod u+x ./tpe2-g11-server-1.0-SNAPSHOT/*.sh

