---
layout: main
title: "RoboZombie | Docs"
tag: docs
---

> Note that [Gson](http://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.2.4/gson-2.2.4.jar) 
is required for JSON (de)serialization and [Simple-XML](http://search.maven.org/remotecontent?filepath=org/simpleframework/simple-xml/2.7.1/simple-xml-2.7.1.jar) 
is required for XML (de)serialization. If these two libraries are not detected on the build-path, the JSON and XML serializers will be disabled and any usage will result in an informative warning.

<span id="1"></span>
<br><br>
<h3>Quickstart</h3>

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

<span id="2"></span>
<br><br>
<h3>Defining, Injecting and Invoking</h3>

Endpoints are defined on an **interface** with the `@Endpoint` annotation which takes a URI to locate the endpoint.   

{% highlight java %}
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    ...
}
{% endhighlight %}
> Trivially, an alternate port or a root path can be defined.   

<br>
By defining requests on the endpoint you create the **endpoint contract**. A request definition is created using an abstract method annotated with `@GET`, `@POST`, `@PATCH`, `@PUT`, `@DELETE`, `@HEAD`, `@TRACE` or `@OPTIONS`. These identify the [HTTP method](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html) for the request and the subpath as a parameter. A method return type of `String` will give you the raw response body, e.g. a JSON string.   

{% highlight java %}
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/sahan/repos")
    String getRepos();
}
{% endhighlight %}
> The subpath will be directly appended to the endpoint URI.

<br>
If the subpath needs to be parameterized, a placeholder can be defined with a parameter name wrapped between curly braces. Its value can be supplied upon invocation using a method argument. The method parameter for the argument should have an `@PathParam` annotation which takes the name of the URL parameter.   

{% highlight java %}
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    String getRepos(@PathParam("user") String user);
}
{% endhighlight %}
> A subpath can contain multiple parameters and the endpoint URI could be parameterized as well.

<br>
Before using an endpoint it should be injected. Define a property with the endpoint interface type and annotate it with `@Bite`. To inject the endpoint, use `Zombie.infect(...)` on your object. To execute a request simply invoke the method on the interface.   

{% highlight java %}
@Bite
private GitHubEndpoint gitHubEndpoint;
{
    Zombie.infect(this);
}

...

String repoJSON = gitHubEndpoint.getRepos("sahan");
{% endhighlight %}
> A single invocation of `Zombie.infect(...)` will search an entire inheritance hierarchy for endpoints.

<br>
If the request executed successfully, the response body should be returned. If an error occurred while creating the request and executing it or while processing the response a **context-aware** `InvocationException` will be thrown. Use `getContext()` to retrieve the `InvocationContext` which can be used to examine the request information and meta-data. If the error occurred while processing the response, an `HttpResponse` should be available. Invoke `hasResponse()` to check the availability of an [`HttpResponse`](http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/HttpResponse.html) and invoke `getResponse()` to retrieve it.   

{% highlight java %}
try {

    String repoJSON = gitHubEndpoint.getRepos("sahan");
}
catch(InvocationException errorContext) {

    InvocationContext context = errorContext.getContext();
    ...
                
    if(errorContext.hasResponse()) {
                    
        HttpResponse response = errorContext.getResponse();
        ...
    }
}
{% endhighlight %}
> Clearly, the `hasResponse()` method doubles as an indicator to distinguish between a request creation/execution error vs a response processing error.

<span id="3"></span>
<br><br>
<h3>Identifying HTTP Methods</h3>

An [HTTP request method](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html) is a *verb* which specifies the action to be performed on a resource. The resource is identified using the complete URI and a combination of query parameters, form parameters or request body. Eight such methods are supported: **GET**, **POST**, **PATCH**, **PUT**, **DELETE**, **HEAD**, **TRACE** and **OPTIONS**. Request definitions that use these methods are identified by the annotation of the same name.   

<br>
####1. HTTP GET

`GET` requests are used for retrieving a resource identified by the URI. It can use a [query string](https://github.com/sahan/RoboZombie/wiki/Sending-Query-and-Form-Parameters) to further filter the identity of the resource. A `GET` request should not possess a payload in its body; hence any `@FormParam`, `@FormParams` and `@Entity` annotations are forbidden.   

{% highlight java %}
@GET("/repos/sahan/RoboZombie/issues")
List<Issue> getIssues(@QueryParam("state") String state);
{% endhighlight %}

<br>
####2. HTTP POST

`POST` requests are used to update the a resource identified by the URI. The request body should contain the update version of the resource as the payload. A POST request is allowed to use `@FormParam`, `@FormParams` and `@Entity` to describe the updated resource. Some servers may create the resource if it doesn't exist already and reply with `210 (Created)`.   

{% highlight java %}
@POST("/gists")
void postGist(@Entity Gist gist);
{% endhighlight %}

<br>
####3. HTTP PATCH

`PATCH` requests are relatively new as was introduced in [RFC 5789](http://tools.ietf.org/html/rfc5789). They are used to apply partial updates to the resource identified by the URI. Like `POST`, if the URI does not point to an existing resource, the resource may be created instead. The patched resource is sent as a request payload using the `@Entity` annotation.   

{% highlight java %}
@PATCH("/gists/{id}")
void patchGist(@PathParam("id") String id, @Entity Gist gist);
{% endhighlight %}
> `@FormParam` and `@FormParams` are permissible.

<br>
####4. HTTP PUT

`PUT` requests are used to create a new resource at the location identified by the URI. The resource to be created is sent within the request body using an `@Entity` annotation.   

{% highlight java %}
@PUT("/repos/sahan/RoboZombie/issues/{no}/labels")
void putLabels(@PathParam("no") String no, @Entity List<String> labels);
{% endhighlight %}

<br>
####5. HTTP DELETE

`DELETE` requests are used to remove the resource identified by the URI from the server. These requests should not contain a payload in its body; hence any `@FormParam`, `@FormParams` and `@Entity` annotations are forbidden.   

{% highlight java %}
@DELETE("/repos/sahan/RoboZombie/issues/{no}/labels")
void deleteAllLabels(@PathParam("no") String no);
{% endhighlight %}

<br>
####6. HTTP HEAD

`HEAD` requests are used to retrieve meta-information about the resource identified by the URI. These requests are quite similar to `GET` requests with the exception that the response does not include a payload in its body. A `HEAD` request may not contain a request body and the required meta-information is contained within the response headers.   

{% highlight java %}
@HEAD("/user/repos")
void queryAuthScheme(@Header("WWW-Authenticate") StringBuilder authResponseHeader);
{% endhighlight %}

<br>
####7. HTTP TRACE

`TRACE` requests are used to follow a request to the server and use that information for testing and diagnostics. A `TRACE` request may not contain a request body.   

{% highlight java %}
@TRACE("/feeds")
void queryGateways(@Header("Via") StringBuilder viaResponseHeader);
{% endhighlight %}

<br>
####8. HTTP OPTIONS

`OPTIONS` requests are used for retrieving information about the communication options available to the server regarding the resource identified by the URI. An `OPTIONS` request may not contain a request body.   

{% highlight java %}
@OPTIONS("/feeds")
void queryContentType(@Header("Content-Type") StringBuilder contentTypeResponseHeader);
{% endhighlight %}

<span id="4"></span>
<br><br>
<h3>Sending Query and Form Parameters</h3>

A request may require additional parameters submitted as a [query string](http://en.wikipedia.org/wiki/Query_string) or a [form-url-encoded string](http://en.wikipedia.org/wiki/Percent-encoding) which comprises of a set of **key-value pairs**. A request definition should specify the keys to be used and the values may be defined as constants or supplied as arguments.   

<br>
####1. Query Parameters

A query parameter is identified using `@QueryParam` by supplying the **key** for the key-value pair. This annotation can only be used on arguments of type `CharSequence`.   

{% highlight java %}

@GET("/repos/sahan/RoboZombie/issues")
List<Issue> getIssues(@QueryParam("state") String state);

...

List<Issue> openIssues = gitHubEndpoint.getIssues("open");
{% endhighlight %}
> `https://api.github.com/repos/sahan/RoboZombie/issues ?state=open`

Certain requests may use many query parameters. Although they can be defined individually using `@QueryParam`, an alternate approach is to use `@QueryParams` on a `Map<CharSequence, CharSequence>`. The entries in the `Map` should be the key-value pairs for the query string.   

{% highlight java %}

@GET("/repos/sahan/RoboZombie/issues")
List<Issue> getIssues(@QueryParams Map<String, String> params);

...

Map<String, String> params = new LinkedHashMap<String, String>();
params.put("state", "open");
params.put("page", "1");
params.put("per_page", "8");

List<Issue> openIssues = gitHubEndpoint.getIssues(params);
{% endhighlight %}
> `https://api.github.com/repos/sahan/RoboZombie/issues? state=open & page=1 & per_page=5`

<br>
####2. Constant Query Parameters

If the same query parameters are passed for each invocation, they can be declared as constants using `@QueryParams` and `@Param`.   

{% highlight java %}

@GET("/repos/sahan/RoboZombie/issues")
@QueryParams({@Param(name = "state", value = "open"), 
              @Param(name = "per_page", value = "8")})
List<Issue> getOpenIssues(@QueryParam("page") String page);

...

List<Issue> openIssuesPg1 = gitHubEndpoint.getOpenIssues("1");
{% endhighlight %}
> `https://api.github.com/repos/sahan/RoboZombie/issues? state=open & per_page=10 & page=1`

<br>
####3. Batch Query Parameters

Query strings may contain key-value pairs with the same key but different values. This is common when multiple options are possible for a query parameter, e.g. `hobby=reading & hobby=antiquing`.

Batch query parameters are identified with `@QueryParams` on a map of type `Map<String, Collection<CharSequence>>`.   

{% highlight java %}

@GET("/search/issues")
Results searchOpenIssues(@QueryParams Map<String, Object> params);

...

List<String> values = new ArrayList<String>();
values.add("value1");
values.add("value2");
values.add("value3");

Map<String, Object> params = new LinkedHashMap<String, Object>();
params.put("key", values);
params.put("state", "open");

Results results = gitHubEndpoint.searchOpenIssues(params);
{% endhighlight %}
> `https://api.github.com/search/issues? key=value1 & key=value2 & key=value3 & state=open`

<br>
####4. Form Parameters

Form parameters are sent as `x-www-form-urlencoded` data within the body of the request. This is essentially a series of key-value pairs similar to that of a query string. Form parameters are identified using `@FormParam` on arguments of type `CharSequence` by supplying the **key** for the key-value pair.   

{% highlight java %}
@POST("/emails/send")
void sendEmail(@FormParam("from") String from, @FormParam("to") String to, 
               @FormParam("subject") String subject, @FormParam("text") String text);

...

endpoint.sendEmail("sahan@example.com", "someone@example.com", "Hi", "Knock-Knock");
{% endhighlight %}
> `request-body: from=sahan@example.com & to=someone@example.com & subject=Hi & text=Knock-Knock`

Like query parameters, form parameters may be supplied as a map of key-value pairs using `@FormParams` on a request argument of type `Map<CharSequence, CharSequence>`.   

{% highlight java %}
@POST("/emails/send")
void sendEmail(@FormParams Map<String, String> params);

...

Map<String, String> params = new LinkedHashMap<String, String>();
params.put("from", "sahan@example.com");
params.put("to", "someone@example.com");
params.put("subject", "Hi");
params.put("text", "Knock-Knock");

endpoint.sendEmail(params);
{% endhighlight %}
> `request-body: from=sahan@example.com & to=someone@example.com & subject=Hi & text=Knock-Knock`

<br>
####5. Constant Form Parameters

Form parameters which remain the same for every invocation can be declared as constants using `@FormParams` and `@Param`.   

{% highlight java %}
@POST("/emails/send")
@FormParams({@Param(name = "from", value = "sahan@example.com"),
             @Param(name = "to", value = "support@example.com")})
void contactSupport(@FormParams Map<String, String> params);

...

Map<String, String> params = new LinkedHashMap<String, String>();
params.put("subject", "Hi");
params.put("text", "Knock-Knock");

endpoint.contactSupport(params);
{% endhighlight %}
> `request-body: from=sahan@example.com & to=support@example.com & subject=Hi & text=Knock-Knock`

<br>
####6. Batch Form Parameters

Like the query string, a form-url-encoded string may contain key-value pairs with same key but different values. These batch form parameters are identified with `@FormParams` on a map of type `Map<String, Collection<CharSequence>>`.   

{% highlight java %}
@POST("/emails/send")
void sendEmail(@FormParams Map<String, String> params);

...

List<String> ccValues = new ArrayList<String>();
values.add("one@example.com");
values.add("two@example.com");
values.add("three@example.com");

Map<String, String> params = new LinkedHashMap<String, Object>();
params.put("cc", ccValues);

...

endpoint.sendEmail(params);
{% endhighlight %}
> `request-body: ... cc=one@example.com & cc=two@example.com & cc=three@example.com`

<span id="5"></span>
<br><br>
<h3>Sending a Request Body</h3>

The body of a request can be used to send content expected by the remote service. This is quite common in RESTful services where the targeted resource is communicated between the server and the client using the request and response bodies.   

<br>
####1. Filling a Request Body

To send a plain `CharSequence` within the request body, simply annotate the argument with `@Entity`.   

{% highlight java %}

@POST("/user/emails")
void addEmail(@Entity String email);

...

gitHubEndpoint.addEmail("sahan@example.com");
{% endhighlight %}
> `request-body: sahan@example.com`

<br>
####2. Apache HC HTTP Entities

[Apache HC](http://hc.apache.org/httpcomponents-client-ga) refers to a request with a body as an [`HttpEntityEnclosingRequest`](http://developer.android.com/reference/org/apache/http/HttpEntityEnclosingRequest.html). Such a request may contain an implementation of [`HttpEntity`](http://developer.android.com/reference/org/apache/http/HttpEntity.html) which provides the body content. A request definition may specify an `HttpEntity` directly by annotating it with `@Entity`.   

{% highlight java %}

@POST("/user/emails")
void addEmail(@Entity HttpEntity email);

...

gitHubEndpoint.addEmail(new StringEntity("sahan@example.com"));
{% endhighlight %}
> `request-body: sahan@example.com`

<br>
####3. Request Entity Translation

If `@Entity` does not annotate an `HttpEntity`, the annotated argument will be translated to an appropriate implementation of `HttpEntity` based on its type. This translation proceeds as outlined below.   

<table class="table table-bordered">
<th>Order</th><th>Argument Type</th><th>Translated Entity</th>
<tr><td>1</td><td><code>byte[]</code>, <code>Byte[]</code></td> <td><code><a href="http://developer.android.com/reference/org/apache/http/entity/ByteArrayEntity.html">ByteArrayEntity</a></code></td></tr>
<tr><td>2</td><td><code>java.io.File</code></td> <td><code><a href="http://developer.android.com/reference/org/apache/http/entity/FileEntity.html">FileEntity</a></code></td></tr>
<tr><td>3</td><td><code>java.io.InputStream</code></td> <td><code><a href="http://developer.android.com/reference/org/apache/http/entity/BufferedHttpEntity.html">BufferedHttpEntity</a></code></td></tr>
<tr><td>4</td><td><code>CharSequence</code></td> <td><code><a href="http://developer.android.com/reference/org/apache/http/entity/StringEntity.html">StringEntity</a></code></td></tr>
<tr><td>5</td><td><code>java.io.Serializable</code></td> <td><code><a href="http://developer.android.com/reference/org/apache/http/entity/SerializableEntity.html">SerializableEntity</a></code></td></tr>
</table>

<br>
{% highlight java %}

@PUT("/documents/new")
void putDocument(@Entity File document);

...

endpoint.putDocument(new File(documentUri));
{% endhighlight %}
> `request-body: <contents of file>`

<span id="6"></span>
<br><br>
<h3>Serializing Request Content</h3>

A request body may carry a payload in a format which is expected by the server. Common **standardized** formats are defined by [Internet Media Types](http://en.wikipedia.org/wiki/Internet_media_type) (also known as MIME types or Content-Types). These are recognized by a special identifier written as `type/subtype`, for example `application/json`, `application/xml`. A serializer is responsible for converting a given model (the payload) to an expected format which might be standardized or customized.   

<br>
####1. Attaching a Serializer

Serializers are attached using an `@Serialize` annotation which takes the `ContentType` to identify the proper serializer to be used. This annotation can be placed either on an endpoint or a request. If placed on an endpoint, the serializer is used for all requests defined on the endpoint. If a specific request requires an alternate serializer, the root definition can be overridden with another `@Serialize` annotation on the request.   

{% highlight java %}
import static com.lonepulse.robozombie.annotation.Entity.ContentType.JSON;
import static com.lonepulse.robozombie.annotation.Entity.ContentType.XML;

@Serialize(JSON)
@Endpoint("https://example.com")
public interface ExampleEndpoint {

    @PUT("/json")
    void putJsonContent(@Entity Content content);
    
    @PUT("/xml")
    @Serialize(XML)
    void putXmlContent(@Entity Content content);
}
{% endhighlight %}

<br>
####2. Using Prefabricated Serializers

Out-of-the-box serializers are provided for the content-types `text/plain`, `application/json` and `application/xml`. These can be used for plain text, JSON or XML content respectively.   

> Note that [Gson](http://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.2.4/gson-2.2.4.jar) 
is required for JSON serialization and [Simple-XML](http://search.maven.org/remotecontent?filepath=org/simpleframework/simple-xml/2.7.1/simple-xml-2.7.1.jar) 
is required for XML serialization. If these two libraries are not detected on the build-path, the JSON and XML serializers will be disabled and any usage will result in an informative warning.   

<br>
Serializers for `application/json` and `application/xml` work by converting a model into a JSON or XML `String`. For JSON, unless a custom [field naming policy](https://sites.google.com/site/gson/gson-user-guide#TOC-JSON-Field-Naming-Support) is used, the variable names of the class will be used in the JSON string. Likewise for XML, if [explicit element and attribute naming](http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php) is not used, the variable names of the class will be used for the XML elements.   

{% highlight java %}
public class Issue {

    private String title;
    private String body;
    private String assignee;
    private List<String> labels;

    ...
}

@Serialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @POST("/repos/sahan/RoboZombie/issues")
    void postIssue(@Entity Issue issue);
}

...

Issue issue = new Issue();
issue.setTitle("Update Wiki");
issue.setBody("Revise wiki pages.")
issue.setAssignee("sahan");
issue.setLabels(Arrays.asList("documentation", "backlog"));

gitHubEndpoint.postIssue(issue);
{% endhighlight %}
> `request-body: {"title": "Update Wiki", "body": "Revise wiki pages.", "assignee": "sahan", "labels": ["documentation", "backlog"]}`

<br>
Attaching a serializer for `text\plain` via `@Serialize(PLAIN)` will simply invoke `String.valueOf(...)` on the given model. Use `@Serialize(PLAIN)` on requests when you need to override a serializer attached at the endpoint level.   

{% highlight java %}
@Serialize(JSON)
@Endpoint("https://example.com")
public interface ExampleEndpoint {

    @PUT("/json")
    void putModel(@Entity Content content);
    
    @PUT("/json")
    @Serialize(PLAIN)
    void putJson(@Entity StringBuilder json);
}
{% endhighlight %}

<br>
####3. Creating Custom Serializers

If an out-of-the-box serializer is not a good match, you're free to create your own custom serializer by extending `AbstractSerializer` and overriding `serialize(...)`. This extension should be parameterized to the **input and output types** and the **default constructor** should be exposed which calls the super constructor with the `Class` of the output type.   

Note that all serializers exist as **singletons** and are expected to be **thread-safe**. If an incurred state affects thread-safety, ensure that proper thread-local management is performed.   

{% highlight java %}
import org.apache.http.util.EntityUtils;

import some.third.party.lib.Heap;

final class HeapSerializer extends AbstractSerializer<Heap, String> {

    public HeapSerializer() {

        super(String.class);
    }

    @Override
    protected String serialize(InvocationContext context, Heap heap) {
        
        return heap.deflate();
    }
}
{% endhighlight %}

You might even extend `AbstractSerializer` to create your own JSON serializer with a custom `Gson` instance.

{% highlight java %}
import some.third.party.lib.Model;

final class CustomJsonSerializer extends AbstractSerializer<Object, String> {

    private final Gson gson;

    public CustomJsonSerializer() {

        super(String.class);

        this.gson = new GsonBuilder()
        .serializeNulls()
        .excludeFieldsWithModifier(Modifier.STATIC)
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .registerTypeAdapter(Model.class, new InstanceCreator<Model>() {

            @Override
            public Model createInstance(Type type) {

                return Model.newInstance(); //default constructor unavailable
            }
        })
        .create();
    }

    @Override
    protected String serialize(InvocationContext context, Object input) {
        
        Type type = TypeToken.get(input.getClass()).getType();
        return gson.toJson(input, type);
    }
}
{% endhighlight %}

Custom serializers are attached by specifying their `Class` on the *type* property, e.g. `@Serialize(type = CustomSerializer.class)`.   

{% highlight java %}

@Endpoint("https://example.com")
@Serialize(type = CustomJsonSerializer.class)
public interface ExampleEndpoint {

    @PUT("/json")
    void putJsonContent(@Entity Content content);
    
    @PUT("/heap")
    @Serialize(type = HeapSerializer.class)
    void putHeap(@Entity Heap heap);
}
{% endhighlight %}

<span id="7"></span>
<br><br>
<h3>Receiving a Response Body</h3>

The body of a response may contain the results of a request invocation. This is quite common in RESTful services where the targeted resource is communicated between the server and the client using the request and response bodies.   

<br>
####1. Extracting a Response Body

To extract the body of a response as a plain `String`, simple define the return type of the method as a `CharSequence`.

{% highlight java %}
@GET("/users/{user}")
String getUser(@PathParam("user") String user);

...

String response = gitHubEndpoint.getUser("sahan");
{% endhighlight %}
> `response: { "login": "sahan", "id": 1979186, "avatar_url": ... }`

<br>
####2. Ignoring a Response Body

If you have no use for the response body and wish to simply ignore it, define the return type of the method as `void` or `Void`.

{% highlight java %}

@POST("/user/emails")
void addEmail(@Entity String email);

...

gitHubEndpoint.addEmail("sahan@example.com");
{% endhighlight %}
> `response-body (ignored): sahan@example.com`

<br>
####3. Receiving `HttpResponse` and `HttpEntity`

If the return type is defined as `HttpResponse`, no special action will taken to process the response body. The response given by Apache HC will be returned as it is. Likewise, if the return type is defined as `HttpEntity`, the raw `HttpEntity` contained within the `HttpResponse` is returned. Refer the documentation on [`HttpResponse`](http://developer.android.com/reference/org/apache/http/HttpResponse.html) and [`HttpEntity`](http://developer.android.com/reference/org/apache/http/HttpEntity.html) for more information.    

{% highlight java %}
@GET("/users/{user}")
HttpResponse getUser(@PathParam("user") String user);

@GET("/users/{user}/gists")
HttpEntity getGists(@PathParam("user") String user);

...

HttpResponse response = gitHubEndpoint.getUser("sahan");
HttpEntity entity = gitHubEndpoint.getGists("sahan");
{% endhighlight %}
> `response.getEntity(): { "login": "sahan", "id": 1979186, "avatar_url": ... }`   
> `entity: [ { "url": "https://api.github.com/gists/352cfb... ]`

<br>
####4. Status Codes `204` and `205`

Responses with the status codes `204` and `205` represent a **success status** but do not return any content. A brief summary of these status codes are given below.   

<table class="table table-bordered">
<th>Code</th><th>Name</th><th>Description</th>
<tr>
<td>204</td><td>No Content</td><td>Response content should not be expected, e.g. a DELETE request.</td>
</tr>
<tr>
<td>205</td><td>Reset Content</td><td>Instructs a reset on the client, e.g. to clear the submission form.</td>
</tr>
</table>

Unless the return type is `void` or `Void`, a request which receives a response with either of these codes will return `null`.

<span id="8"></span>
<br><br>
<h3>Deserializing Response Content</h3>

The response body may contain content in numerous formats identified by [content-types](http://en.wikipedia.org/wiki/Internet_media_type). A deserializer is responsible for parsing the response content into model which can be readily consumed by your application code.   

<br>
####1. Attaching a Deserializer

Deserializers are attached using an `@Deserialize` annotation which takes the `ContentType` to identify the proper deserializer to be used. This annotation can be placed either on an endpoint or a request. If placed on an endpoint, the deserializer is used for all requests defined on the endpoint. If a specific request requires an alternate deserializer, the root definition can be overridden with another `@Deserialize` annotation on the request.   

{% highlight java %}
import static com.lonepulse.robozombie.annotation.Entity.ContentType.JSON;
import static com.lonepulse.robozombie.annotation.Entity.ContentType.XML;

@Deserialize(JSON)
@Endpoint("https://example.com")
public interface ExampleEndpoint {

    @GET("/json")
    Content getJsonContent();
    
    @GET("/xml")
    @Deserialize(XML)
    Content getXmlContent();
}
{% endhighlight %}

<br>
####2. Using Prefabricated Deserializers

Out-of-the-box deserializers are provided for the content-types `text/plain`, `application/json` and `application/xml`. These can be used for plain text, JSON or XML content respectively.   

> Note that [Gson](http://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.2.4/gson-2.2.4.jar) 
is required for JSON deserialization and [Simple-XML](http://search.maven.org/remotecontent?filepath=org/simpleframework/simple-xml/2.7.1/simple-xml-2.7.1.jar) 
is required for XML deserialization. If these two libraries are not detected on the build-path, the JSON and XML deserializers will be disabled and any usage will result in an informative warning.   

<br>
Deserializers for `application/json` and `application/xml` work by converting the response body into a model. The model will be an object of a class which represents the JSON or XML. For JSON, unless a [field naming policy](https://sites.google.com/site/gson/gson-user-guide#TOC-JSON-Field-Naming-Support) is used, the variable names of the class should be equal to that of the JSON object. Likewise for XML, if the element names aren't equal to the class variables, [explicit element and attribute naming](http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php) can be used.   

{% highlight java %}
public class Repo {

    private String id;
    private String name;
    private boolean fork;
    private int stargazers_count;

    ...
}

@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {


    @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user);
}

...

List<Repo> repos = gitHubEndpoint.getRepos("sahan");
{% endhighlight %}
> `response-body: [{ "id": 7576372, "name": "RoboZombie", "fork": false, ... ]`   

<br>
Attaching a deserializer for `text\plain` via `@Deserialize(PLAIN)` will give you the raw response body. This is essentially the same as defining the method return type as a `CharSequence`. Use `@Deserialize(PLAIN)` on requests when you need to override a deserializer attached at the endpoint level.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://example.com")
public interface ExampleEndpoint {

    @GET("/json")
    Content getModel();
    
    @GET("/json")
    @Deserialize(PLAIN)
    String getJson();
}
{% endhighlight %}

<br>
####3. Creating Custom Deserializers

If an out-of-the-box deserializer is not a good match, you're free to create your own custom deserializer by extending `AbstractDeserializer` and overriding `deserialize(...)`. This extension should be parameterized to the **output type** and the **default constructor** should be exposed which calls the super constructor with the `Class` of the output type.   

Note that all deserializers exist as **singletons** and are expected to be **thread-safe**. If an incurred state affects thread-safety, ensure that proper thread-local management is performed.   

{% highlight java %}
import org.apache.http.util.EntityUtils;

import some.third.party.lib.Document;

final class DocumentDeserializer extends AbstractDeserializer<Document> {

    public DocumentDeserializer() {

        super(Document.class);
    }

    @Override
    protected Document deserialize(InvocationContext context, HttpResponse response) {
        
        String customFormattedContent = EntityUtils.toString(response.getEntity());
        return Document.parse(customFormattedContent);
    }
}
{% endhighlight %}

You might even extend `AbstractDeserializer` to create your own JSON deserializer with a custom `Gson` instance.

{% highlight java %}
import some.third.party.lib.Model;

final class CustomJsonDeserializer extends AbstractDeserializer<Object> {

    private final Gson gson;

    public CustomJsonDeserializer() {

        super(Object.class);

        this.gson = new GsonBuilder()
        .serializeNulls()
        .excludeFieldsWithModifier(Modifier.STATIC)
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .registerTypeAdapter(Model.class, new InstanceCreator<Model>() {

            @Override
            public Model createInstance(Type type) {

                return Model.newInstance(); //default constructor unavailable
            }
        })
        .create();
    }

    @Override
    protected Object deserialize(InvocationContext context, HttpResponse response) {
        
        String json = EntityUtils.toString(response.getEntity());
        Type type = TypeToken.get(context.getRequest().getReturnType()).getType();

        return gson.fromJson(json, type);
    }
}
{% endhighlight %}

Custom deserializers are attached by specifying their `Class` on the *type* property, e.g. `@Deserialize(type = CustomDeserializer.class)`.   

{% highlight java %}

@Endpoint("https://example.com")
@Deserialize(type = CustomJsonDeserializer.class)
public interface ExampleEndpoint {

    @GET("/json")
    Content getJsonContent();
    
    @GET("/document")
    @Deserialize(type = DocumentDeserializer.class)
    Document getDocument();
}
{% endhighlight %}

<span id="9"></span>
<br><br>
<h3>Sending and Receiving Headers</h3>

A request or response contains headers which are used to relay meta-information and control the communication between the client and sever. Such a header could be a [predefined standard](http://en.wikipedia.org/wiki/List_of_HTTP_header_fields) or a [custom header](http://www.ietf.org/rfc/rfc2047.txt) prefixed with `X-`.   

<br>
####1. Sending Request Headers

A request header is identified using the `@Header` annotation with the header name placed on a parameter of type `CharSequence`. The header value can be provided as an argument when the request is being invoked.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    List<Repo> getRepos(@Header("Accept") String header);
}

...

List<Repo> repos = gitHubEndpoint.getRepos("application/vnd.github.v3");
{% endhighlight %}
> `request-headers: "Accept: application/vnd.github.v3"`   

To skip sending a defined request header, simply pass `null` or `""`.

{% highlight java %}
gitHubEndpoint.getRepos(null);   /* or */   gitHubEndpoint.getRepos("");
{% endhighlight %}
> `request-headers: <none>`   

<br>
####2. Defining Static Request Headers

Certain requests might require a header whose value will remain constant across all invocations. Such headers are considered to be *static* and can be defined using `@Headers` and `@Headers.Header`.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    @Headers({@Headers.Header(name = "User-Agent", value = "MyGitHubApp-v1"),
              @Headers.Header(name = "Accept", value = "application/vnd.github.v3")})
    List<Repo> getRepos(@PathParam("user") String user);
}
{% endhighlight %}
> `request-headers: "User-Agent: MyGitHubApp-v1, Accept: application/vnd.github.v3"`

<br>
####3. Extracting Response Headers

Response headers are identified the same way request headers are using an `@Header` annotation. However, the parameter type on which the annotation is applied should be an instance of `StringBuilder`.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user, 
                        @Header("X-RateLimit-Remaining") StringBuilder responseHeader);
}

...

StringBuilder responseHeader = new StringBuilder();

List<Repo> repos = gitHubEndpoint.getRepos(responseHeader);

String rateLimitRemaining = responseHeader.toString();
{% endhighlight %}
> `response-headers: "X-RateLimit-Remaining: 4950"`   
> `rateLimitRemaining: 4950`

<span id="10"></span>
<br><br>
<h3>Executing Requests Asynchronously</h3>

A request execution is a blocking operation and multiple requests can only be executed sequentially. For asynchronous execution you could rely on your own threading mechanisms or defer to using `@Async` with an `AsyncHandler`. This will allow you to execute several requests in parallel and handle the results once the response is available.   

<br>
####1. Identifying Asynchronous Requests

The `@Async` annotation can be placed on an endpoint to execute all requests asynchronously or on a specific request definition to selectively identify asynchronous requests.   

{% highlight java %}
@Async @Endpoint("https://example.com")
public interface SeussEndpoint {

    @DELETE("/thing1")
    void anAsyncRequest();

    @DELETE("/thing2")
    void anotherAsyncRequest();
}
{% endhighlight %}

or...

{% highlight java %}
@Endpoint("https://example.com")
public interface SeussEndpoint {

    @Async @DELETE("/thing1")
    void anAsyncRequest();

    @DELETE("/thing2")
    void notAnAsyncRequest();
}
{% endhighlight %}

> No response content is expected to be handled for the above requests.   

<br>
####2. Defining Callbacks for Responses

To process the resulting response of an asynchronous request, an `AsyncHandler` must be provided as an argument. **The method return-type is still inspected for deserialization.** The parameterized `AsyncHandler` type should be assignable from the method return-type.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @Async @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user, AsyncHandler<List<Repo>> asyncHandler);
}
{% endhighlight %}
> `getRepos(...)` will always return `null`.

<br>
####3. Response Handling Scenarios

The typical scenario is to execute an asynchronous request and handle a **successful** response. A response is deemed successful if it contains a status code of type `2xx`. For such responses the `onSuccess(...)` callback of an `AsyncHandler` will be invoked.   

{% highlight java %}
gitHubEndpoint.getRepos("sahan", new AsyncHandler<List<Repo>>() {
            
    @Override
    public void onSuccess(HttpResponse response, List<Repo> repos) {
        
        doSomethingUseful(repos);
    }
});
{% endhighlight %}
> `onSuccess(...)` is the only callback which is mandated to be overridden.   

If the status code is not of type `2xx`, the request is considered to have failed and the `onFailure(...)` callback is invoked.   

{% highlight java %}
import org.apache.http.util.EntityUtils;

...

gitHubEndpoint.getRepos("sahan", new AsyncHandler<List<Repo>>() {
            
    @Override
    public void onSuccess(HttpResponse response, List<Repo> repos) {
        
        doSomethingUseful(repos);
    }

    @Override
    public void onFailure(HttpResponse response) {
                
        String details = EntityUtils.toString(response.getEntity());
        logAndAlertFailure(details);
    }
});
{% endhighlight %}

If an error occurred while creating the request and executing it or while processing the response, the `onError(...)` callback will be invoked. This callback will provide you a context-aware `InvocationException`. Use `getContext()` to retrieve the `InvocationContext` which can be used to examine the request information and metadata. If the error occurred while processing the response, an `HttpResponse` should be available. Invoke `hasResponse()` to check the availability of an `HttpResponse` and invoke `getResponse()` to retrieve it.   

{% highlight java %}
gitHubEndpoint.getRepos("sahan", new AsyncHandler<List<Repo>>() {
            
    @Override
    public void onSuccess(HttpResponse response, List<Repo> repos) {
        
        doSomethingUseful(repos);
    }

    @Override
    public void onError(InvocationException errorContext) {
                
        InvocationContext context = errorContext.getContext();
                
        if(errorContext.hasResponse()) {
                    
            HttpResponse response = errorContext.getResponse();
            logAndAlertError(context, response);
        }
        else {
                    
            logAndAlertError(context);
        }
    }
});
{% endhighlight %}
> Clearly, the `hasResponse()` method doubles as an indicator to distinguish between a request creation/execution error vs a response processing error.

<br>
####4. Reading Response Headers

Extracting response headers remains the same with asynchronous requests, with the exception that it should be performed within a callback.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @Async @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user, 
                        @Header("X-RateLimit-Remaining") StringBuilder header, 
                        AsyncHandler<List<Repo>> asyncHandler);
}

...

final StringBuilder responseHeader = new StringBuilder();

gitHubEndpoint.getRepos("sahan", responseHeader, new AsyncHandler<List<Repo>>() {
            
    @Override
    public void onSuccess(HttpResponse response, List<Repo> repos) {
        
        String rateLimitRemaining = responseHeader.toString();
        doSomethingUseful(repos, rateLimitRemaining);
    }
});
{% endhighlight %}

<span id="11"></span>
<br><br>
<h3>Creating Stateful Endpoints</h3>

By default, the execution of a request does not consult an HTTP context and does not maintain a stateful conversation with the server. This can be changed by placing the `@Stateful` annotation on the endpoint. `@Stateful` directs request execution to use an [`BasicHttpContext`](http://developer.android.com/reference/org/apache/http/protocol/BasicHttpContext.html) with a [`BasicCookieStore`](http://developer.android.com/reference/org/apache/http/impl/client/BasicCookieStore.html) which provides [session management](http://hc.apache.org/httpcomponents-client-ga/tutorial/html/statemgmt.html) via **cookies**.   

{% highlight java %}
@Stateful
@Endpoint("http://example.com")
public interface ExampleEndpoint {

    @GET("/rememberme")
    void rememberMe();
}

...

endpoint.rememberMe(); //1. Creates a new session.
endpoint.rememberMe(); //2. Identifies an existing session.
{% endhighlight %}
> `1. response-header: "Set-Cookie: JSESSIONID=123456789"`   
> `2. request-header: "Cookie: JSESSIONID=123456789"`

<span id="12"></span>
<br><br>
<h3>Intercepting Requests</h3>

Since the prime objective is to provide a higher level of abstraction over tedious network programming, the underlying [Apache HC](http://hc.apache.org) code is rarely exposed. Nonetheless, it's wise to acknowledge the fact that sometimes you may want to reach **under the hood**. This can be achieved using an `Interceptor`.   

Every invocation goes through a processor chain which builds the request before execution. An `Interceptor` will allow you to hook into the very end of the request processor chain and manipulate the request just prior to execution.   

<table>
<tr>
<td><pre>URI</pre></td><td>&rarr;</td><td><pre>Headers</pre></td><td>&rarr;</td><td><pre>Query</pre></td><td>&rarr;</td><td><pre>Form</pre></td><td>&rarr;</td><td><pre>Entity</pre></td><td>&rarr;</td><td><pre><font color="#FA5858">Intercept</font></pre></td><td>&rarr;</td><td><b>EXECUTE</b></td>
</tr>
</table>
> The request **processor chain** depicting the **sequence** of processors and **point of interception** before request execution.   

<br>
####1. Creating Interceptors

Every interceptor is an implementation of `Interceptor` where the `intercept(...)` method is expected to process the request before execution. The `InvocationContext` which is available within `intercept(...)` supplies all the information about the current invocation.   

Note that all interceptors exist as **singletons** and are expected to be **thread-safe**. If an incurred state affects thread-safety, ensure that proper thread-local management is performed.   

{% highlight java %}
public class DebugInterceptor implements Interceptor {

    @Override       
    public void intercept(InvocationContext context, HttpRequestBase request) {
    
        Log.d("MyApp-v1", request.getRequestLine().toString());
    }
}
{% endhighlight %}

<br>
####2. Attaching Interceptors

Multiple interceptors can be attached to either a single request or an entire endpoint. Use `@Intercept` with the `Class` of the `Interceptor`.   

{% highlight java %}
@Endpoint("http://example.com")
@Intercept(DebugInterceptor.class)
public interface ExampleEndpoint {

    @GET("/document")
    @Intercept(CensorInterceptor.class)
    String getDocument();

    @PUT("/document")
    @Intercept({GrammarInterceptor.class,
                PlagiarismInterceptor.class})
    void putDocument(@Entity String document);
}
{% endhighlight %}

<br>
####3. Beyond Interception

Interceptors are quite versatile and may prove to be invaluable when used with a little imagination. The code snippet below demonstrates the use of interceptors in **metadata processing** with **user-defined annotations**.

{% highlight java %}
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {}


public class AuthInterceptor implements Interceptor {

    private final AuthManager authManager;

    public AuthInterceptor() {

        this.authManager = new OAuthService();
    }

    @Override
    public void intercept(InvocationContext context, HttpRequestBase request) {

        if(context.getRequest().isAnnotationPresent(Auth.class)) {
        
            CharSequence oAuthHeader = authManager.getOAuthHeader();
            request.addHeader("Authorization", oAuthHeader);
        }
    }
}


@Deserialize(JSON)
@Intercept(AuthInterceptor.class)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @Auth @GET("/user/repos")
    List<Repo> getRepos();

    @GET("/users/{user}/gists")
    List<Gist> getGists(@PathParam("user") String user);
}

...

List<Repo> repos = gitHubEndpoint.getRepos();
{% endhighlight %}
> `request-headers: "Authorization: token abcdef...456789"`   
> Assumes that an unexpired OAuth token is available, presumably fetched via cached credentials.

<span id="13"></span>
<br><br>
<h3>Overriding, Detaching and Skipping Components</h3>

Components such as **serializers**, **deserializers** and **interceptors** attached at the root of the endpoint are common to all requests. If a new request defined on the endpoint requires and an alternate component, you should be able to **override** the root definition. In contrast, if the new request wishes to skip the component, you should be able to **detach** the entire root definition or skip individual components. Without any special provision for overriding, detaching and skipping, component definitions would have to be repeated for every request to which it's attached.   

<br>
####1. Overriding and Detaching Serializers and Deserializers

Serializers and deserializers are attached with the `@Serialize` and `@Deserialize` annotations. Obviously, a single request can only use a single serializer or deserializer. When either of these components are attached to the endpoint, it applies to all requests. If a single request needs to override the root definition, simply reattach the required serializer or deserializer to the request.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}")
    User getUser(@PathParam("user") String user);

    @GET("/users/{user}/gists")
    @Deserialize(type = GistDeserializer.class)
    List<Gist> getGists(@PathParam("user") String user);
}
{% endhighlight %}
> JSON deserializer is overridden by `GistDeserializer`.

To detach the root definition all-together, use the `@Detach` annotation with the `Class` of the annotation used for attaching the component.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}")
    User getUser(@PathParam("user") String user);

    @Detach(Deserialize.class) //detach all components; directive: Deserialize
    @GET("/users/{user}/gists")
    HttpEntity getGists(@PathParam("user") String user);
}
{% endhighlight %}
> JSON deserializer is detached; `getGists()` will not be using any deserializer.

<br>
####2. Detaching and Skipping Interceptors

Interceptors work a tad differently than serializers and deserializers. **There is no concept of interceptor overriding.** The interceptor(s) attached to the endpoint will be **applied together** with any interceptor(s) attached to the request. However, all interceptors, defined on the endpoint or request can be **detached** by using `@Detach`. Individual interceptors defined on the endpoint can be skipped by using `@Skip` on a request.   

{% highlight java %}
@Deserialize(JSON)
@Endpoint("https://api.github.com")
@Intercept({HeaderInterceptor.class, AuthInterceptor.class})
public interface GitHubEndpoint {

    @GET("/user/repos")
    @Intercept(RepoInterceptor.class)
    List<Repo> getRepos();

    @GET("/users/{user}")
    @Skip(AuthInterceptor.class)
    @Intercept(UserInterceptor.class)
    User getUser(@PathParam("user") String user);

    @GET("/emojis")
    @Detach(Intercept.class) //detach all components; directive: Intercept
    String getEmojis();
}
{% endhighlight %}
> `getRepos()` will be using `HeaderInterceptor`, `AuthInterceptor` and `RepoInterceptor`.   
> `getUser()` will be using `HeaderInterceptor` and `UserInterceptor`, but not `AuthInterceptor`.   
> `getEmojis()` will not be using any interceptor.

<br>
####3. Detaching an Async Directive

An `@Async` annotation placed on an endpoint will cause all requests to be executed asynchronously. If a request should be exempted from this rule, the `@Async` directive can be detached with `@Detach`.   

{% highlight java %}
@Async 
@Deserialize(JSON)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    @GET("/users/{user}/repos")
    List<Repo> getRepos(@PathParam("user") String user, AsyncHandler<List<Repo>> asyncHandler);

    @GET("/meta")
    @Detach(Async.class)
    Meta getMetaInfo();
}
{% endhighlight %}

<br>
<br>
Although none of the above examples demonstrate multiple component-type detachment and skipping, keep in mind that this is possible.   

{% highlight java %}
@Detach({Async.class, Intercept.class, Serialize.class, Deserialize.class})
{% endhighlight %}

{% highlight java %}
@Skip({HeaderInterceptor.class, AuthInterceptor.class})
{% endhighlight %}

<span id="14"></span>
<br><br>
<h3>Wiring and Injecting Endpoints</h3>

Once an endpoint contract has been defined, an implementation of that interface should be available to invoke a request. `Zombie` gives you this implementation in the form of a **thread-safe proxy**. This proxy exists a **singleton** and can be injected into your objects with `Zombie.infect(...)`. Wiring a specific endpoint proxy is done using an `@Bite` annotation on a property of the endpoint interface type.   

<br>
####1. Wiring Endpoints
> <font color="#EC6C6C">"...so that the `Zombie` will know where to `Bite`."</font>    

Wiring a specific endpoint proxy is done using an `@Bite` annotation on a property of the endpoint interface type. If multiple endpoint properties are declared, the `@Bite` annotation should be present on each one.   

{% highlight java %}
@Bite
private GitHubEndpoint gitHubEndpoint;
{% endhighlight %}

<br>
####2. Property Injection
> <font color="#EC6C6C">"...getting the `Zombie` to spread the infection."</font>   

To inject a proxy directly into the property, use the `@Bite` annotation for wiring the proper type and invoke `Zombie.infect(...)` on the object instance. Property injection works with all access modifiers provided that a security manager does not restrict this.   

{% highlight java %}
@Bite
private GitHubEndpoint gitHubEndpoint;
{
    Zombie.infect(this);
}
{% endhighlight %}

This works for multiple endpoints as well.   

{% highlight java %}
@Bite
private GitHubEndpoint gitHubEndpoint;

@Bite
protected ExampleEndpoint exampleEndpoint;

{
    Zombie.infect(this);
}
{% endhighlight %}

If the target object has an inheritance hierarchy with multiple endpoints at different levels, a single *infection* is all it takes.   

{% highlight java %}
public class Parent {

    @Bite
    private GitHubEndpoint gitHubEndpoint;

    ...
}

public class Child extends Parent {

    @Bite
    private ExampleEndpoint exampleEndpoint;
    {
        Zombie.infect(this);
    } 

    ...
}
{% endhighlight %}
> Both endpoints will be injected.    

<br>
####3. Setter Injection
> <font color="#EC6C6C">"...avoiding a `Bite`, controlling the infection."</font>   

If a custom [`SecurityManager`](http://docs.oracle.com/javase/6/docs/api/java/lang/SecurityManager.html) disallows clearing access restriction or if you need a callback at the point of injection, a setter can be declared. **However, endpoint properties whose access modifier is *public* will always be injected directly regardless of the presence of a setter.**   

{% highlight java %}
public class BasicGitHubService implements GitHubService {

    @Bite
    private GitHubEndpoint gitHubEndpoint;
    {
        Zombie.infect(this);
    }

    public void setGitHubEndpoint(GitHubEndpoint gitHubEndpoint) {

        if(this.gitHubEndpoint == null) {

            this.gitHubEndpoint = gitHubEndpoint;
        }
    }
}

...

GitHubService gitHubService = new BasicGitHubService();

Zombie.infect(gitHubService);
{% endhighlight %}
> Injection will be performed through the setter. The second `Bite` will have no effect.   

<br>
####4. Confining Hierarchical Searches
> <font color="#EC6C6C">"...searching for victims that matter."</font>   

An invocation of `Zombie.infect(...)` on an object will search the entire inheritance hierarchy for wired endpoints until a type is reached whose package name is prefixed with `android.`, `java.`, `javax.` or `junit.`. The packages to scan for wired endpoints can be explicitly restricted by providing a list of **package prefixes**.   

{% highlight java %}
package com.example.manager;

import some.third.party.library.ThirdPartyActivity; //an extension of android.app.Activity

public class ActivityManager extends ThirdPartyActivity {

    @Bite
    private GitHubEndpoint gitHubEndpoint;

    ...
}
{% endhighlight %}
{% highlight java %}
package com.example.controller;

import com.example.manager.ActivityManager;

public class BasicActivity extends ActivityManager {

    @Bite
    private ExampleEndpoint exampleEndpoint;
    {
        Zombie.infect(this); //1.
        Zombie.infect("com.example.", this); //2.
        Zombie.infect("com.example.service.", this); //3.
        Zombie.infect(Arrays.asList("com.example", "some.third.party."), this); //4.
    }

    ...
}
{% endhighlight %}
> 1. Searches the entire inheritance hierarchy till `android.app.Activity`.   
> 2. Only `BasicActivity` and `ActivityManager` will be searched.   
> 3. Only `BasicActivity` will be searched; `gitHubEndpoint` will be `null`.   
> 4. `BasicActivity`, `ActivityManager` and `ThirdPartyActivity` will be searched.   

<br>
<br>
Although none of the above examples demonstrate **mass infection**, keep in mind that this is possible.   

{% highlight java %}
Zombie.infect(victim1, victim2, victim3);
{% endhighlight %}
> Three object instances injected with a single invocation.   

<span id="15"></span>
<br><br>
<h3>Configuring RoboZombie</h3>

Every endpoint uses an existing configuration for request execution. Such a configuration usually pertains to the settings applied on [Apache HC](http://hc.apache.org). Familiarization with Apache HC should be a prerequisite for building a custom configuration.   

<br>
The default configuration employs a [DefaultHttpClient](http://developer.android.com/reference/org/apache/http/impl/client/DefaultHttpClient.html) which uses a [ThreadSafeClientConnManager](http://developer.android.com/reference/org/apache/http/impl/conn/tsccm/ThreadSafeClientConnManager.html) with the following parameters.   

<table class="table table-bordered">
<th>Parameter</th><th>Configuration</th>
<tr><td>Redirecting</td><td>ENABLED</td></tr>
<tr><td>Connection Timeout</td><td>30 seconds</td></tr>
<tr><td>Socket Timeout</td><td>30 seconds</td></tr>
<tr><td>Socket Buffer Size</td><td>12000 bytes</td></tr>
<tr><td>User-Agent</td><td>System.getProperty("http.agent")</td></tr>
</table>

<br>
Additionally, two schemes are registered.   

<table class="table table-bordered">
<th>Scheme</th><th>Port</th><th>Socket Factory</th>
<tr><td>HTTP</td><td>80</td><td><a href="http://developer.android.com/reference/org/apache/http/conn/scheme/PlainSocketFactory.html">PlainSocketFactory</a></td></tr>
<tr><td>HTTPS</td><td>443</td><td><a href="http://developer.android.com/reference/org/apache/http/conn/ssl/SSLSocketFactory.html">SSLSocketFactory</a></td></tr>
</table>

<br>
A configuration can be tailored for a specific endpoint by creating an implementation of `Zombie.Configuration`. Currently the only configurable property is the `HttpClient`. Once a configuration is created, it can be applied on an endpoint using the `@Config` annotation by specifying the `Class` of the implementation.   

{% highlight java %}
import android.net.http.AndroidHttpClient;

public class GitHubConfig extends Zombie.Configuration {

    @Override
    public HttpClient httpClient() {
        
        HttpClient httpClient = AndroidHttpClient.newInstance("MyGitHubApp-v1");

        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
        HttpConnectionParams.setSoTimeout(params, 10 * 1000);
                    
        return httpClient;
    }
}

@Config(GitHubConfig.class)
@Endpoint("https://api.github.com")
public interface GitHubEndpoint {

    ...
}
{% endhighlight %}
> Demonstrates the use of `AndroidHttpClient` instead of a `DefaultHttpClient`.   

You can tweak the preconfigured `DefaultHttpClient` as well.   

{% highlight java %}
public class TweakedConfig extends Zombie.Configuration {

    @Override
    public HttpClient httpClient() {
        
        HttpClient httpClient = super.httpClient();
        HttpProtocolParams.setUserAgent(httpClient.getParams(), "MyGitHubApp-v1");
                    
        return httpClient;
    }
}
{% endhighlight %}
