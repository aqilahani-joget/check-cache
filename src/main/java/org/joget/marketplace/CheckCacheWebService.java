package org.joget.marketplace;

import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.ExtDefaultPlugin;
import org.joget.plugin.base.PluginWebSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Statistics;

public class CheckCacheWebService extends ExtDefaultPlugin implements PluginWebSupport {

    @Override
    public String getName() {
        return "CheckCacheWebService";
    }

    @Override
    public String getVersion() {
        return "8.0.0";
    }

    @Override
    public String getDescription() {
        return "CheckCacheWebService";
    }

    @Override
    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
    }

    public void performTask() {
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache("org.joget.cache.DATASOURCE_CACHE");

        if (cache != null) {
            LogUtil.info(CheckCacheWebService.class.getName(), "Cache Statistics: " + cache.getStatistics().toString());
            Statistics stats = cache.getStatistics();
            LogUtil.info(CheckCacheWebService.class.getName(), "Ehcache stats: Hits=" + stats.getCacheHits()
                + ", Misses=" + stats.getCacheMisses()
                + ", Size=" + cache.getSize()
                + ", InMemorySize=" + cache.getMemoryStoreSize()
                + ", Evicted=" + stats.getEvictionCount()
            );
        } else {
            LogUtil.warn(CheckCacheWebService.class.getName(), "Cache 'org.joget.cache.DATASOURCE_CACHE' not found.");
        }
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos = bean.dumpAllThreads(true, true);

        long countBlocked = Arrays.stream(infos)
            .filter(info -> info.getLockName() != null && info.getLockName().contains("ReadWriteLock"))
            .count();

        LogUtil.info(CheckCacheWebService.class.getName(), "Threads blocked on cache: " + countBlocked);

    }
}
