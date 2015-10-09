
@ECHO OFF
SETLOCAL

set found=NA
set IE=IEDriverServer.exe
set CH=chromedriver.exe
set driver=NA;

for /f "tokens=1" %%a in ('tasklist /FI "IMAGENAME eq %IE%" ^| findstr "^%IE%"') do  set found=%%a
REM echo IE found: %found%
if /I "%found%"=="%IE%" (
set driver=%IE%
REM echo Will kill %driver%
CALL :killDriver
)
for /f "tokens=1" %%a in ('tasklist /FI "IMAGENAME eq %CH%" ^| findstr "^%CH%"') do  set found=%%a
REM echo Chrome found: %found%
if /I "%found%"=="%CH%" (
set driver=%CH%
REM echo Will kill %driver%
CALL :killDriver
)

exit /b 0

:killDriver
REM echo Killing %driver%
taskkill /f /im %driver%
set result=
for /f "tokens=1" %%a in ('tasklist /FI "IMAGENAME eq %driver%" ^| findstr "^%driver%"') do  set result=%%a

IF /I "%result%"=="%driver%" (goto :killDriver)

ECHO --- Process %driver% is no longer running
ENDLOCAL

goto :eof