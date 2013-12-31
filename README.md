<table>
<tr>
<td>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://raw.github.com/sahan/RoboZombie/master/logo.png"/>
</td>
<td rowspan="3">
<blockquote>
<b>RoboZombie</b> &nbsp;&nbsp;/rō-bō'zŏm'bē/ &nbsp;&nbsp;<em>noun.</em> 
<br><br>
Makes Android networking a breeze. Accepts an interface which describes the remote service and gives you an implementation of it in return.
<br><br>
<a href="https://travis-ci.org/sahan/RoboZombie"><img alt="Build Status" src="https://travis-ci.org/sahan/RoboZombie.png?branch=master"></a>&nbsp;&nbsp;
<a href="https://coveralls.io/r/sahan/RoboZombie?branch=master"><img alt="Coverage Status" src="https://coveralls.io/repos/sahan/RoboZombie/badge.png?branch=master"></a>
</blockquote>
</td>
</tr>
<tr>
<td>
<a href="http://repo1.maven.org/maven2/com/lonepulse/robozombie/1.3.1/robozombie-1.3.1.jar"><pre>robozombie-1.3.1.jar</pre></a>
</td>
</tr>
</table>

<br>
##Overview

Here's your model.   

```java
public class Repo {

    private String id;
    private String name;
    private boolean fork;
    private int stargazers_count;
    
    ...
}
```
> Follow the same variable names as the JSON response.   

<br>
Define the endpoint.   

```java
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user);
}
```
> Looks for [Gson](http://code.google.com/p/google-gson) on your build path.   

<br>
Inject and invoke.   

```java
@Bite
private GitHubEndpoint endpoint;   

{
    Zombie.infect(this);
}

...

List<Repo> repos = endpoint.getRepos("sahan");
```
<br>
Create as many endpoints as you want...   

```java
@Endpoint("http://example.com")
public interface ExampleEndpoint {

    @Serialize(XML)	
    @PUT("/content")
    void putContent(@Entity Content content);
}
```
> Looks for [Simple-XML](http://simple.sourceforge.net) on your build path.

<br>
...and inject 'em all.   

```java
@Bite
private GitHubEndpoint gitHubEndpoint;

@Bite
private ExampleEndpoint exampleEndpoint;

{
    Zombie.infect(this);
}
```
<br>
RoboZombie requires the **INTERNET** manifest permission for network communication.   
 
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
> ...and be sure to invoke all synchronous requests from a worker thread.

<br>
##Setup

### 1. For Maven based projects.   

Add the following dependency to project's pom.xml file.

```xml
<dependency>
   <groupId>com.lonepulse</groupId>
   <artifactId>robozombie</artifactId>
   <version>1.3.1</version>
</dependency>
```

<br>   
### 2. For Gradle based projects.   

Add the following repository and dependency to your project's build.gradle file.

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.lonepulse:robozombie:1.3.1'
}
```

<br>   
### 3. Add the JAR to your build path manually.   

Download the [RoboZombie-1.3.1.jar](http://repo1.maven.org/maven2/com/lonepulse/robozombie/1.3.1/robozombie-1.3.1.jar) 
and add it to your **libs** folder.   
<br>

> Note that [Gson](http://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.2.4/gson-2.2.4.jar) 
is required for JSON (de)serialization and [Simple-XML](http://search.maven.org/remotecontent?filepath=org/simpleframework/simple-xml/2.7.1/simple-xml-2.7.1.jar) 
is required for XML (de)serialization.   

<br>
##Wiki <pre><font color = "#F78181">(OBSOLETE)</font></pre>

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
