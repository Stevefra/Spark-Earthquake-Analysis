Spark Earthquake Analysis

 Tempi di calcolo su Dataproc
* **2 Workers**: 7 min 13 s (partizione 8)
* **3 Workers**: 5 min 29 s (partizione 12)
* **4 Workers**: 3 min 47 s (Configurazione ottimale con `repartition(16)`)

 Distribuzione su Google Cloud
Il progetto è stato interamente realizzato tramite la **Cloud Shell** della Google Cloud Platform.

Passaggi seguiti:
1. **Gestione del Cluster**: Creazione e monitoraggio dei cluster tramite comandi `gcloud dataproc clusters` (es: eliminazione di `mon-cluster-4w` dopo i test).
2. **Esecuzione del Codice**: Lo script Scala è stato eseguito direttamente sul cluster Dataproc nella regione `europe-west1`.
3. **Ottimizzazione**: L'uso di `repartition(16)` ha permesso di massimizzare il parallelismo sui 4 worker, riducendo il tempo di esecuzione a meno a 3min 47s

 Struttura del repository
* `src/main/scala/SparkEarthquakes.scala`: Codice sorgente dell'analisi.
* `build.sbt`: Configurazione delle dipendenze Spark. Spark-Earthquake-Analysis





📦 Metodologia di Generazione e Caricamento del Codice su IJ
Nel progetto, la trasformazione del codice Scala in un formato eseguibile per Google Cloud è avvenuta tramite la generazione di file binari nella cartella target.

1. Compilazione delle Classi
Invece di un unico archivio, il processo ha generato i file compilati individuali:

EarthquakeAnalysis.class: Il bytecode principale del programma.

EarthquakeAnalysis$.class: La classe corrispondente all'oggetto Singleton in Scala.

2. Workflow di Aggiornamento
Ogni volta che il codice veniva modificato per i test di performance (cambiando il numero di partizioni), la procedura era la seguente:

Ricompilazione: Generazione dei nuovi file .class aggiornati nella cartella target.

Packaging: Questi file venivano raggruppati in un file JAR (Java ARchive) per essere pronti all'uso.

Upload e Deployment: Il file JAR risultante veniva caricato sulla piattaforma Google Cloud tramite l'interfaccia web per essere eseguito come Job Dataproc.
