## Language and Framework

I chose Java 21 and Spring Boot 4 to strengthen my backend 
engineering skills in the Java ecosystem. I used Spring Boot 
instead of plain Java despite the added framework overhead because 
it simplifies dependency injection, configuration management, testing, 
and external API integration while still allowing the application to run as a CLI-style process.

## Architecture Choices

The application separates responsibilities across several components:

`Runner → TweetAuditService → Parser / AuditClient / URL Builder / CSV Writer / Checkpoint / FailedTweetWriter`

Each component has a specific responsibility. The parser reads tweets from the X archive, 
the audit client determines whether they should be flagged, the URL builder creates tweet URLs,
and the CSV writer stores flagged results. The checkpoint service tracks successfully processed 
tweets, while the failed tweet writer tracks unresolved failures. `TweetAuditService` coordinates 
the workflow.

I also introduced the `TweetAuditClient` interface so the service depends on an abstraction rather 
than Gemini directly. This improves testability and allows fake and Gemini implementations to be 
switched using Spring profiles.

The tradeoff is more classes and structure, in exchange for clearer responsibilities, easier testing, 
and isolated external API logic.

## Concurrency Strategy

I chose sequential processing instead of batching or multiple threads because it simplifies 
checkpointing, retry handling, rate limiting, and file writes. It is also easier to debug and 
reduces the risk of overwhelming the Gemini API.

The downside is slower processing for large archives and lower network concurrency. Bounded 
concurrency or batching could be added later if performance becomes a real issue.

## Error Handling and Recovery

Gemini failures are classified as retryable or non-retryable. Temporary failures such as rate 
limits or HTTP 503 responses are retried using exponential backoff. Non-retryable failures are 
recorded in `failed_tweets.txt`, while processing continues with the next tweet.

Failed tweets are retried on later runs because they are not checkpointed. Successfully audited 
tweets are stored in the checkpoint file so the application can resume after a crash without 
starting over.

The tradeoff is additional recovery logic and state files in exchange for resumability and 
fault tolerance.

## Performance vs Safety

I prioritized correctness and recoverability over maximum throughput. The application uses 
sequential processing, controlled request delays, checkpointing, selective retries, and separate 
failure tracking. Gemini interactions also use `store=false` because the application processes 
personal archive data.

These choices reduce API pressure and simplify recovery, but make the application slower than a 
fully asynchronous implementation.
