package org.geektimes.cache.annotation.interceptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geektimes.cache.DataRepository;
import org.geektimes.cache.InMemoryDataRepository;
import org.geektimes.interceptor.DefaultInterceptorEnhancer;
import org.geektimes.interceptor.InterceptorEnhancer;
import org.junit.Test;

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
