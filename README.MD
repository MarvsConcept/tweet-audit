# Tweet Audit

Tweet Audit is a Java 21 and Spring Boot 4 CLI application that analyzes an 
X (Twitter) archive using Google's Gemini API and flags tweets that do not 
match user-defined alignment criteria.

Flagged tweets are written to a CSV file for manual review and deletion.

## Features

- Parses tweets from an exported X archive
- Evaluates tweets against configurable alignment criteria
- Uses Gemini AI for semantic analysis
- Generates a CSV of flagged tweet URLs
- Supports checkpointing and resumable processing
- Retries temporary Gemini API failures with exponential backoff
- Tracks unresolved failed tweets separately
- Supports fake and real Gemini clients through Spring profiles

## Requirements

* Java 21
* Maven
* An extracted X archive
* A Google Gemini API key

## Configuration

Application settings are defined in `src/main/resources/application.properties`.

Example:

```properties
archive.tweets-path=path/to/twitter-archive/data/tweets.js
x.username=your_x_username
output.csv-path=output/flagged_tweets.csv
checkpoint.path=output/checkpoint.txt
failed-tweets.path=output/failed_tweets.txt

audit.criteria.forbidden-words=crypto,NFT,hustlegrindset
audit.criteria.professional-check=true
audit.criteria.tone=respectful and thoughtful
audit.criteria.exclude-politics=true

gemini.api-key=${GEMINI_API_KEY:}
gemini.model=gemini-3.5-flash-lite
gemini.base-url=https://generativelanguage.googleapis.com/v1beta/interactions
gemini.request-delay-ms=6000
```

The real Gemini API key should not be stored in `application.properties` or 
committed to Git. Set `GEMINI_API_KEY` as an environment variable instead.

A `config.example.json` file is also included as a reference showing the 
available configuration options without exposing real secrets.

## Gemini API Key

Create a Gemini API key from Google AI Studio.

Do not place the real key in the repository.

For local development in IntelliJ:

1. Open **Run → Edit Configurations**
2. Select the Tweet Audit run configuration
3. Add these environment variables:

```text
GEMINI_API_KEY=your_real_api_key
SPRING_PROFILES_ACTIVE=gemini
```

`GEMINI_API_KEY` provides the secret API key, while the `gemini` Spring profile 
activates the real `GeminiTweetAuditClient`.

Without the `gemini` profile, the application uses `FakeTweetAuditClient` for 
local development and testing.

## Running the Application

Update `application.properties` with the path to your extracted X archive and your X username:

```properties
archive.tweets-path=path/to/archive/data/tweets.js
x.username=your_username
```

Then run the application from IntelliJ or with Maven:

```bash
mvn spring-boot:run
```

When using the real Gemini client, make sure `GEMINI_API_KEY` and `SPRING_PROFILES_ACTIVE=gemini` are 
available in the environment before starting the application.

## Output

Flagged tweets are written to the configured CSV file:

```csv
tweet_url,deleted
https://x.com/username/status/1234567890,false
```

The application does not delete tweets automatically. The CSV is intended for manual review and deletion.

## Checkpointing and Failure Recovery

Successfully audited tweet IDs are stored in the checkpoint file. On later runs, those tweets 
are skipped so the application can resume without starting over.

Tweets that fail during auditing are stored in `failed_tweets.txt`. They are not checkpointed, 
so they can be retried on later runs. If a previously failed tweet later succeeds, its failure record is removed.

Temporary Gemini failures such as rate limits or service unavailability are retried with exponential 
backoff. Non-retryable failures are recorded and processing continues with the next tweet.

## Testing

Run the test suite with:

```bash
mvn test
```

The tests cover archive parsing, URL generation, CSV output, checkpointing, failed-tweet tracking, 
fake audit behavior, service orchestration, failure recovery, and resumable processing.
