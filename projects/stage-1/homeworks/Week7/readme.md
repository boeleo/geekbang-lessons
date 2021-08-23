# 作业

描述 Spring 校验注解 `org.springframework.validation.annotation.Validated` 的工作原理，它与 Spring Validator 以及 JSR-303 Bean Validation @javax.validation.Valid 之间的关系。

## 作答

1. org.springframework.validation.annotation.Validated 的 Java Doc 注释

```
Variant of JSR-303's {@link javax.validation.Valid}, supporting the specification of validation groups. Designed for convenient use with Spring's JSR-303 support but not JSR-303 specific.
```

作为 JSR-303 中 @javax.validation.Valid 的变体，支持 validation groups 的规范。专门为方便使用 Spring JSR-303 支持而设计，但不特定于 JSR-303。

```
Can be used e.g. with Spring MVC handler methods arguments. Supported through {@link org.springframework.validation.SmartValidator}'s validation hint concept, with validation group classes acting as hint objects.
```

可用于注解，例如 Spring MVC handler 方法的参数。通过 SmartValidator 的验证提示概念支持，与验证组类充当提示对象。

```
Can also be used with method level validation, indicating that a specific class is supposed to be validated at the method level (acting as a pointcut for the corresponding validation interceptor), but also optionally specifying the validation groups for method-level validation in the annotated class. 
```

也可以与方法级验证一起使用，表示应该在方法级验证特定的类（充当相应验证拦截器的切入点），但也可以选择在注释中指定方法级验证的验证组类。

```
Applying this annotation at the method level allows for overriding the validation groups for a specific method but does not serve as a pointcut; a class-level annotation is nevertheless necessary to trigger method validation for a specific bean to begin with. Can also be used as a meta-annotation on a custom stereotype annotation or a custom group-specific validated annotation.
```

在方法级别应用此注解允许覆盖特定方法的验证组，但不用作切入点；尽管如此，类级别的注释对于触发特定 bean 的方法验证是必要的。也可以用作自定义构造型注释或自定义组特定验证注释的元注释。
