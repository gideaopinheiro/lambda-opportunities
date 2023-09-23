FROM clojure:tools-deps
WORKDIR /app
COPY . .
RUN clj -T:build uber
EXPOSE 3333 
CMD ["java", "-jar", "target/loapi-1.0.1-standalone.jar"]
