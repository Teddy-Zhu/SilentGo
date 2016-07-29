# SilentGo Framework
a light web mvc framework.

####  IOC
use with @Inject  
####  AOP 
class implements Interceptor.
####  route
match path , you can use :   
		
```java
	@Controller(value="/path")  
	@Route(value="/{id}/{name}") or @Route(value="/[0-9a-z]+" , regex = true)
```
match parameters  

```java
	public void index(Response reponse,Resquest request ,@PathVariable(value="id") Integer id ,@PathVariable(value="name") String myname, 
	@RequestParam @RequestString( required = true , range = { 5 , 10}) String name)
```  
validate parameters
	class implements IValidator
	you can custom validator for parameters yourself


