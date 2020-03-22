### Struttura del file di policy
La configurazione è rappresentata da un oggetto **JSON** con al più tre campi, il campo
`rules` che rappresenta le regole di sostituzione o di modifica del codice, il capo `security`
che invece riguarda la parte del tool che si occupa del *Security Manager* di *Java*, e il
campo `serialkiller`, che riguarda la funzione di mitigazione dei problemi di *unsafe
deserialize*.

Il campo `rules` è un array di oggetti, ognuno dei quali rappresenta una regola. Ogni regola
ha all’interno due campi, `classname` che contiene sotto forma di stringa il nome della
classe sulla quale si vuole agire e il campo `actions` che è un array di oggetti azione. Il
campo `classname` può essere impostato a “” per estendere le azioni a tutte le classi.

Un oggetto azione è formato anch’esso da vari campi, tra i quali il campo type che
definisce l’azione che si vuole effettuare, può essere:
- `METHOD-REPLACE`, sostituzione dell’intero corpo di un metodo.
- `METHOD-INSERT-BEFORE`, inserimento di codice prima dell’esecuzione di un
metodo.
- `METHOD-INSERT-AFTER`, inserimento di codice dopo l’esecuzione di un metodo.
- `METHOD-INSIDE-CALL`, intercettazione della chiamata di un metodo e sostituzione
della stessa con altro codice.
- `METHOD-INSIDE-NEW`, intercettazione della creazione di un nuovo oggetto tramite
new e sostituzione con altro codice.
- `FIELD-REPLACE`, sostituzione di un campo di una classe.
Oltre al campo type un oggetto azione può contenere vari campi in base al tipo:
- `METHOD-INSIDE-CALL`:
  - `method-name`, che indica il nome del metodo che contiene la chiamata (si può
impostare a “”).
  - `classname`, che indica il nome della classe del metodo chiamato.
  - `method-call-name`, che indica il nome del metodo chiamato internamente che
si vuole sostituire.
  - `code`, che contiene il codice della modifica.
- `METHOD-INSIDE-NEW`:
  - `method-name`, che indica il nome del metodo che contiene la chiamata (si può
impostare a “”).
  - `classname`, che indica il nome della classe dell’oggetto che si sta creando.
  - `code`, che contiene il codice della modifica.
- `METHOD-REPLACE` e `FIELD-REPLACE`:
  - `name`, che indica il nome del metodo o del campo che si vuole sostituire
  - `code`, che contiene il codice della modifica (deve contenere anche il tipo e
attributi nel caso di `FIELD-REPLACE`).
- `METHOD-INSERT-BEFORE` e `METHOD-INSERT-AFTER`:
  - `name`, che indica il nome del metodo a cui si vuole aggiungere codice
  - `code`, che contiene il codice della modifica.

Il campo `security`, invece è un oggetto che contiene al più due campi, `type` e `code` o
`path`:
Il campo `type` è una stringa che indica che tipo di Security Manager si vuole installare,
può essere di due tipi:
- `default`, per impostare la sicurezza soltanto sui file: in questo modo viene
impostata una policy di sicurezza che restringe soltanto l’accesso, la modifica, la
scrittura e l'eliminazione dei file al di fuori della cartella di esecuzione (working
directory) e la cartella temporanea (tempdir) e permette tutte le altre azioni.
- `custom`, per impostare una policy personalizzata fornita al tool tramite stringa
oppure tramite file attraverso un campo aggiuntivo chiamato rispettivamente `code`
o `path`.

Il campo `serialkiller` è un oggetto contenente due campi: `class` e `method`, che
indicano rispettivamente il nome della classe e del metodo su cui viene effettuata la
chiamata al costruttore di `ObjectInputStream` che si vuole sostituire con `SerialKiller`.
Entrambi questi campi possono essere impostati a stringa vuota (“”) per estendere la
modifica a tutto il codice.

### Scrittura del codice delle patch e hook di javassist
Per ogni funzione di modifica l’utente deve scrivere il codice della modifica che vuole
effettuare. Il codice di modifica deve essere scritto nel linguaggio di programmazione
Java, se contiene più statement o espressioni deve essere contenuto in un blocco (tra
parentesi graffe) e ogni statement o espressione deve essere concluso con un punto e
virgola. È sempre consigliato scrivere il codice sotto forma di blocco anche nel caso in cui
contenga una sola espressione.
Le espressioni del blocco possono fare riferimento ai campi o ai metodi; è possibile fare
riferimento anche ai parametri se il codice è stato compilato con l’opzione `-g` altrimenti
per accedervi è necessario usare delle variabili speciali che verranno di seguito descritte.

Siccome il codice fornito dall’utente è compilato usando il compilatore di **javassist**
vi sono una serie di variabili precedute da $ che hanno un significato speciale. Le più
interessanti sono:
- `$0,$1,$2,...` rappresentano this e i parametri attuali.
- `$args`, rappresenta un array di parametri, il tipo è Object[]. Da notare che $args[0]
non è equivalente a $0 perché $0 rappresenta this.
- `$$,` rappresenta tutti i parametri attuali, usati nella chiamata originaria (anche
nessuno).
- `$r`, rappresenta il tipo del valore di ritorno, usato in un’espressione di type cast
- `$_`, rappresenta il valore di ritorno
Altre sono:
- `$w`, rappresenta il tipo wrapper, usato in un’espressione di type cast
- `$sig`, rappresenta un array di oggetti java.lang.Class che rappresentano il tipo dei
parametri formali
- `$type`, rappresenta il tipo formale del valore di ritorno
- `$class`, rappresenta la classe che si sta modificando al momento.

Tutte queste sono utilizzabili sulle funzioni di sostituzione del corpo di un metodo,
inserimento di codice prima e dopo un metodo e intercettazione e modifica di chiamate a
metodo o costruttore.
Nel caso delle funzioni di intercettazione chiamata a metodo o a costruttore visto che
viene sempre effettuata una sostituzione di codice è possibile usare la variabile speciale
`$proceed`, che rappresenta il nome del metodo chiamato originariamente; dunque
`$proceed($$)` rappresenta la chiamata al metodo che si sta sostituendo. E’ perciòpossibile fare modifiche ai parametri e chiamare il metodo con `$$` che rappresenta tutti i
parametri, oppure è possibile chiamare il metodo con meno parametri o scambiandone
l’ordine usando le variabili `$1, $2,..` . Nel caso in cui si voglia eseguire codice prima e
dopo una chiamata a metodo si può usare un codice di sostituzione simile al seguente:
```
{ before-statements;
$_ = $proceed($$);
after-statements;​ }
```
Dove before-statements e after-statements rappresentano uno o più statement che possono
interagire con le variabili speciali sopracitate.

### Example
```
{
    rules: [
        {
            classname: "TestClass",
            actions: [
                {
                    type: "METHOD-REPLACE",
                    name: "test",
                    code: "{System.out.println(\“Replaced\”);}"
                },
                {
                    type: "METHOD-INSIDE-NEW",
                    method-name: "callingMethod",
                    classname: "OtherClass",
                    code: "{$1=”test”; $_=$proceed($$);}"
                }
            ]
        }

    ],
    security: 
    {
        type: "custom",
        code: "grant { permission java.security.AllPermission; };"
    },
    serialkiller: 
    {
        class: "DeserializationClass",
        method: ""
    }
}
```
