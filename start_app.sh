#!/bin/sh

git clone https://github.com/jovancvl/JavaBotWebsite

cd JavaBotWebsite

# Start the first process
java -jar lavalink/Lavalink.jar &

ls
# Start the second process
java -jar JavaBotWebsite.jar &

# Wait for any process to exit
wait -n

# Exit with status of process that exited first
exit $?