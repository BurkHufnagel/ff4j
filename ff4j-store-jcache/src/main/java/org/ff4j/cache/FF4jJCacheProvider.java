package org.ff4j.cache;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.ff4j.core.Feature;
import org.ff4j.property.AbstractProperty;

public class FF4jJCacheProvider {
    
    /** The Cache Provider. */
    private CachingProvider cachingProvider;
    
    /** JCache, cache manager. */
    private CacheManager cacheManager; 
    
    /**
     * Initialization of cache.
     */
    protected FF4jJCacheProvider() {
        this(null);
    }

    /**
     * Initialization of cache.
     */
    protected FF4jJCacheProvider(String providerClassName) {
        
        // Initialisation of provider (can be null)
        cachingProvider = initCachingProvider(providerClassName);
      
        // Initialization of manager
        cacheManager = initCacheManager();
    }
    
    /**
     * Default Initialisation of {@link CachingProvider}. It will work only is there is
     * a single {@link CachingProvider} implementation within classpath. Otherwise should must
     * overriden it to initialize with your own.
     *
     * @return
     *      specialization of {@link CachingProvider} (JCache)
     */
    protected CachingProvider initCachingProvider(String cachingProviderClassname) {
        try {
            if (cachingProviderClassname == null) {
                return Caching.getCachingProvider();
            }
            return Caching.getCachingProvider(cachingProviderClassname);
        } catch(RuntimeException re) {
            /* Some cache implementation do not provide CachingProvider but the cacheManager
             * work properly. As a consequence, caching provider can be null and should not throw
             * caching exception.
             */
            return null;
        }
    }
    
    /**
     * Initialization of cache manager. Default implementation rely on the {@link CachingProvider} as
     * expected by the JSR 107 but some cache implementation do not provide caching provider.
     *
     * @return
     *      initialisation of cache manager
     */
    protected CacheManager initCacheManager() {
        if (cachingProvider == null) {
            throw new IllegalArgumentException("Cannot initialize cacheManager as CachingProvider is empty, please check 'initCachingProvider'");
        }
        return cachingProvider.getCacheManager();
    }
     
    /**
     * Initialize cache configuration, could be overriden.
     *
     * @return
     *      cache default configuration
     */
    protected MutableConfiguration< String, Feature> getFeatureCacheConfiguration() {
        MutableConfiguration<String, Feature> featuresCacheConfig = new MutableConfiguration<String, Feature>();        
        featuresCacheConfig.setTypes(String.class, Feature.class);
        featuresCacheConfig.setStoreByValue(true);
        featuresCacheConfig.setStatisticsEnabled(false);
        return featuresCacheConfig;
    }
    
    
    /**
     * Initialize cache configuration, could be overriden.
     *
     * @return
     *      cache default configuration
     */
    @SuppressWarnings("rawtypes")
    protected MutableConfiguration< String, AbstractProperty> getPropertyCacheConfiguration() {
        MutableConfiguration<String, AbstractProperty> propertiesCacheConfig = new MutableConfiguration<String, AbstractProperty>();        
        propertiesCacheConfig.setTypes(String.class, AbstractProperty.class);
        propertiesCacheConfig.setStoreByValue(true);
        propertiesCacheConfig.setStatisticsEnabled(false);
        return propertiesCacheConfig;
    }
    
    /**
     * Getter accessor for attribute 'cachingProvider'.
     *
     * @return
     *       current value of 'cachingProvider'
     */
    public CachingProvider getCachingProvider() {
        return cachingProvider;
    }

    /**
     * Getter accessor for attribute 'cacheManager'.
     *
     * @return
     *       current value of 'cacheManager'
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }

}
