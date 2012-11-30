Jakarta Commons Transaction
===========================

Welcome to the Transaction component of the Jakarta Commons project.
This component contains implementations of utility classes commonly used in
transactional Java programming. Initially there are implementations for
multi level locks, transactional collections and transactional file access.

Building from source
--------------------
This component requires the excellent Ant utility.
It can be found here :

  http://ant.apache.org/

Once you have Ant propertly installed you are ready to
build and test. All necessary jars are already present and
you will only have to create a build.properties from
build.properties.sample if you want to compile to other 
versions.

The major ant targets are:

ant build     - Compiles the main classes
ant jar       - Creates the jar
ant test      - Exectutes JUnit tests
ant doc       - Generates documentation
ant javadocs  - Creates the API documentation
ant dist      - Creates distribution
ant package   - Packages distribution
ant           - Creates jar

Maven
-----
The component can also be built using Maven. (Ant is the primary build tool.)
It can be found here :

  http://maven.apache.org/

Once installed, the jars may be built with 'maven jar'.
