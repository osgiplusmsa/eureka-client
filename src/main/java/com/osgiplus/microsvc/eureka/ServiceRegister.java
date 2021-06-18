package com.osgiplus.microsvc.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;

public class ServiceRegister {
	EurekaInstanceConfig instanceConfig = null;
	EurekaClientConfig clientConfig = null;
	private ApplicationInfoManager applicationInfoManager=null;
	private DiscoveryClient eurekaClient;

	//com.netflix.config.DynamicPropertyFactory.getInstance();
	//new MyDataCenterInstanceConfig();

	protected ApplicationInfoManager initializeApplicationInfoManager() {
		
		if (applicationInfoManager == null) {
			InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
			applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
		}

		return applicationInfoManager;
	}

	protected  EurekaClient initializeEurekaClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig clientConfig) {

		if (eurekaClient == null) {
			eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
		}

		return eurekaClient;
	}
	
	public EurekaClient initializeEurekaClient(){
		initConfig();
		
		if(applicationInfoManager == null){
			applicationInfoManager = initializeApplicationInfoManager();
		}
		
		return this.initializeEurekaClient(applicationInfoManager, clientConfig);
	}
	
    

	public void initConfig(){
		
		if(instanceConfig == null){
			instanceConfig = new  MyDataCenterInstanceConfig();
			clientConfig =  new DefaultEurekaClientConfig();
		}
		
	}

	protected EurekaInstanceConfig getConfigInstance() {
		return instanceConfig;
	}

	protected EurekaClientConfig getClientConfig() {
		return clientConfig;
	}

}
