# Description
This is a Java Backend template, designed to be generic, yet versatile.
Over the time I will improve and add/adapt to new technologies I come across and find important to add or change here.

# Instructions
1. Clone/download/fork the repo or use this template.
2. Change most/all "template" names in this project to your project name/title.
3. To run the DataBase you'll need docker installed. Use:
~~~~
docker-compose up --build db
~~~~
The rest of the docker images are still a WIP.

4. You can use the startup.sh file to run the application. It accepts an argument and this will be the env, by default the env is **local_dev**. Use:
~~~~
. startup.sh
~~~~
or (for example env **prod**):
~~~~
. startup.sh prod
~~~~