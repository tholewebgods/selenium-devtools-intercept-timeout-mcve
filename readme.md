
# MCVE to reproduce timeout exception when using Devtools' intercept

## Code

```
Route route = Route.matching(req -> GET == req.getMethod() && req.getUri().contains("/api/properties"))
    .to(() -> req -> new HttpResponse()
        .setStatus(responseCode)
        .addHeader("Content-Type", "application/json")
        .addHeader("-x-e2e-dev-tools-override", "mockPropertiesGetResponse")
        .setContent(utf8String(responseBody)));

new NetworkInterceptor(driver, route);
```

## Error

```
    at org.openqa.selenium.devtools.Connection$Listener.lambda$onText$0(Connection.java:202)
    at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
    at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
    at java.base/java.lang.Thread.run(Thread.java:829)
Caused by: org.openqa.selenium.TimeoutException: java.util.concurrent.TimeoutException
Build info: version: '4.1.3', revision: '7b1ebf28ef'
System info: host: 'h001287568', ip: '192.168.122.1', os.name: 'Linux', os.arch: 'amd64', os.version: '4.15.0-191-generic', java.version: '11.0.16'
Driver info: driver.version: unknown
    at org.openqa.selenium.devtools.Connection.sendAndWait(Connection.java:161)
    at org.openqa.selenium.devtools.DevTools.send(DevTools.java:70)
    at org.openqa.selenium.devtools.idealized.Network.lambda$prepareToInterceptTraffic$4(Network.java:216)
    at org.openqa.selenium.devtools.Connection.lambda$handle$6(Connection.java:288)
    at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
    at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:177)
    at java.base/java.util.stream.ReferencePipeline$11$1.accept(ReferencePipeline.java:442)
    at java.base/java.util.HashMap$KeySpliterator.forEachRemaining(HashMap.java:1621)
    at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
    at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
    at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
    at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
    at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
    at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
    at org.openqa.selenium.devtools.Connection.handle(Connection.java:257)
    at org.openqa.selenium.devtools.Connection.access$200(Connection.java:58)
    at org.openqa.selenium.devtools.Connection$Listener.lambda$onText$0(Connection.java:199)
    ... 3 more
Caused by: java.util.concurrent.TimeoutException
    at java.base/java.util.concurrent.CompletableFuture.timedGet(CompletableFuture.java:1886)
    at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2021)
    at org.openqa.selenium.devtools.Connection.sendAndWait(Connection.java:150)
    ... 19 more
```


## Possibly related issues

- https://github.com/SeleniumHQ/selenium/issues/9889

