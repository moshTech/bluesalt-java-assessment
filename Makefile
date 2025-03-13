# Makefile

# Run integration tests
test-unit:
	mvn spotless:apply
	mvn test -P dev

# Run integration tests
test:
	mvn spotless:apply
	mvn verify -P integration-test