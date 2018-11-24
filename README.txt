La configuration de l'accès à la base de données se fait dans la DaoFactory (ligne 29) et mettre les paramètres de la base de données locales
il faudra aussi modifier le multi-part du web.xml (ligne 24) :
<multipart-config>
      <location>G:/Développement/openclassroom/subtitlor/Subtitlor/temp</location> <------ à changer
Il y a un fichier SQL à exécuter pour initialiser la base de données : Subtitlor\resources\create database.sql
Dès que la base est créé, lancer ResetDatabase.java en tant qu'application java pour reset/alimenter la base de données.
Des fichiers de tests sont à disposition pour l'upload des fichiers sous Subtitlor\test files
repo git si besoin : https://github.com/ACTIVthcr/java-ee-subtitor

Bonne correction.

