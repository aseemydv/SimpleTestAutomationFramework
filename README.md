
# Home test task

**Test Coverage**
-----
 * web application with url http://automationpractice.com/index.php;
 * 4 [test cases](TESTCASES.md);
 * 4 automated tests.
 

**How to Run ?**
----
Project has been prepared on IntelliJ Idea. Create a project by doing a checkout of this repository.


Platform and tools information:
* OS: Windows 10 (Build 16299)
* Browsers: Chrome, Firefox, Microsoft Edge
* Frameworks/Tools: Selenium, Apache POI, TestNG, Log4j
* IDE: IntelliJ Idea Community Edition - 2018.1
* Java JDK Version: 10.0.1

Setting-Up:
* In  Run > Edit Configurations, select the Test Kind as "Suite" and set the path to Testng.xml file.
* In Module settings, set the Language level to "10 - Local varibale type inference".
* In Settings > Build, Execution, Deployment > Compiler > Java Compiler, set the Project and Target Bytecode Version to "10".
* Invoke TESTNG to start test execution - Select "testng.xml" from the tree, click "Run"

Execution:
* In TESTNG.xml -- Change the "parallel" attribute to "tests" for running all the tests on different browsers in parallel.
* In PageSetup.java -- Change the "invocationCount" in @Test annotation, for running the tests multiple times.




