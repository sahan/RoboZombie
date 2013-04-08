![RoboZombie](https://raw.github.com/sahan/RoboZombie/master/logo.png)

> **RoboZombie** /rō-bō'zŏm'bē/ <em>noun.</em> **1** A lightweight HTTP facade 
which simplifies network communication. **2** An endpoint proxy generator for web services. 
[![Build Status](https://travis-ci.org/sahan/RoboZombie.png?branch=master)](https://travis-ci.org/sahan/RoboZombie)

<br/>
##About

**RoboZombie** allows easy integration with remote services by allowing you to replicate an endpoint 
contract and generate a proxy to access it.

* Contracts can be very flexible in terms of the resources they access. These could be vary from static 
*html* content or an *RRS* feed, to a RESTful web service endpoint.   
<br/>
* Each endpoint contract is specified on a single interface using annotations to provide the communication 
metadata. It is then wired into your code via an annotation, where it'll be created, cached and injected at 
runtime.   
<br/>

##Setup

### 1. For Maven Based Android Projects

Add the following dependency in your project's pom.xml.

```xml
<dependency>
   <groupId>com.lonepulse</groupId>
   <artifactId>robozombie</artifactId>
   <version>1.2.2</version>
   <type>apklib</type>
</dependency>
```

For information on building Android projects using Maven here's [Chapter 14](http://www.sonatype.com/books/mvnref-book/reference/android-dev.html) of `Maven: The Complete Reference`.   
<br/>   

### 2. For Standard Android Projects

Clone the repository and import it as an **existing project** in Eclipse.

```bash
$ git clone git://github.com/sahan/RoboZombie.git
```

Since the Eclipse metadata is maintained in the repository, the project should be immediately available as an Android library which you can reference in your own project. Refer the [developer guide](http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject) for information on referencing library projects.   
<br/><br/>

##Usage

Coding with RoboZombie is a breeze. It follows a simple annotation based coding style and adheres to a *minimal intrusion* policy. 
Kickoff with the [quickstart](https://github.com/sahan/RoboZombie/wiki/Quickstart) and follow the rest of the wiki pages. 

1. [Quickstart](https://github.com/sahan/RoboZombie/wiki/Quickstart)

2. [Defining Endpoint Contracts](https://github.com/sahan/RoboZombie/wiki/Defining-Endpoint-Contracts)

3. [Working With Response Parsers](https://github.com/sahan/RoboZombie/wiki/Working-With-Response-Parsers)

4. [Injecting Endpoint Proxies](https://github.com/sahan/RoboZombie/wiki/Injecting-Endpoint-Proxies)

5. [Accessing RESTful Services](https://github.com/sahan/RoboZombie/wiki/Accessing-RESTful-Services)   

6. [Executing Requests Asynchronously](https://github.com/sahan/RoboZombie/wiki/Executing-Requests-Asynchronously)
<br><br>   

> RoboZombie requires the **INTERNET** manifest permission for network communication.   
 
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
<br/>

##License
This library is licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
