### 作业：

参考实现类 `org.geektimes.cache.interceptor.CachePutInterceptor`，实现 `@javax.cache.annotation.CacheRemove` 注解的 `@Interceptor` Class

### 作答：

(在我同步了马哥的repository然后开始做作业的时候，发现CacheRemove已经实现好了 T_T，于是乎默默地在本地删掉了 `CacheRemoveInterceptor.java` 和 `CacheRemoveInterceptorTest.java`， 然后自己尝试实现一下，思路如下)

1）参照 `org.geektimes.cache.interceptor.CachePutInterceptor` 实现 `org.geektimes.cache.interceptor.CacheRemoveInterceptor` 如下：  
```
/**
 * The {@link Interceptor @Interceptor} class for Java Caching annotation {@link CacheRemove}.
 *
 */
@Interceptor
public class CacheRemoveInterceptor extends CacheOperationInterceptor<CacheRemove> {
    @Override
    protected CacheOperationAnnotationInfo getCacheOperationAnnotationInfo(CacheRemove cacheOperationAnnotation,
                                                                           CacheDefaults cacheDefaults) {
        return new CacheOperationAnnotationInfo(cacheOperationAnnotation, cacheDefaults);
    }

    @Override
    protected Object beforeExecute(CacheRemove cacheOperationAnnotation,
                                    CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                    CacheOperationAnnotationInfo cacheOperationAnnotationInfo, Cache cache,
                                    Optional<GeneratedCacheKey> cacheKey) {
        if (!cacheOperationAnnotationInfo.isAfterInvocation()) {
            manipulateCache(cacheKeyInvocationContext, cache, cacheKey);
        }
        return null;
    }

    @Override
    protected void afterExecute(CacheRemove cacheOperationAnnotation,
                                CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                CacheOperationAnnotationInfo cacheOperationAnnotationInfo, Cache cache,
                                Optional<GeneratedCacheKey> cacheKey, Object result) {
        if (cacheOperationAnnotationInfo.isAfterInvocation()) {
            manipulateCache(cacheKeyInvocationContext, cache, cacheKey);
        }
        
    }

    private void manipulateCache(CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext, Cache cache,
                                 Optional<GeneratedCacheKey> cacheKey) {
        cacheKey.ifPresent(key -> {
            CacheInvocationParameter valueParameter = cacheKeyInvocationContext.getValueParameter();
            if (valueParameter != null) {
                cache.remove(key, valueParameter.getValue());
            } else {
                cache.remove(key);
            }
        });
    }

    @Override
    protected void handleFailure(CacheRemove cacheOperationAnnotation,
                                 CacheKeyInvocationContext<CacheRemove> cacheKeyInvocationContext,
                                 CacheOperationAnnotationInfo cacheOperationAnnotationInfo, Cache cache,
                                 Optional<GeneratedCacheKey> cacheKey, Throwable failure) {
        cacheKey.ifPresent(key -> {
            cache.remove(key, failure);
        });
    }
}
```

基本与 `CachePutInterceptor` 一致，只有方法 `manipulateCache` 和 `handleFailure` 稍微不一样，用的是 `cache.remove`。  

2）实现单元测试:  
- 将接口 `DataRepository` 中的 `remove` 标记上 `@CacheRemove(cacheName = "simpleCache", afterInvocation = false)`。  
- 实现单元测试如下   
```
public class CacheRemoveInterceptorTest {
    private DataRepository dataRepository = new InMemoryDataRepository();

    private InterceptorEnhancer enhancer = new DefaultInterceptorEnhancer();

    @Test
    public void test() {
        DataRepository repository = enhancer.enhance(dataRepository, DataRepository.class,
                                                    new CachePutInterceptor(), new CacheRemoveInterceptor());
        // 添加数据
        assertTrue(repository.create("geek", 1));
        assertNotNull(repository.get("geek"));
        
        // 测试删除数据
        assertTrue(repository.remove("geek"));
        assertNull(repository.get("geek"));
    }
}
```