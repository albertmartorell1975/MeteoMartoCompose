# Workflow Initializer Skill

Aquest és el component central de la **Llavor de Flux de Treball Foundation**. La seva funció és automatitzar el desplegament de la governança i la configuració inicial de qualsevol projecte Android o KMP.

## 🚀 Com començar

### 1. Instal·lació de la Llavor
Si encara no has instal·lat la fundació, executa aquesta comanda a l'arrel del teu projecte:
```bash
npx skills add albertmartorell/android-ai-workflow-foundation -y
```

### 2. Activació del Flux
Un cop instal·lat, obre el xat de l'agent a Android Studio i digues:
> "Activa la skill **workflow-initializer** per configurar el projecte."

## ⚙️ Què fa aquesta Skill?

### Fase 1: Desplegament de Governança
L'agent crearà automàticament els fitxers de control a l'arrel del projecte:
- `.agents/rules.md`: Normes de Prompt Engineering i proactivitat.
- `.agents/AGENTS.md`: Definició de rols d'IA basats en Clean Architecture.

### Fase 2: Diagnòstic del Stack (Personalització)
L'agent et preguntarà sobre la teva arquitectura per ajustar els rols:
- **Arquitectura**: MVI o MVVM.
- **DI**: Hilt, Koin o Native.
- **Dades**: Retrofit, Room, etc.
- **Platform**: Android o KMP.

### Fase 3: Baseline de Git
Si el projecte no té Git, l'agent:
1. Executarà `git init`.
2. Crearà les branques `main` i `develop`.
3. Farà el primer commit amb la governança desplegada.

## 🛡️ Principis Aplicats
- **Clean Architecture**: Manteniment de capes Domain, UseCase, Data i UI.
- **SOLID**: Responsabilitats úniques per a cada agent.
- **Human-in-the-loop**: L'IA mai pren decisions arquitectòniques o fa commits sense aprovació humana.
