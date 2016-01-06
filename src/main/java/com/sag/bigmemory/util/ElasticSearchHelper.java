package com.sag.bigmemory.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.elasticsearch.node.Node;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;



@Configuration
@PropertySource("classpath:config.properties")
public class ElasticSearchHelper {

	@Value("${elastic.home}")
	private static String elastichome;

	//hello
	@Value("${elastic.clustername}")
	private static String clustername;
	
    private static Client client;
	
    private static void init(){
    	Node node = nodeBuilder().clusterName(clustername)
				.settings(Settings.settingsBuilder().put("http.enabled", false))
			.settings(Settings.settingsBuilder().put("path.home", elastichome)).client(true).node();

			
			Client client = node.client();
    }
    
    public static Client getElasticClient(){
    	
    	if (client == null) init();
    	return client;
    			
    	
    }
	
}
