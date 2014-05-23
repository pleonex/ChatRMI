#!/bin/bash
java -cp . -Djava.rmi.server.codebase=file:/Path/To/Project/Folder/build/classes/ -Djava.rmi.server.hostname=localhost -Djava.security.policy=/Path/To/Project/Folder/build/classes/cliente.policy cliente/InicioFrame
