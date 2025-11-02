# gradlew config
GRADLEW         = ./gradlew         # path to gradlew executable
COMMAND_DESKTOP = run
COMMAND_WEB     = html:superDev
COMMAND_CLEAN   = html:clean

# config to open in browser
BROWSER = firefox-developer-edition # put the name or path to your browser here
PORT    = 8080                      # default port

# tests as a desktop app
test:
	$(GRADLEW) $(COMMAND_DESKTOP)

# opens html server
start:
	$(GRADLEW) $(COMMAND_WEB)

# opens in browser
open:
	$(BROWSER) http://localhost:$(PORT)

clean:
	$(GRADLEW) $(COMMAND_CLEAN)