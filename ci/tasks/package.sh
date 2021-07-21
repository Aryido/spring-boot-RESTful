# ci/tasks/package.sh

#!/bin/bash

set -e -u -x

cd source-code/
./mvn package
