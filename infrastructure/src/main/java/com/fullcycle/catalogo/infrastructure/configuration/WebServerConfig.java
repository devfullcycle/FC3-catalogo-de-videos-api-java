package com.fullcycle.catalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fullcycle.catalogo")
public class WebServerConfig {
}