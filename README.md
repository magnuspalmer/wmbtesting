wmbtesting
==========

Samples of how to test the IBM WebSphere Message Broker using Groovy, Spock and Apache Camel.

Prerequisites:
--------------

__WMB8__

It uses the WMB8 Default Configuration for development (however this is configurable, see Configuration)
This means:
*  A broker called MB8BROKER
*  A queuemanager called MB8QMGR with a listener on port 2414

[WMB8 InfoCenter](http://publib.boulder.ibm.com/infocenter/wmbhelp/v8r0m0/topic/com.ibm.etools.mft.doc/ae20200_.htm "Creating a default configuration")

__JDK SE 7__

You need a JDK SE 7 and also you need to set the JAVA_HOME environment variable.

Configuration:
--------------

Edit the src/test/resources/wmq.properties if you have other connection params to your queuemanager.
Default is:

     qmgr.hostName=localhost
     qmgr.port=2414
     qmgr.queueManager=MB8QMGR
     qmgr.channel=JMS.CHANNEL
     #0 = binding, 1 = client, 8 = first binding, then client
     qmgr.transportType=1

Building/testing with Gradle:
-----------------------------

All you need is Java and you can run the Gradle build using gradlew or gradlew.bat depending upon OS.
No need for Gradle or Groovy...

gradlew.bat test to run the tests (it will download a gradle-wrapper dist).
