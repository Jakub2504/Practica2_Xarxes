Jakub Wysocki

La estratègia utilitzada per implementar aquests dos programes és la següent:

Els programes són una implementació de la comunicació client-servidor mitjançant sockets TCP/IP en Java. La estratègia principal ha estat utilitzar threads d'execució per a gestionar la comunicació simultània en ambdues direccions, permetent que el servidor i el client enviïn i rebin missatges de manera independent.

En el codi del servidor, s'obre un ServerSocket a un port específic i s'espera la connexió del client. Un cop establerta la connexió, es creen dos threads d'execució separats: un per llegir els missatges del client i un altre per llegir els missatges des de la consola i enviar-los al client. A més, s'ha implementat un hook de shutdown per gestionar la finalització correcta del servidor quan es rep la senyal SIGINT (Ctrl+C).

D'altra banda, en el codi del client, es crea un Socket per connectar-se al servidor en una adreça i port específics. També s'implementen dos threads d'execució separats: un per llegir els missatges del servidor i un altre per llegir els missatges des de la consola i enviar-los al servidor. De manera similar al servidor, s'utilitza un hook de shutdown per garantir la finalització adequada del client quan es rep la senyal SIGINT.

En ambdós programes s'ha tingut cura de filtrar i evitar l'enviament i la visualització de missatges buits, i s'ha implementat la funcionalitat per finalitzar la conversa quan es rep el missatge "FI". Així mateix, s'han gestionat els errors de manera adequada per proporcionar una millor robustesa i fiabilitat en la comunicació entre el servidor i el client.

"En la creació digital, servidor i client entrellacen camins, teixint un llegat en el vast univers digital."