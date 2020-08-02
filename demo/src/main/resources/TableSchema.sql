
------User Table for sigin/signup-------------------
-----------------------------------------------------------

CREATE TABLE USER(
   USERNAME VARCHAR(100) NOT NULL,
   PASSWORD VARCHAR(1000) NOT NULL,
   FIRSTNAME VARCHAR(1000) NOT NULL,
   LASTNAME VARCHAR(1000) NOT NULL,
   PHONENUMBER INT(10) NOT NULL,
   WALLETID INT(10) NOT NULL ,
   PRIMARY KEY (`USERNAME`),
   FOREIGN KEY (`WALLETID`) REFERENCES WALLET(WALLETID)
   
);

COMMIT;

------Wallet Table for Transaction---------
-----------------------------------------------------------
CREATE TABLE WALLET(
WALLETID INT(10) AUTO_INCREMENT,
BALANCE INT(10),
PRIMARY KEY (`WALLETID`)
);

commit;

---TRANSACTION Table for TransactionHistory---------
------------------------------------------------------------

CREATE TABLE TRANSACTION(
   TRANSACTIONID INT(10) NOT NULL AUTO_INCREMENT,
   TRANSACTIONDATE TIMESTAMP NOT NULL,
   AMOUNT INT(10) NOT NULL,
   FROMUSERNAME VARCHAR(100) NOT NULL,
   TOUSERNAME VARCHAR(100) NOT NULL,
   TRANSACTIONSTATUS VARCHAR(1000) NOT NULL,
   PRIMARY KEY (`TRANSACTIONID`) 
   
);

COMMIT;