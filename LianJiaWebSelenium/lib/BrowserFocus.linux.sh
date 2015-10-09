#!/bin/bash
found=`wmctrl -l | grep "Open Files"`
length=${#found}

if [[ $length -gt 0 ]]; then
  wmctrl -a "Open Files"
  exit 0
fi

found=`wmctrl -l | grep "File Upload"`
length=${#found}

if [[ $length -gt 0 ]];
then
  wmctrl -a "File Upload"
  exit 0
fi

wmctrl -a "$1"
