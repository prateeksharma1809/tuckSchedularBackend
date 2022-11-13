package com.tuck.matches;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsynchConfiguration {
	
	@Bean(name = "asyncExecutor")
	  public Executor asyncExecutor() 
	  {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(1000);
	    executor.setMaxPoolSize(1000);
	    executor.setQueueCapacity(1000);
	    executor.setThreadNamePrefix("AsynchThread-");
	    executor.initialize();
	    return executor;
	  }

}
