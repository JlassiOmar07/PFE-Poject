@echo off
REM Configuration pour IntelliJ - Ouvrir rapport Cucumber
REM Ce script peut être utilisé comme External Tool

echo Ouverture du rapport Cucumber...

REM Vérifier si le fichier existe
if exist "target\cucumber-report.html" (
    echo Fichier trouvé : target\cucumber-report.html
    
    REM Ouvrir avec le navigateur par défaut
    start "" "target\cucumber-report.html"
    
    echo Rapport ouvert avec succès !
) else (
    echo ERREUR : Fichier target\cucumber-report.html non trouvé
    echo Assurez-vous d'avoir exécuté les tests d'abord avec : mvn test
    pause
)

exit /b 0
