package org.joget.marketplace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joget.commons.util.LogUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    protected Collection<ServiceRegistration> registrationList;

    protected ScheduledExecutorService scheduler;
    
    public void start(BundleContext context) {
        registrationList = new ArrayList<ServiceRegistration>();

        CheckCacheWebService service = new CheckCacheWebService();
        
        //Register plugin here
        registrationList.add(context.registerService(CheckCacheWebService.class.getName(), new CheckCacheWebService(), null));
        LogUtil.info(CheckCacheWebService.class.getName(), "starting scheduler for CheckCacheWebService");
        Runnable minuteTask = () -> {
            service.performTask();
        };
        scheduler = Executors.newScheduledThreadPool(1);
        //change 5 minutes to another value
        scheduler.scheduleAtFixedRate(minuteTask, 0, 5, TimeUnit.MINUTES);
    }

    public void stop(BundleContext context) {
        for (ServiceRegistration registration : registrationList) {
            registration.unregister();
        }
        
        LogUtil.info(CheckCacheWebService.class.getName(), "shutting down scheduler for CheckCacheWebService");
        scheduler.shutdown();
    }
}