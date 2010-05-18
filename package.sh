\#!/bin/sh

TARGET="/tmp/netention"

ant jar

rm -Rf $TARGET
mkdir $TARGET

cp *.sh $TARGET
cp *.bat $TARGET
cp -R dist/* $TARGET
cp -R media $TARGET

mkdir $TARGET/lib/jogl
cp -R lib/jogl/*.so $TARGET/lib/jogl
cp -R lib/jogl/*.dll $TARGET/lib/jogl

cd /tmp
zip netention.`date +%s`.zip -r -9 netention

