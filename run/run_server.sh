#!/bin/bash
#rmiregistry &
java -cp . -Djava.rmi.server.codebase=file:/Path/To/Project/Folder/build/classes/ -Djava.rmi.server.hostname=localhost -Djava.security.policy=file:/Path/To/Project/Folder/build/classes/servidor.policy servidor/Servidor localhost 9090