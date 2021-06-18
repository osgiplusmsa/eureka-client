package com.osgiplus.microsvc.eureka;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;

public class TestServiceRegister {

	@Before
	public void setUp() throws Exception {
		System.setProperty("eureka.client.props", "conf/sample-eureka-service");
	}

	@Test
	public void testInitConfig() {
		
		ServiceRegister r = new ServiceRegister();
		r.initConfig();
		
		EurekaInstanceConfig x = r.getConfigInstance();
		//EurekaClientConfig y = r.getClientConfig();
		
		//x.getStringProperty(propName, defaultValue);
		
		String address = x.getIpAddress();
		int port = x.getNonSecurePort();
		String vn = x.getVirtualHostName();
		
		
		assertEquals(8001,port);
		//assertEquals("192.168.99.1",address);
		assertEquals("sampleservice.mydomain.net",vn);		
		//y.getAvailabilityZones(arg0)
		
	}
	
	@Test
	public void testInitApp(){		
		
		
		ServiceRegister r = new ServiceRegister();
		r.initConfig();
		
		//EurekaInstanceConfig x = r.getConfigInstance();
		
		ApplicationInfoManager y = r.initializeApplicationInfoManager();
		InstanceInfo z = y.getInfo();
		
		assertEquals(8001,z.getPort());
		
		assertEquals("sampleRegisteringService".toUpperCase(),y.getInfo().getAppName());
		
	}
	
	@Test
	public void testInitClient(){

		ServiceRegister r = new ServiceRegister();
		r.initConfig();

		//EurekaInstanceConfig x = r.getConfigInstance();

		ApplicationInfoManager y = r.initializeApplicationInfoManager();
		InstanceInfo z = y.getInfo();
		
		//z.getDataCenterInfo().

		//EurekaClientConfig clientConfig;
		EurekaClient c = r.initializeEurekaClient(y, new DefaultEurekaClientConfig());
		
		System.out.println("Simulating service initialization by sleeping for 2 seconds...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Nothing
        }

        // Now we change our status to UP
        System.out.println("Done sleeping, now changing status to UP");
        y.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
        
		//c.get
		//query server to verify it is registered
		InstanceInfo nextServerInfo = null;
		boolean bQuit =false;
		while(bQuit) {
		//while (nextServerInfo == null) {
			try {
				//nextServerInfo = c.getNextServerFromEureka("sampleservice.mydomain.net", false);
				bQuit = (c.getApplications().size() > 1);
				
			} catch (Throwable e) {
				System.out.println("Waiting ... verifying service registration with eureka ...");

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		y.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
		//assertTrue(nextServerInfo != null);
		//assertNotNull(nextServerInfo);
		
		System.out.println("applications: "+c.getApplications().getRegisteredApplications());
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		c.shutdown();

	}
	
	@Test
	public void testInitClientX(){

		ServiceRegister r = new ServiceRegister();
		EurekaClient c = r.initializeEurekaClient();
		
		System.out.println("Simulating service initialization by sleeping for 2 seconds...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Nothing
        }

        // Now we change our status to UP
        System.out.println("Done sleeping, now changing status to UP");
        
        c.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
        
		//c.get
		//query server to verify it is registered
		InstanceInfo nextServerInfo = null;
		//boolean bQuit =false;
		
		while (nextServerInfo == null) {
			try {
				nextServerInfo = c.getNextServerFromEureka("sampleservice.mydomain.net", false);
				//bQuit = (c.getApplications().size() > 1);
				
			} catch (Throwable e) {
				System.out.println("Waiting ... verifying service registration with eureka ...");

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		c.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
		//assertTrue(nextServerInfo != null);
		//assertNotNull(nextServerInfo);
		
		System.out.println("applications: "+c.getApplications().getRegisteredApplications());
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		c.shutdown();
	}

}
