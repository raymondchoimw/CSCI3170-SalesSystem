# CSCI3170-SalesSystem
#### Group 4 Member
1. CHOI Man Wai (1155159354)
2. CHAN Kai Chun (1155159939)
3. CHENG Lam Hoi (1155144073)

#### List of files with description
All files are found in the folder `\src\SalesSystem`. Folder structure is as follow:
```
│   Admin.java
│   App.java
│   Database.java
│   Manager.java
│   mysql-jdbc.jar
│   Sales.java
│
└───demo_data
        category.txt
        manufacturer.txt
        part.txt
        salesperson.txt
        transaction.txt
```

##### Linux command needed due to folder structure
1. Change working directory
```
cd .\src\SalesSystem
```
2. Compile related .java files at once
```
javac -cp ..\ App.java
```
3. Execute with jdbc driver
```
java -cp './mysql-jdbc.jar:..' SalesSystem.App
```
