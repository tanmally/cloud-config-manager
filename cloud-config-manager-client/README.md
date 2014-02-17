Cloud config manager client usage:
==================================



Build the project and add maven artifact to local repo as the lib is not published to public repo yet . Once the maven artifcat is there in the local repo add the lib to your pom file .The ${cc-manager-version} is the current stable version of cloud config manager. JDK or JRE 1.7 is required for cloud config manager.

            <dependency>
    			  <groupId>com.bbytes.config</groupId>
    			  <artifactId>cloud-config-manager-client</artifactId>
    			  <version>${cc-manager-version}</version>
    		</dependency>
    		  
    		  
 Using cloud config manager in java spring project 
 
 The spring context file content : 
 
 
 	<!-- Config loading via cloud config manager -->
	<bean class="com.bbytes.config.spring.CloudConfigPropertyInitializer">
		<!-- cloud config server host & port -->
		<property name="host" value="localhost" /> 
		<property name="port" value="9000" />
		<!-- project name the cloud config has to serve -->
		<property name="project" value="test" />
		<!-- how freq the properties should be checked for update -->
		<property name="pollDelay" value="10" />
		<!-- config files in class path, same as spring config locations property  -->
		<property name="configList">
			<list>
				<value>classpath:sample.properties</value>
				<value>classpath*:META-INF/conf.properties</value>
			</list>
		</property>
	</bean>

	<!-- Using the cloud config impl of spring PropertyPlaceholderConfigurer, 
		this line is the one that tells spring to load the config 
		files using cloud config mechanism -->
	<bean
		class="com.bbytes.config.spring.CloudConfigPropertyPlaceholderConfigurer"
		depends-on="com.bbytes.config.spring.CloudConfigPropertyInitializer" />


	<!-- Using the properties loaded by cloud config in a bean -->
	<!-- prop1 and prop2 are from sample.properties file -->
	<bean id="propTest" class="com.bbytes.config.PropLoadTestBean">
		<property name="prop1" value="${prop1}" />
		<property name="prop2" value="${prop2}" />
	</bean>
	
	


Using cloud config inside java code :

    String prop1Val = DynamicPropertyFactory.getInstance().getStringProperty("prop1", "default").get(); // if string
    double pollTime = DynamicPropertyFactory.getInstance().getDoubleProperty("pollTime", 1000d).get();  // if double
    
    
The cloud config is an extension of Netflix archaius project
    
    
    
