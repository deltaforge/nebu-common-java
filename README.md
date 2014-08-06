# Nebu Common Java

## What is it?
This project is a library that is part of the Nebu software ecosystem written in
Java. The library holds data container classes for physical topology objects and
other utility functions.

## How do you install it?
Since this project uses maven, all you have to do to install this project is run
`mvn install` in the root of the project. It can then be included in other
mvn projects by adding the following to your pom.xml file:
``` xml
<dependency>
	<groupId>nl.bitbrains.nebu.common</groupId>
	<artifactId>nebu-common</artifactId>
	<version>0.1</version>
</dependency>
```

