锘�#!/bin/sh

lib=$PWD/lib

java -Xmx1024m -cp lianjia.selenium.auto.jar:common.selenium.jar:libs.jar com.lianjia.test.lianjiaweb.suite.SuiteLauncher com.lianjia.test.lianjiaweb.users.ResetMailFile

cat logs/current/results.txt
echo ""
echo ""
