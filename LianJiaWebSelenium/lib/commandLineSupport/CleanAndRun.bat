:: Usage... TestCaseRunner.bat suite.tests
:: Where suite.tests contains a list of test case classes, one per line, in the form:
::
::   com.lianjia.lianjiaweb.authentication.JustForTest
::   com.lianjia.lianjiaweb.bvt0.CityExchangeTestCase

@ECHO Off
echo.

call Reset.bat

java -Xmx1024m -cp lianjia.selenium.auto.jar;common.selenium.jar;libs.jar com.lianjia.lianjiaweb.suite.SuiteLauncher %*

type logs\current\results.txt
echo.
echo.
