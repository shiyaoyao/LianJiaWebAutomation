#!/bin/bash
result=`/usr/bin/osascript <<ASEND

set browserName to "$1"
set dialogObj to null
set pastebox to null

-- Make the browser the active windo

tell application browserName
	activate
	delay 0.5
end tell

tell application "System Events"
	-- set UI elements enabled to true
	
	tell process browserName
		set visible to true
		
		-- Look for the File upload dialog.
		-- Firefox uses a dialog window, Chrome and Safai use a dialog sheet.
		set winCount to count windows
		set sheetCount to count sheets of front window
		
		--If there's only one window and no sheet then the File Browser 
		--object isn't visible. Wait 2 sec and try again.
		if winCount is equal to 1 and sheetCount is equal to 0 then
			delay 2
			set winCount to count windows
			set sheetCount to count sheets of front window
		end if
		
		
		--If we're using Firefox and we have more than one window
		--find the dialog window
		if browserName contains "Firefox" and winCount is greater than 1 then
			set dialogObj to first window
			if description of dialogObj is not "dialog" then
				set dialogObj to second window
			end if
		else
			-- If we're not using Firefox then find for the dialog sheet
			if sheetCount is greater than 0 then
				set dialogObj to sheet 1 of front window
			end if
		end if
		
	end tell
	
	-- Return immediately if dialogObj isn't found
	if dialogObj is null then
		return "FAIL - dialogObj is null"
	end if
	
	get properties of dialogObj
	set dialogDesc to description of dialogObj
	if dialogDesc does not contain "dialog" and dialogDesc does not contain "open" then
		return "FAIL - dialogObj is not a dialog type (" & dialogDesc & ")"
	end if
	
	-- Bring up the text entry box, Cmd-Shift-g and clear contents
	keystroke "g" using {command down, shift down}
	delay 1
	key code 0 using command down
	key code 51
	delay 0.5
	--Paste file name
	keystroke "v" using {command down}
	--Press return twice
	delay 0.5
	set textField to value of first text field of first sheet of dialogObj
	key code 36
	delay 0.5
	key code 36
	delay 0.5
	
	--If the dialog still exists, escape out
	if (exists dialogObj) then
		key code 53
		key code 53
		return "FAIL - Dialog still open. Escaped out. (" & textField & ")"
	end if
	
	return not (exists dialogObj) 
	
end tell
ASEND`
echo $result > ${PWD}/lib/result.txt
