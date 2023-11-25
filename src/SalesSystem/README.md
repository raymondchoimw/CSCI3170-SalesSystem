# CSCI3170-SalesSystem
#### Group 4 Member
1. CHOI Man Wai (1155159354)
2. CHAN Kai Chun (1155159939)
3. CHENG Lam Hoi (1155144073)

#### List of files with description
All files are found in the folder `\src\SalesSystem`. Folder structure is as follow:
```
│   Admin.java		# All functions related to Administrator, including Administrator Menu & other subfunctions.
│   App.java		# Landing page of the app (i.e. Main Menu), and helper function applicable to whole app.
│   Database.java	# Helper functions related to database, including connection, checking table existence and format table output.
│   Manager.java	# All functions related to Manager, including Manager Menu & other subfunctions.
│   mysql-jdbc.jar	# MySQL JBDC Driver 
│   Sales.java		# All functions related to Salesperson, including Salesperson Menu & other subfunctions.
│
└───demo_data                # Each txt file contains data to be loaded into the database table of the same name.
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
