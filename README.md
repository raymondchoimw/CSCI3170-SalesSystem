# CSCI3170-SalesSystem
## Getting Started
### Connecting to MySQL Database Server
Open your terminal, do the followings:
1. `ssh [account]@gw.cse.cuhk.edu.hk`
2. Enter your own password
3. `ssh linux[1-10]` (recommend using 1, 2; avoid using 9)
4. `mysql --host=projgw --port=2633 -u Group4 -p`
5. Enter the password `CSCI3170`

Use `db4` for the phase 2 project devlopement.
### Navigating MySQL Database
Note: End each line of command with `;`, otherwise it won't be executed.
#### Show all databases in the MySQL Server
```
SHOW DATABASES;
```
## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Compile .java in CLI

- Step 1: In your terminal, change your working directory to `.\src\SalesSystem`
- Step 2: In CLI, type `javac -cp ../ App.java`

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
