Jakub Wysocki

Estratègia emprada:

Servidor:
Inici del Servidor: S'estableix un ServerSocket en un port específic i s'espera la connexió dels clients.
Gestió de Clients: Quan un client es connecta, el servidor accepta la connexió i crea un nou fil (Thread) per gestionar les sol·licituds d'aquell client.
Comunicació Bidireccional: S'utilitzen DataInputStream i DataOutputStream per rebre i enviar dades al client.
Gestió de Sol·licituds del Client: Les sol·licituds del client es gestionen a través d'un bucle while en un fil de client dedicat, on es processen les diferents opcions (llestes, obtenció d'informació de llibres, afegir i eliminar llibres).
Operacions de Base de Dades: Les operacions CRUD (Crear, Llegir, Actualitzar, Eliminar) es realitzen en una base de dades de llibres (BooksDB) segons les sol·licituds del client.
Gestió d'Errors: Es gestionen els errors per garantir la fiabilitat i robustesa en la comunicació amb el client.

Client:
Connexió amb el Servidor: S'estableix una connexió amb el servidor utilitzant un Socket en una adreça i port específics.
Interacció amb l'Usuari: S'ofereix un menú interactiu a l'usuari per triar les diferents opcions (llestes, obtenció d'informació de llibres, afegir i eliminar llibres).
Comunicació amb el Servidor: Les opcions triades per l'usuari s'envien al servidor mitjançant DataOutputStream.
Recepció de Respostes: El client rep les respostes del servidor mitjançant DataInputStream i les mostra a l'usuari (llestes de títols de llibres, informació de llibres, confirmació d'afegir i eliminar llibres).
Gestió d'Errors: Es gestionen els errors per garantir la fiabilitat i robustesa en la comunicació amb el servidor.
