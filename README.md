# JUnit Platform Maven Plugin
 
[![jdk11](https://img.shields.io/badge/jdk-11-blue.svg)](http://jdk.java.net/11/)
[![travis](https://travis-ci.com/sormuras/junit-platform-maven-plugin.svg?branch=master)](https://travis-ci.com/sormuras/junit-platform-maven-plugin)
[![experimental](https://img.shields.io/badge/api-experimental-yellow.svg)](https://javadoc.io/doc/de.sormuras/junit-platform-maven-plugin)

Maven Plugin launching the JUnit Platform

## Goals

* Utilize JUnit Platform's ability to execute multiple `TestEngine`s natively.
* Support _white-box_ and _black-box_ testing when writing modularized projects.

## Usage with Jupiter

In your `pom.xml`, just add the test compile dependency, like this:

```xml
<dependencies>
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.2.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.2.0</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

And configure the `junit-platform-maven-plugin` like this in the `<build><plugins>`-section:

```xml
<plugin>

  <groupId>de.sormuras</groupId>
  <artifactId>junit-platform-maven-plugin</artifactId>
  <version>0.0.4</version>
  
  <!-- Configure the plugin. -->
  <configuration>
    <timeout>99</timeout>
    <strict>true</strict>
    <reports>custom-reports-directory</reports>
  </configuration>
  
  <!-- Add matching TestEngine implementation to the test runtime. -->
  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.2.0</version>
      <scope>runtime</scope>
    </dependency>
  </dependencies>
  
  <!-- Bind and execute the plugin to the test phase. -->
  <executions>
    <execution>
      <phase>test</phase>
      <goals>
        <goal>launch-junit-platform</goal>
      </goals>
    </execution>
  </executions>

</plugin>
```