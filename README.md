[![Stories in Ready](https://badge.waffle.io/sahan/RoboZombie.png)](http://waffle.io/sahan/RoboZombie)  
![RoboZombie](https://raw.github.com/sahan/RoboZombie/master/logo.png)

> **RoboZombie** /rE-bE'zEm'bD/ <em>noun.</em> **1** A lightweight HTTP facade 
which simplifies network communication. **2** An endpoint proxy generator for web services. 
[![Build Status](https://travis-ci.org/sahan/RoboZombie.png?branch=master)](https://travis-ci.org/sahan/RoboZombie)

<br>
##About

**RoboZombie** allows easy integration with remote services by allowing you to replicate an endpoint 
contract and generate a proxy to access it.

* Contracts can be very flexible in terms of the resources they access. These could be vary from static 
*html* content or an *RRS* feed, to a RESTful web service endpoint.   
<br>
* Each endpoint contract is specified on a single interface using annotations to provide the communication 
metadata. It is then wired into your code via an annotation, where it'll be created, cached and injected at 
runtime.   
<br>

##Overview   
<br>
Here's your Model   

```java
public class Conversion {

    private String lhs;
    private String rhs;
    private String error;
    private boolean icc;

    /* Accessors, Mutators, hashCode(), 
     * equals() and toString() omitted */
}
```
<br>
Define the Endpoint Interface   

```java
@Endpoint("www.google.com")
@Parser(PARSER_TYPE.JSON)
public interface ConverterEndpoint {

    @Request(path = "/ig/calculator")
    Conversion convert(@Param("q") String query);
}
```
<br>
Inject and Invoke   

```java
@Bite
private ConverterEndpoint endpoint;
{
    Zombie.infect(this);
}

...

Conversion rate = endpoint.convert("1USD=?AUD");
```
<br>
Create as many endpoints as you want...   

```java
@Endpoint("s3.amazonaws.com/archive.travis-ci.org")
public interface AmazonS3Endpoint {

    @Parser(PARSER_TYPE.STRING)	
    @Rest(path = "/jobs/:job_id/log.txt")
    String getBuildLog(@PathParam("job_id") String jobId);
}
```
<br>
...and inject 'em all.   

```java
@Bite
private ConverterEndpoint ccEndpoint;

@Bite
private AmazonS3Endpoint s3Endpoint;

{
    Zombie.infect(this);
}
```
<br>
RoboZombie requires the **INTERNET** manifest permission for network communication.   
 
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
> ...and be sure to invoke all endpoint calls from a worker thread.

<br>
##Setup

### 1. For Maven Based Android Projects

Add the following dependency in your project's pom.xml.

```xml
<dependency>
   <groupId>com.lonepulse</groupId>
   <artifactId>robozombie</artifactId>
   <version>1.2.3</version>
   <type>jar</type>
</dependency>
```

For information on building Android projects using Maven here's [Chapter 14](http://www.sonatype.com/books/mvnref-book/reference/android-dev.html) of `Maven: The Complete Reference`.   
<br>   

### 2. For Standard Android Projects

Download the [RoboZombie](http://repo1.maven.org/maven2/com/lonepulse/robozombie/1.2.3/robozombie-1.2.3.jar) + [Gson](http://repo1.maven.org/maven2/com/google/code/gson/gson/2.2.2/gson-2.2.2.jar) 
jars and add them to your **libs** folder.
<br><br>

##Wiki

Coding with RoboZombie is a breeze. It follows a simple annotation based coding style and adheres to a *minimal intrusion* policy. 
Kickoff with the [quickstart](https://github.com/sahan/RoboZombie/wiki/Quickstart) and follow the rest of the wiki pages. 

1. [Quickstart](https://github.com/sahan/RoboZombie/wiki/Quickstart)

2. [Defining Endpoint Contracts](https://github.com/sahan/RoboZombie/wiki/Defining-Endpoint-Contracts)

3. [Working With Response Parsers](https://github.com/sahan/RoboZombie/wiki/Working-With-Response-Parsers)

4. [Injecting Endpoint Proxies](https://github.com/sahan/RoboZombie/wiki/Injecting-Endpoint-Proxies)

5. [Accessing RESTful Services](https://github.com/sahan/RoboZombie/wiki/Accessing-RESTful-Services)   

6. [Executing Requests Asynchronously](https://github.com/sahan/RoboZombie/wiki/Executing-Requests-Asynchronously)
<br><br>   

##License
This library is licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
