##Running application
1. We can start application by running below commands
    * docker-compose build
    * docker-compose up -d
2. Although, we are running redis as part of dependency in compose file; please make sure to give appropriate binding for `cache.redisHost` in applications.properties file. 