# 作业

增加一个注解名为 `@ConfigSources`，使其能够关联多个 `@ConfigSource`，并且在 `@ConfigSource` 使用 `Repeatable`；
可以对比参考 Spring 中 `@PropertySources` 与 `@PropertySource`，并且文字说明 Java 8 `@Repeatable` 实现原理。

- 可选作业，根据 URL 与 URLStreamHandler 的关系，扩展一个自定义协议，可参考 `sun.net.www.protocol.classpath.Handler`

## 作答

1. 参照Spring 中 `@PropertySources` 的实现，注解名`@ConfigSources`的实现如下：

    ```java
    /**
    * Container annotation that aggregates several {@link ConfigSource} annotations.
    *
    */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface ConfigSources {
        ConfigSource[] value();
    }
    ```

    `ConfigSources` 作为容器可以容纳多个 `ConfigSource` 的注释名。这就要求在 `ConfigSource` 添加 `@Repeatable` 并关联 `ConfigSources` 才能完成实现。具体如下：

    ```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Repeatable(ConfigSources.class)
    public @interface ConfigSource {

        /**
        * The name of {@link org.eclipse.microprofile.config.spi.ConfigSource}
        *
        * @see {@link org.eclipse.microprofile.config.spi.ConfigSource#getName()}
        */
        String name() default "";
    .... // 此处省略
    ```

2. 单元测试：
    参照马哥的 `ConfigSourceTest`，创建了 `testConfigSource.properties` 存放如下数据

    ```text
    user.id = 002
    user.name = Hello geekbang
    ```

    测试代码如下：

    ```java
    @ConfigSources(value = { 
        @ConfigSource(ordinal = 200, resource = "classpath:/META-INF/default.properties"),
        @ConfigSource(ordinal = 201, resource = "classpath:/META-INF/testConfigSource.properties"),
    })
    public class ConfigSourcesTest {
        @Before
        public void initConfigSourceFactory() throws Throwable {
            // 获取 ConfigSources 注解
            ConfigSources configSources = getClass().getAnnotation(ConfigSources.class);
            // 获取 ConfigSources 注解内容
            ConfigSource[] configSourcesValues = configSources.value();
            for (ConfigSource configSource : configSourcesValues) {
                String name = configSource.name();
                int ordinal = configSource.ordinal();
                String encoding = configSource.encoding();
                String resource = configSource.resource();
                URL resourceURL = new URL(resource);
                Class<? extends ConfigSourceFactory> configSourceFactoryClass = configSource.factory();
                if (ConfigSourceFactory.class.equals(configSourceFactoryClass)) {
                    configSourceFactoryClass = DefaultConfigSourceFactory.class;
                }

                ConfigSourceFactory configSourceFactory = configSourceFactoryClass.newInstance();
                org.eclipse.microprofile.config.spi.ConfigSource source =
                        configSourceFactory.createConfigSource(name, ordinal, resourceURL, encoding);
                // 打印 configSource 属性
                System.out.println(source.getProperties());
            } 
        }

        @Test
        public void test() {
        }
    }
    ```

    打印结果如下：

    ```text
    {user.age=35, user.name=mercyblitz, user.id=001}
    {user.id=002, user.name=Hello geekbang}
    ```

3. Java 8 `@Repeatable` 实现原理：

    源代码如下

    ```java
    /**
     * The annotation type {@code java.lang.annotation.Repeatable} is
     * used to indicate that the annotation type whose declaration it
     * (meta-)annotates is <em>repeatable</em>. The value of
     * {@code @Repeatable} indicates the <em>containing annotation
     * type</em> for the repeatable annotation type.
     *
     * @since 1.8
     * @jls 9.6 Annotation Types
     * @jls 9.7 Annotations
    */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    public @interface Repeatable {
        /**
        * Indicates the <em>containing annotation type</em> for the
        * repeatable annotation type.
        * @return the containing annotation type
        */
        Class<? extends Annotation> value();
    }
    ```

    由此可见 `@Repeatable` 用于注解 “注解类型”， 表明该注解类型是可以重复使用。它是java8为了解决同一个注解不能重复在同一类/方法/属性上使用的问题。由 `PropertySource` 和 `ConfigSource` 的实现可以看出，`Repeatable(Values.class)` 指示在同一个类中 `@Value` 注解是可以重复使用的，重复的注解被存放至 `@Values` 注解中。
