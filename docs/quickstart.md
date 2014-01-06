---
layout: main
title: "RoboZombie"
---

RoboZombie works by allowing you to describe a remote service on an interface using annotations and giving you a proxy implementation of that interface which can be used to invoke the remote service.
<br><br>

####1. Defining an endpoint.

Use `@Endpoint` to annotate the interface which describes the remote service. It requires a **URI** for locating the remote service.   

{% highlight java %}
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

}
{% endhighlight %}
<br>

####2. Defining a request.

Add an aptly named abstract method and annotate it to identify the **HTTP method** to be used. This annotation accepts the request **path**.   

Most web APIs today follow a **RESTful** pattern. In this case you may need to parameterize the path using a suitable name enclosed in curly braces, eg. `{param}`. The path parameter can be supplied as an argument when invoking the request. Identify this argument by annotating a request parameter with `@PathParam("param")`.   

The method return type indicates the response content you wish to receive. Request definitions with a return type of `String` will give you the raw response body, e.g. a JSON string.   

{% highlight java %}
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    String getRepos(@PathParam("user") String user);
}
{% endhighlight %}
<br>

####3. Using models and deserializers.

It would be convenient if the request gave you a deserialized **model** instead of a plain JSON string. To achieve this, create a simple POJO with variables names which match the JSON object.

{% highlight java %}
public class Repo {

    private String id;
    private String name;
    private boolean fork;
    private int stargazers_count;
    
    ...
}
{% endhighlight %}
   
Next, annotate either the endpoint or the request with `@Deserialize` and specify the content type with `Entity.ContentType`. Since the response gives you an array of `Repo` JSON objects, you can redefine the return type as `List<Repo>`.

{% highlight java %}
import static com.lonepulse.robozombie.annotation.Entity.ContentType.JSON;

@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user);
}
{% endhighlight %}
<br/>

####4. Injecting the endpoint.

You can use this endpoint in an object by declaring a variable of the interface type and injecting the proxy implementation of it. All such proxies are **thread-safe** and exist as **singletons**.   

To inject an endpoint proxy, annotate the variable with `@Bite` and invoke `Zombie.infect(...)` on the instance.   

{% highlight java %}
public class ProfileService implements ProfileManager {

    @Bite
    private GitHubEndpoint gitHubEndpoint;
    {
        Zombie.infect(this);
    }

    @Override
    public void addGitHubRepos() {

        List<Repo> repos = gitHubEndpoint.getRepos("sahan");

        ...
    }
}
{% endhighlight %}
