La configuration de l'acc�s � la base de donn�es se fait dans la DaoFactory (ligne 29) et mettre les param�tres de la base de donn�es locales
il faudra aussi modifier le multi-part du web.xml (ligne 24) :
<multipart-config>
      <location>G:/D�veloppement/openclassroom/subtitlor/Subtitlor/temp</location> <------ � changer
Il y a un fichier SQL � ex�cuter pour initialiser la base de donn�es : Subtitlor\resources\create database.sql
D�s que la base est cr��, lancer ResetDatabase.java en tant qu'application java pour reset/alimenter la base de donn�es.
Des fichiers de tests sont � disposition pour l'upload des fichiers sous Subtitlor\test files
repo git si besoin : https://github.com/ACTIVthcr/java-ee-subtitor

Bonne correction.

