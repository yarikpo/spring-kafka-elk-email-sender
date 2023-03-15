# spring-kafka-elk-email-sender

## Installation

Clone repository:

```sh
git clone https://github.com/yarikpo/spring-kafka-elk-email-sender.git
```

Enter it:
```sh
cd spring-kafka-elk-email-sender
```

Create data folder:

```sh
mkdir data
```

Give it "right" privileges:
```sh
sudo chmod a+rwx ./data
```

### Set up your properties

Change properties in .env and application.properties files. For example change ***spring.application.ipurl*** property (application.properties), ***EMAIL_ADDRESS*** and ***EMAIL_API_PASSWORD*** property (.env)

### Build the project

Run:
```sh
docker-compose build
```

### Run the project

Run:
```sh
docker-compose up
```



## Notice

*I had troubles with elasticsearch, so docker runs java multiple times (until elasticsearch is finaly up). So, integration tests can't be run, because while building (when tests runs) application requires elasticsearch. Also this is the reason why I don't build jars on docker side.*

## How to test

In **postman** you can send messages by posting 
```sh
{
    "sender": "smthelse@gmail.com",
    "content": "HOHOHO TEST EMAIL!!!",
    "subject": "TESTING!>>>!",
    "receivers": [
        "yaroslavpopovich04@email.com",
        "yaroslav.popovych@nure.ua"
    ]
}
```
on link http://localhost:8080/api/sendMessage

After this you'll see results after posting here: http://localhost:9200/emails/_search 

this:
```sh
{
    "query": {
        "match_all": { }
    },
    "sort": [{
        "timestamp": "desc"
    }]
}
```

And you can check for error messages by posting 
```sh
{
    "sender": "sender@gmail.com",
    "subject": "AGAIN!...",
    "receivers": [""]
}
```
here http://localhost:8080/api/sendMessage

After last request you can find new message with *error* status in all messages. Also you can correct it by sending 
```sh
{
    "doc": {
        "receivers": [
                        "yaroslavpopovich04@gmail.com"
                    ]
    }
}
```
here http://localhost:9200/emails/_update/{id}

In 5 minutes (or after restarting docker) this message will be sent and will change its status to *DONE*.
