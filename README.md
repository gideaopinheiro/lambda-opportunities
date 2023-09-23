# Lambda Opportunities API

This project is a Clojure-written version of the [Gopportunities](https://www.youtube.com/watch?v=wyEYpX5U4Vg) project written in Go.
The libraries used are: ring, reitit and next.jdbc

## How to run

Once you have both Docker and Docker Compose installed in your machine, just open an terminal window, go to the project directory and run:

```
$ docker-compose up
```

After performing this command, you will have the API and a PostgreSQL database up and running.
Then you can execute CRUD operations to the endpoints (I recommend you use Postman or Insomnia for a better experience):

Create an opening:

```
curl -X POST -H "Content-Type: application/json" -d '{
    "role": "Backend Engineer",
    "company": "Uber",
    "remote": true,
    "link": "http://uber.com",
    "location": "address, 123",
    "salary": 180000
}' http://localhost:3333/opening

```

List all openings:

```
curl http://localhost:3333/openings

```

List by id:

```
curl http://localhost:3333/opening/{put-a-valid-id-here}

```

Update an opening:

```
curl -X PUT -H "Content-Type: application/json" -d '{
    "salary": 185996
}' http://localhost:3333/opening/{put-a-valid-id-here}
```

Delete an opening:

```
curl -X DELETE http://localhost:3333/opening/{put-a-valid-id-here}
```

## To do

- [ ] Schema validation using Malli

- [ ] Use Swagger for API documentation

- [ ] Add Testing

- [ ] Improve the architecture and code readability
