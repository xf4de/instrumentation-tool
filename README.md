# Instrumentation Tool 
Live patching Java applications with bytecode Instrumentation

## Abstract
Vista la grande diffusione del linguaggio Java vi sono diversi casi di applicazioni non più mantenute, ma ancora utilizzate, con gravi problemi di sicurezza. La non disponibilità del codice sorgente rende difficoltosa la correzione di questi errori: si propone un tool che mediante il sistema di Instrumentation di Java permette di sovrascrivere durante l'esecuzione le parti vulnerabili di un'applicazione.
Il software presentato offre inoltre due possibilità aggiuntive: (1) abilitare il Security Manager di Java permettendo di limitare l’accesso al file system oppure specificare una policy di sicurezza personalizzata; (2) abilitare la modifica delle chiamate al costruttore di ObjectInputStream con una sua implementazione alternativa che permette di mitigare problemi legati a “unsafe deserialize”.
Il tool presenta una configurazione unificata che permette di abilitare e definire tutte e tre le operazioni.

## Build Instructions
Open the project with NetBeans and build it.
Navigate to *BuildFiles* in the project folder, then execute the script *build.sh* to build the agent, which will be located in the project root directory and will be named *agent.jar*.
To use the tool also copy the folders *agentassets* and *agentlibs*, located in the *BuildFiles* directory, with the agent to the new location.