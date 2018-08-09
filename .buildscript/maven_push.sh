#!/bin/bash
./gradlew clean build generatePomFileForReleasePublication bintrayUpload -PbintrayUser=$1 -PbintrayKey=$2 -PdryRun=false