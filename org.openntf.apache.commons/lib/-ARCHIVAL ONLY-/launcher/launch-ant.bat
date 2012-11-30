@echo off
if "%OS%" == "Windows_NT" setlocal

rem ---------------------------------------------------------------------------
rem
rem Sample script for launching Ant using the Launcher
rem
rem ---------------------------------------------------------------------------

rem Get standard environment variables
set PRG=%0
if exist %PRG%\..\settings.bat goto gotCmdPath
rem %0 must have been found by DOS using the %PATH% so we assume that
rem settings.bat will also be found in the %PATH%
call settings.bat
goto doneSetenv
:gotCmdPath
call %PRG%\..\settings.bat
:doneSetenv

rem Make sure prerequisite environment variables are set
if not "%JAVA_HOME%" == "" goto gotJavaHome
echo The JAVA_HOME environment variable is not defined
echo This environment variable is needed to run this program
goto end
:gotJavaHome

rem Get command line arguments and save them with the proper quoting
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

rem Execute the Launcher using the "ant" target
"%JAVA_HOME%\bin\java.exe" -classpath %PRG%\..;"%PATH%" LauncherBootstrap -verbose ant %CMD_LINE_ARGS%

:end
