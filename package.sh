#!/bin/sh

TARGET="/tmp/netention"

ant jar

rm -Rf $TARGET
mkdir $TARGET

cp -R dist/* $TARGET
cp -R media $TARGET

cd /tmp
zip netention.`date +%s`.zip -r -9 netention

