# CSCI3170-SalesSystem
## Getting Started
### Connecting to MySQL Database Server
Open your terminal, do the followings:
1. Connect to CSE server
```
ssh [account]@gw.cse.cuhk.edu.hk
```
2. Enter your own password
3. Connect to linux server (recommend using 1 or 2;anyway, avoid using 9)
```
ssh linux[1-10]
```
4. Go to MySQL server
```
mysql --host=projgw --port=2633 -u Group4 -p
```
5. Enter the password
```
CSCI3170
```

Note: Use `db4` for the phase 2 project devlopement.
### Navigating MySQL Database
Note: End each line of command with `;`, otherwise it won't be executed. The command is case-insensitive.
#### Show all databases in the MySQL Server
```
SHOW DATABASES;
```
#### Show all tables in a particular database
```
USE db4;
SHOW TABLES;
```
## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Compile & Execute .java in CLI
1. Change working directory
```
cd .\src\SalesSystem
```
2. Compile related .java files at once
```
javac -cp ..\ App.java
```
3. Execute with jdbc driver [Ref](https://stackoverflow.com/questions/18093928/what-does-could-not-find-or-load-main-class-mean)\
Note: As of 7/11/2023 18:45, test driver-related issue using `SalesSystem.Database`
- For Windows PowerShell
```
java -cp '.\mysql-jdbc.jar;..' SalesSystem.App
```
- For Windows cmd
```
java -cp ".\mysql-jdbc.jar;.." SalesSystem.App
```
- For other CLI
> Consult ChatGPT or New Bing for translation

Together (for easy copying), it is:
```
cd .\src\SalesSystem
javac -cp ..\ App.java
java -cp '.\mysql-jdbc.jar;..' SalesSystem.App
```
## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
