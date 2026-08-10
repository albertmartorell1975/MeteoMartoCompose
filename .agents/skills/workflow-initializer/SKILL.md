---
name: workflow-initializer
description: Initializes a new Android or KMP project with the AI-assisted development workflow seed. It sets up the governance files and guides the initial customization of agents and skills.
metadata:
  author: Albert Martorell Garcia
  version: 1.0.0
  keywords:
  - setup
  - initialization
  - workflow-seed
  - project-kickoff
---
# Workflow Initializer Specialist

This skill provides a structured process for setting up the AI-assisted workflow in a new Android or Kotlin Multiplatform (KMP) project. It ensures that the project starts with a solid architectural foundation and a clear collaboration protocol between humans and agents.

## Overview

The `workflow-initializer` is the gateway to the "Seed" workflow. It transforms a standard project into an "AI-augmented" one by deploying the governance structure and tailoring it to the project's specific tech stack.

## Initialization Process

### PHASE 1: Deployment
1. **Directory Setup**: Ensure the `.agents/` and `.agents/skills/` directories exist in the project root.
2. **Core Governance**: Copy the `rules.md` (General Prompting Rules) and `AGENTS.md` (Role Definitions) templates to the `.agents/` directory.

### PHASE 2: Stack Discovery & Customization
The agent MUST ask the user about the project's specific technical choices:
- **Architecture**: MVI, MVVM, or other?
- **Dependency Injection**: Hilt, Koin, or Native?
- **Networking**: Retrofit, Ktor, or none?
- **Persistence**: Room, SQLDelight, or none?
- **Platform**: Android Only or KMP?

Based on these answers, the agent MUST:
1. **Update `AGENTS.md`**: Replace generic examples with the specific technologies chosen (e.g., replace `MVI/MVVM` with `MVI`).
2. **Install Service Skills**: Recommend and install the corresponding specialized skills using `npx skills add`.

### PHASE 3: Git Baseline
1. **Initialize Git**: If not already initialized, perform `git init`.
2. **Branching Model**: Set up the `develop` and `main` branches according to `git-governance`.
3. **Commit Governance**: Ensure the user is aware of the commit naming conventions.

## Actionable Checklist for New Projects
- [ ] Create `.agents/` directory.
- [ ] Copy `rules.md` and `AGENTS.md` templates.
- [ ] Perform **Stack Diagnosis** with the user.
- [ ] Customize Role Definitions in `AGENTS.md`.
- [ ] Install core skills: `workflow-feature`, `git-governance`, `to-plan`.
- [ ] Install stack-specific skills (e.g., `hilt`, `mvi`, `firebase`).
- [ ] Run `git status` to verify the baseline.

## Documentation & References
- Refer to [AGENTS.md](../../AGENTS.md) for role details.
- Refer to [rules.md](../../rules.md) for prompting standards.
