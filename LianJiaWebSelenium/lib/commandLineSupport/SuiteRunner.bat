:: Usage... TestCaseRunner.bat suite.tests
:: Where suite.tests contains a list of test case classes, one per line, in the form:
::
::   com.lianjia.test.lianjiaweb.authentication.JustForTest
::   com.lianjia.test.lianjiaweb.bvt0.CityExchangeTestCase

@ECHO Off
echo.

call vcredist_install.bat

java -Xmx1024m -cp lianjia.selenium.auto.jar;common.selenium.jar;libs.jar com.lianjia.test.lianjiaweb.suite.SuiteLauncher %*

:: Remove the temporary UserSet provided by GRTE 
:: del /f/s/q targets\auto

type logs\current\results.txt
echo.
echo.
