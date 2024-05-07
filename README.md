Jakub Wysocki

Aquest projecte consisteix en un sistema de gestió de biblioteca implementat mitjançant una arquitectura client-servidor. El servidor proporciona funcionalitats per gestionar una base de dades de llibres, mentre que el client permet als usuaris interactuar amb aquestes funcionalitats mitjançant una interfície de línia de comandes.

Funcionalitats del Servidor:

Inici del servidor: S'inicia un servidor TCP que escolta les connexions dels clients en un port específic.
Gestió de clients: Quan un client es connecta, el servidor crea un nou fil de gestió per atendre les seves peticions de manera concurrent.
Comunicació bidireccional: Utilitza DataInputStream i DataOutputStream per rebre i enviar dades al client.
Operacions de la base de dades: El servidor permet realitzar diverses operacions sobre la base de dades de llibres, incloent-hi llistar els títols dels llibres, obtenir informació sobre un llibre específic, afegir un nou llibre i eliminar un llibre existent.
Gestió d'errors: S'implementa una gestió d'errors per garantir la fiabilitat i robustesa en la comunicació amb el client.

Funcionalitats del Client:

Connexió amb el servidor: El client es connecta al servidor utilitzant l'adreça IP i el port especificats.
Interacció amb l'usuari: S'ofereix un menú interactiu a l'usuari per seleccionar les diferents operacions disponibles, com llistar els títols dels llibres, obtenir informació sobre un llibre, afegir un nou llibre i eliminar un llibre existent.
Comunicació amb el servidor: El client envia les opcions seleccionades al servidor mitjançant DataOutputStream.
Recepció de respostes: Les respostes del servidor es reben mitjançant DataInputStream i es mostren a l'usuari, incloent-hi llistes de títols de llibres, informació dels llibres, confirmacions d'operacions o missatges d'error si cal.
Gestió d'errors: S'informa adequadament a l'usuari en cas d'ocurrir errors durant la comunicació amb el servidor o en les operacions sol·licitades.
