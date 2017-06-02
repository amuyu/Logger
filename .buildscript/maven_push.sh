#!/bin/bash
./gradlewww clean build bintrayUpload -PbintrayUser=$0 -PbintrayKey=$1 -PdryRun=false