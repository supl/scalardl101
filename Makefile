all:
	@rm ./*.class
	@./gradlew clean build installDist
