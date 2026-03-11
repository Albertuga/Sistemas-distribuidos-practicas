# Sistemas-distribuidos-practicas
Practicas universitarias de la asignatura Sistemas Distribuidos

## instrucciones
1) compilamos con maven #mvn clean compile (Esto nos genera la carpeta target)
2) en un terminal tenemos que iniciar el servidor. Para ello:
java -cp target/classes es.ubu.lsi.server.ChatServerImpl
3) una vez iniciado el servidor tendremos que iniciar los clientes que queramos.
java -cp target/classes es.ubu.lsi.client.ChatClientImpl [nombre_cliente]
