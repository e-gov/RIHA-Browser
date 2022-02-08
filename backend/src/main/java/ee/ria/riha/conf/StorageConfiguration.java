package ee.ria.riha.conf;

import ee.ria.riha.client.StorageClient;
import ee.ria.riha.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StorageConfiguration {

    @Bean
    public StorageClient getStorageClient(ApplicationProperties applicationProperties) {
        return new StorageClient(applicationProperties.getStorageClient().getBaseUrl());
    }

    @Bean
    public MainResourceRepository mainResourceRepository(StorageClient storageClient) {
        return new MainResourceRepository(storageClient);
    }

    @Bean
    public InfoSystemRepository infoSystemRepository(MainResourceRepository mainResourceRepository) {
        return new RihaStorageInfoSystemRepository(mainResourceRepository);
    }

    @Bean
    public CommentRepository commentRepository(StorageClient storageClient) {
        return new CommentRepository(storageClient);
    }

    @Bean
    public MainResourceRelationRepository mainResourceRelationRepository(StorageClient storageClient) {
        return new MainResourceRelationRepository(storageClient);
    }

    @Bean
    public FileRepository fileRepository(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        return new FileRepository(restTemplate, applicationProperties.getStorageClient().getBaseUrl());
    }

    @Bean
    public LdapRepository ldapRepository(LdapContextSource ldapContextSource, ApplicationProperties applicationProperties) {
        return new LdapRepository(ldapContextSource, applicationProperties.getLdapRepository());
    }
}
