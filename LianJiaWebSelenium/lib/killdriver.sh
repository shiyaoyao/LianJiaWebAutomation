PID=`ps -eo pid,comm | grep -i "$1" | awk {'print $1;exit;'}`
echo "START PID: $PID"
COUNT=0
while [[ "[$PID]" != "[]" && COUNT -lt 15 ]]; do
	kill -9 $PID
	(( COUNT++ ))
	sleep 1
	PID=`ps -eo pid,comm | grep -i "$1" | awk {'print $1;exit;'}`
	echo "New pid: $PID"
done
