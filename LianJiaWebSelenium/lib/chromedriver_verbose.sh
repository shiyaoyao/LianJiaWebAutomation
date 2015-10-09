#!/bin/bash
pth=$(dirname $0)
echo "$pth"
$pth/_chromedriver_mac --verbose $*
