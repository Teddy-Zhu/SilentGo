# SilentGo JetBrick
#### How to Use
add maven dependency
```
<dependency>
    <groupId>com.silentgo</groupId>
    <artifactId>jetbrick</artifactId>
    <version>0.0.1</version>
</dependency>
```
in config class
```java
public class Config implements com.silentgo.core.config.Config {
    @Override
    public void initialBuild(SilentGoConfig config) {
     //enable shiro
     //UserRealm your self realm
     config.addExtraInitConfig(new ShiroInitConfig(new UserRealm()));
    }
}

```