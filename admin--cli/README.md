# Administrative Interface

Run with: 
docker run -p5432:5432 --name phase0-postgres -e POSTGRES_PASSWORD=abc123 -e POSTGRES_USER=rlg322 -d postgres

mvn package
POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_USER=rlg322 POSTGRES_PASS=abc123 mvn exec:java