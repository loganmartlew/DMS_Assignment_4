Team:
Logan Martlew 19076051
Coen Yakas 14851544

Split Grade

Grace days: whatever we have left

Instructions to Run:

ApiInterface.kt in android app needs IP address to be modified to
wherever the server is deployed

Some glassfish resources are required to run the app:
JDBC Pool - jdbc/MySQLDmsAssignmentResource
JMS Queue Connection Factory - jms/AddressBookConnectionFactory
JMS Queue Resources:
jms/AddressBookLocationQueue
jms/AddressBookUserQueue
jms/AddressBookRequestQueue