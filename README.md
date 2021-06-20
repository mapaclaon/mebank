#ME Bank Coding Challenge
Implement a system that analyses financial transaction records

The system will be initialised with an input file in CSV format containing a list of
transaction records.
Once initialised it should be able to print the relative account balance (positive or
negative) in a given time frame.
The relative account balance is the sum of funds that were transferred to / from an
account in a given time frame, it does not account for funds that were in that account
prior to the timeframe.

Another requirement is that, if a transaction has a reversing transaction, this
transaction should be omitted from the calculation, even if the reversing transaction is
outside the given time frame.

#Pre-requisite
1. Java 8
2. Maven

#Assumptions
It is assume that:
1. Input file and records are all in a valid format
2. Transactions are recorded in order
3. Input `From` date is same or before the `To` date.
4. Input `To` date is same or after the `From` date.
5. Input date is in a valid format i.e. `dd/MM/yyyy HH:mm:ss` `(20/10/2018 18:00:00)`

#Project Setup and Running
1. git clone `git@github.com:mapaclaon/mebank.git`
2. From your IDE, import existing maven project
3. To build the application

    `mvn clean install`
    
4. To run the application 

   4.1 From IDE
   
        4.1.1 add a program argument with value equals to the path of the input csv file
        4.1.2 main class com.mebank.MEBankApplication
   
   4.2 From terminal
   
        4.2.1 mvn exec:java -Dexec.mainClass=com.mebank.MEBankApplication -Dexec.args="<path to csv file>", e.g "C:\mebank.csv"

5. Test class - com.mebank.TransactionServiceTest

