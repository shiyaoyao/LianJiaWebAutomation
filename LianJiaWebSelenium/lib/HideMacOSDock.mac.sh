#!/bin/sh
/usr/bin/osascript <<EOF

set _hide to "$1"

tell application "System Events"
	tell dock preferences to set autohide to _hide
end tell
