@echo off
setlocal EnableDelayedExpansion

SET prog86="c:\program files (x86)"
SET redist86= %cd%\lib\vcredist_x86.exe /q /norestart
set redist64=%cd%\lib\vcredist_x64.exe /q /norestart
SET INSTALL=0

IF EXIST %prog86% (call :x64) else (call :x86)

exit /b %ERRORLEVEL%
endlocal

:x86
	CALL :chkReg 86
	IF [%INSTALL%] EQU [1] (
		ECHO Instlling 32bit Microsoft Visual C++ Redistributable Libraries
		%redist86%
	) ELSE (
		ECHO   ALREADY INSTALLED
	)
	ECHO.
	goto :eof

:x64
	CALL :chkReg 64
	IF [%INSTALL%] EQU [1] (
		ECHO Instlling 64-bit Microsoft Visual C++ Redistributable Libraries
		%redist64%
	) ELSE (
		ECHO   ALREADY INSTALLED
	)
	ECHO.
	call :x86
	GOTO :eof
	
:chkReg
	SET INSTALL=0
	SET BITS=%1
	SET PROG86="c:\program files (x86)"
	SET VCREG=reg query HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\VisualStudio\10.0\VC\VCRedist\x%BITS% /v Installed  > nul

	IF [%BITS%] EQU [64] (
		IF NOT EXIST %PROG86% (
			SET INSTALL=0
			ECHO 32-bit OS cannot install 64-bit libraries
			GOTO :END
		)
	)

	%VCREG% > nul
	IF [%ERRORLEVEL%] EQU [1] (
		SET INSTALL=1
		GOTO :EOF
	)

	FOR /F "tokens=3" %%G IN ('%VCREG%') DO (
		SET INSTALL=%%G
	)
	ECHO INSTALL %INSTALL%
	SET INSTALL=%INSTALL:~0,3%
	IF [%INSTALL%] NEQ [0x1] ( SET INSTALL=1) ELSE (SET INSTALL=0)

	GOTO :EOF

:end
	ECHO End vcredist installation
	exit /b
 endlocal