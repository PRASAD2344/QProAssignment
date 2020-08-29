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

##Decisions
1. `/best-stories` - Should return the top 10 best stories ranked by score in the last 15 minutes (Read Instructions), Each story should have title, URL, score, time of submission and the user who submitted it
    * https://hacker-news.firebaseio.com/v0/beststories.json endpoint will already sort entries based on score, so taking top 10 from that list. Effectively so sorting based on scored will be done by application
2. `/past-stories` - Should return all the past top stories that were served previously
    * We will store & return all the stories which were being served to user when he/she make a call(s) `/best-stories`
    * While returning items, we will sort them based on`score`
3. `/comments` - Should return the top 10 comments on a given story sorted by total number of child comments. Each comment should contain comment text, user’s hacker news handle and how old the users hacker news profile is in years
    * We will simply list all comments, and limit it to top 10. I am not sure what order hackernews api will follow, either lastest one first or the one with most child comments.
    * We will sort the 10 comments, based on number of child comments using `kids` collection size. Child comment will in-turn contains child comments, we will not go there as part of this demo.
 