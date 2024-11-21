# Festio
# Plateforme de Gestion d'Événements

## 📜 Contexte et Objectifs

La **Plateforme de Gestion d'Événements** est une solution complète pour organiser, gérer et suivre les événements. Elle vise à simplifier la gestion des inscriptions, des paiements et des notifications tout en intégrant des fonctionnalités avancées pour les organisateurs et les participants.

### 🎯 Objectifs généraux
- Concevoir une application web robuste et intuitive.
- Offrir une expérience fluide pour les inscriptions et paiements.
- Mettre en œuvre des technologies modernes garantissant sécurité, fiabilité et extensibilité.

### ✨ Objectifs spécifiques
#### Fonctionnalités de base :
1. **Gestion des événements** : Création, modification et suppression d'événements (titre, description, date, lieu, capacité).
2. **Inscriptions des participants** : Suivi des participants et gestion des listes d’attente.
3. **Notifications automatiques** : Envoi d'emails (confirmation d'inscription, rappels).
4. **Paiements en ligne** : Intégration d'une passerelle de paiement simulée.

#### Fonctionnalités avancées :
1. Création de différents types d'événements (conférences, ateliers, soirées).
2. Gestion des rôles et des accès (organisateurs et participants).
3. Système de notation pour les événements.
4. Calendrier interactif pour visualiser les événements.

---

## 🛠️ Technologies utilisées

- **Backend** : Spring Boot, Spring Data JPA, Spring Security, Spring Mail.
- **Frontend** : Thymeleaf, Bootstrap ou Tailwind CSS.
- **Base de données** : PostgreSQL ou MySQL.
- **Paiements en ligne** : Stripe, PayPal ou simulation.
- **Tests** : JUnit pour les tests unitaires, Postman pour les tests d'API.

---

## 📐 Architecture de l’Application

### Modèle MVC
- **Modèle** : Entités JPA pour gérer la base de données.
- **Vue** : Pages dynamiques Thymeleaf.
- **Contrôleur** : Gestion des requêtes et des logiques métier via Spring MVC.

### RESTful API
- Fournit des endpoints pour intégrations tierces et clients futurs.

### Tests
- Tests unitaires pour les composants.
- Tests d'intégration pour valider les interactions entre modules.

---

## 🗂️ Déroulement du Projet

### 1. **Conception**
- Analyse des besoins et rédaction d'un cahier des charges.
- Modélisation des entités clés :
  - **Événement** : ID, titre, description, date, lieu, capacité.
  - **Participant** : ID, nom, email, événement_ID (relation avec Événement).
  - **Paiement** : ID, montant, méthode, statut, participant_ID.
- Création des diagrammes UML :
  - Diagramme de cas d'utilisation.
  - Diagramme de classes.
  - Diagramme de séquence.

### 2. **Développement**
#### Backend :
- Configuration des entités JPA et relations (One-to-Many, Many-to-One).
- Implémentation des services et contrôleurs REST.
- Configuration de Spring Security pour l'authentification par rôle.

#### Frontend :
- Création des pages pour la gestion des événements et des inscriptions.
- Mise en œuvre de notifications dynamiques (via JavaScript ou bibliothèque).

#### Paiements :
- Simulation de paiement pour la validation des transactions.

### 3. **Tests**
- Tests unitaires : Validation des services et des contrôleurs REST.
- Tests d'intégration : Vérification des interactions globales.
- Tests fonctionnels : Vérification de l'interface utilisateur.

---

## 📦 Livrables

1. **Rapport détaillé** :
   - Analyse des besoins, architecture, et choix technologiques.
   - Résultats des tests et captures d’écran des fonctionnalités principales.
2. **Code source** : Hébergé sur [GitHub](https://github.com) avec documentation.
3. **Démo fonctionnelle** : Plateforme déployée pour évaluation.

---

## 🏆 Critères d’évaluation

- **Conception et architecture** : Clarté et qualité des modèles.
- **Implémentation** : Respect des consignes et des fonctionnalités.
- **Interface utilisateur** : Ergonomie et design.
- **Qualité du code** : Standards de codage et couverture des tests unitaires.
- **Documentation** : Exhaustivité et clarté des explications.

---

## 🚀 Comment démarrer ?

### Prérequis
- Java 17 ou plus.
- Maven.
- MySQL.
- Node.js (pour le frontend si applicable).

### Installation
1. Clonez ce dépôt :
   ```bash
   git clone https://github.com/mustaphaAitigunaoun/Festio.git
