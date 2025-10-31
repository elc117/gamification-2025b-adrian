GRADLEW = ./gradlew
COMMAND = :html:superDev

BROWSER = firefox
PORT = 8080

all:
	$(MAKE) run
	$(MAKE) open

run:
	$(GRADLEW) $(COMMAND)

open:
	$(BROWSER) http://localhost:$(PORT)