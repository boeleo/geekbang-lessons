# 作业

描述 Spring 校验注解 `org.springframework.validation.annotation.Validated` 的工作原理，它与 Spring Validator 以及 JSR-303 Bean Validation @javax.validation.Valid 之间的关系。

## 作答

1. 分析 `org.springframework.validation.annotation.Validated` 的 Java Doc 注释

    ```java
    Variant of JSR-303's {@link javax.validation.Valid}, supporting the specification of validation groups. Designed for convenient use with Spring's JSR-303 support but not JSR-303 specific.
    ```

    作为 JSR-303 中 @javax.validation.Valid 的变体，支持 validation groups 的规范。专门为方便使用 Spring JSR-303 支持而设计，但不特定于 JSR-303。

    ```java
    Can be used e.g. with Spring MVC handler methods arguments. Supported through {@link org.springframework.validation.SmartValidator}'s validation hint concept, with validation group classes acting as hint objects.
    ```

    可用于注解，例如 Spring MVC handler 方法的参数。通过 SmartValidator 的验证提示概念支持，与验证组类充当提示对象。

    ```java
    Can also be used with method level validation, indicating that a specific class is supposed to be validated at the method level (acting as a pointcut for the corresponding validation interceptor), but also optionally specifying the validation groups for method-level validation in the annotated class. 
    ```

    也可以与方法级验证一起使用，表示应该在方法级验证特定的类（充当相应验证拦截器的切入点），但也可以选择在注释中指定方法级验证的验证组类。

    ```java
    Applying this annotation at the method level allows for overriding the validation groups for a specific method but does not serve as a pointcut; a class-level annotation is nevertheless necessary to trigger method validation for a specific bean to begin with. Can also be used as a meta-annotation on a custom stereotype annotation or a custom group-specific validated annotation.
    ```

    在方法级别应用此注解允许覆盖特定方法的验证组，但不用作切入点；尽管如此，类级别的注释对于触发特定 bean 的方法验证是必要的。也可以用作自定义构造型注释或自定义组特定验证注释的元注释。

2. `@Validated` 和 `@Valid` 的关系 （[参考](https://blog.csdn.net/qq_27680317/article/details/79970590)）

    * 分组
        * `@Validated`：提供了分组的功能，可以在入参验证时，根据不同的分组采用不同的验证机制。
        * `@Valid`：作为标准的 JSR-303 规范，没有分组的功能。

    * 注解位置
        * `@Validated`：可以用在类型、方法和方法参数上。但是不能用在成员属性（字段）上。
        * `@Valid`：可以用在方法、构造函数、方法参数和成员属性（字段）上。

    * 嵌套验证
        * 所谓 “嵌套验证” ，即类与其成员属性都有各自的验证机制。两者是否能用于成员属性（字段）上直接影响能否提供嵌套验证的功能。
        * `@Validated`：用在方法入参上无法单独提供嵌套验证功能。不能用在成员属性（字段）上，也无法提示框架进行嵌套验证。可以配合标注在成员属性上的 `@Valid` 进行嵌套验证。
        * `@Valid`：用在方法入参上无法单独提供嵌套验证功能。可以用在成员属性（字段）上，提示验证框架进行嵌套验证。能配合嵌套验证注解 `@Valid` 进行嵌套验证。

3. [Spring Validator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Validator.html)
    * 通过实现 `org.springframework.validation.Validator` 接口， 可以创建自定义的 Spring 验证器。该接口有两个方法需要实现：
        * boolean supports(Class<?> clazz)
        * void validate(Object target, Errors errors)

    * 接口文档中给出的实现例子是

        ```java
        public class UserLoginValidator implements Validator {

            private static final int MINIMUM_PASSWORD_LENGTH = 6;

            // Can this Validator validate instances of the supplied clazz?
            public boolean supports(Class clazz) {
                return UserLogin.class.isAssignableFrom(clazz);
            }

            /**
             * Validate the supplied target object, which must be of a Class for 
             * which the supports(Class) method typically has (or would) return true.
             */
            public void validate(Object target, Errors errors) {
                // validates that the various String properties of a UserLogin
                // instance are not empty (that is they are not null and do not
                // consist wholly of whitespace)
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "field.required");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
                UserLogin login = (UserLogin) target;
                // validates that any password that is present is not null
                // and at least 'MINIMUM_PASSWORD_LENGTH' characters in length. 
                if (login.getPassword() != null
                        && login.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH) {
                    errors.rejectValue("password", "field.min.length",
                            new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},
                            "The password must be at least [" + MINIMUM_PASSWORD_LENGTH + "] characters in length.");
                }
            }
        }
        ```

    * 可以在 Controller 中 通过创建 Validator 使用 `validator.validate(target, error)` 进行验证， 或者使用 Validator 配置 DataBinder 实例， 通过 `binder.validate()`来调用 Validator。
