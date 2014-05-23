1º Edit the paths inside cliente.policy, servidor.policy, run_client.sh and
   run_server.sh

2º Copy these files into build/classes folder and start rmiregistry in the
   following way: "rmiregistry 9090 &"
   (NOTE: The console must be in the build/classes folder)

3º Run the server: sh run_server.sh

4º Start as many clients as you want with: sh run_client.sh

5º Chat!