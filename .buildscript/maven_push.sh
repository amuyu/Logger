#!/bin/bash
./gradlew clean build bintrayUpload -PbintrayUser=$0 -PbintrayKey=$1 -PdryRun=false