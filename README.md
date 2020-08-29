##Running application
1. We can start application by running below commands
    * docker-compose build
    * docker-compose up -d
2. Although, we are running redis as part of dependency in compose file; please make sure to give appropriate binding for `cache.redisHost` in applications.properties file. 


##Design
1. Assuming that these services can be accessed by multiple devices & users. So instead of complex user management, for this demo we simply assume that IP Address equates to a User
2. All top-stories accessed by a user aka IP Address will be stored internally in Redis to server `/past-stories` end point
3. Items i.e., Stories & Comments will be stored in Redis with ttl as 30 minutes to improve performance, so user might get stale data
4. Continuing Point #3, for Stories most probably only score will change; And as this is not much important we can safely use 30 minute cache
5. Hacker News User details will be stored in Redis cache permanently, and we are showing only userhandle & createdate this will not have any impact on the application
6. Please refer to https://github.com/HackerNews/API/pull/16 for more details
 