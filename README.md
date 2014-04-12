xo-orientdb
===========

[OrientDB](https://github.com/tinkerpop/blueprints/wiki) binding for [eXtendend Objects (XO)](https://github.com/buschmais/extended-objects).

Maven Users
-----------

Our artifacts are published to the Maven Central repository and can be found under the ``com.smb-tec.xo`` groupId.

You'll need to add the following dependency in your builds (and Maven will automatically include the additional transitive dependencies to OrientDB for you). It currently uses the OrientDB 1.7-rc2 version:

	<dependency>
	  	<groupId>com.smb-tec.xo</groupId>
	  	<artifactId>xo-orientdb</artifactId>
	  	<version>0.0.1-SNAPSHOT</version>
	</dependency>
	
Getting Started
---------------

Please visit the project wiki for the latest information : [https://github.com/BluWings/xo-orientdb/wiki](https://github.com/BluWings/xo-orientdb/wiki)

License
-------

``xo-orientdb`` is contributed under Apache License 2.0.

Build
-----

Start the Maven build on command line

	mvn clean package

Continuous Build
----------------

[![Build Status](https://secure.travis-ci.org/BluWings/xo-orientdb.png)](http://travis-ci.org/BluWings/xo-orientdb)