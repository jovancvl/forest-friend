#!/bin/sh

cd JavaBotWebsite

# Start the first process
cd lavalink
java -jar Lavalink.jar &
cd ..

# Start the second process
java -jar JavaBotWebsite.jar &

# Wait for any process to exit
wait -n

# Exit with status of process that exited first
exit $?