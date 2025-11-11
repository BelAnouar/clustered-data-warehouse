APP_NAME = fx-deals-service
JAR_FILE = target/$(APP_NAME).jar

.DEFAULT_GOAL := help

help:
	@echo "Usage:"
	@echo "  make build              → Build the project"
	@echo "  make run                → Run Spring Boot app"
	@echo "  make test               → Run tests"
	@echo "  make clean              → Clean target directory"
	@echo "  make package            → Build jar without running tests"
	@echo "  make docker Build       → Build Docker image (if Dockerfile exists)"
	@echo "  make docker  compose    → Build Docker image (if Dockerfile exists)"

build:
	@mvn clean install

run:
	@mvn spring-boot:run

test:
	@mvn test

clean:
	@mvn clean

package:
	@mvn clean package -DskipTests

docker Build:
	@docker build -t $(APP_NAME):latest .

docker compose:
	@docker compose up -d
