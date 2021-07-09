all:
	@./gradlew build installDist

clean:
	-@rm -f *.class
	@./gradlew clean
