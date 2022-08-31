
# MCVE to reproduce timeout exception when using Devtools' intercept

## Prerequisites

- Podman
- Maven >= 3.6


## How to run the reproducer

### Start

```
grid term $ scripts/start-grid
node term $ scripts/start-node
mcve term $ mvn clean package
mcve term $ java -jar target/selenium-devtools-intercept-mcve-1.0.0.jar 
```

You may have to run the jar file multiple times if it does not show the exception. The exception shall be reproducible in at least 1/3 of runs.


### Expected output

This is what you shall see when the reproducer is able to reproduce the error:

```
...
Info: open youtube
Info: wait for consent dialog
Info: dialog found
Info: close the interceptor
Info: close the driver
Info: sleeping 15s after test is done ...
org.openqa.selenium.TimeoutException: java.util.concurrent.TimeoutException
   < rest of stack trace omitted >
Info: exit.
```


### Cleanup

You may want to remove the automatically created network:

```
grid term $ podman network remove selenium
```


## Error

After the interceptor was closed and the driver was quit, the "CDP Connection" thread throws this error, printed to `stderr`:

```
org.openqa.selenium.TimeoutException: java.util.concurrent.TimeoutException
Build info: version: '4.1.3', revision: '7b1ebf28ef'
System info: host: 'h001287568', ip: '10.208.2.6', os.name: 'Linux', os.arch: 'amd64', os.version: '4.15.0-192-generic', java.version: '11.0.16'
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
    at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
    at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
    at java.base/java.lang.Thread.run(Thread.java:829)
Caused by: java.util.concurrent.TimeoutException
    at java.base/java.util.concurrent.CompletableFuture.timedGet(CompletableFuture.java:1886)
    at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2021)
    at org.openqa.selenium.devtools.Connection.sendAndWait(Connection.java:150)
    ... 19 more
```


## Possibly related issues

- https://github.com/SeleniumHQ/selenium/issues/9889

