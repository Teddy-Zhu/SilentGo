# SilentGo Shiro
#### How to Use
add maven dependency
```
<dependency>
    <groupId>com.silentgo</groupId>
    <artifactId>shrio</artifactId>
    <version>0.0.2</version>
</dependency>
```
in config class
```java
public class Config implements com.silentgo.core.config.Config {
    @Override
    public void initialBuild(SilentGoConfig config) {
     //enable shiro
     //use default
     config.addExtraInitConfig(new JetbrickInitConfig());
     
     //use properties
     config.addExtraInitConfig(new JetbrickInitConfig(properties));
    }
}

```