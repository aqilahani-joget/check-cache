package org.joget.marketplace;

import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.ExtDefaultPlugin;
import org.joget.plugin.base.PluginWebSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class CheckCacheWebService extends ExtDefaultPlugin implements PluginWebSupport {

    private static final CacheManager cacheManager;
    private static final Cache cache;
    
    static {
        cacheManager = CacheManager.create();
        cache = cacheManager.getCache("org.joget.cache.DATASOURCE_CACHE");
    }

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
        LogUtil.info(CheckCacheWebService.class.getName(), "Cache Statistics: " + cache.getStatistics().toString());
    }
}
