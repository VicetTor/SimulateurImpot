# 📊 Simulateur d'Impôt sur le Revenu — Projet Reusiné

Ce projet Java propose un simulateur d’impôt sur le revenu, structuré et réusiné pour respecter les bonnes pratiques de lisibilité, modularité et robustesse. Il permet de calculer l’impôt net d’un foyer fiscal en fonction des revenus, de la situation familiale et du nombre de parts fiscales.

---

## 📁 Structure du projet

com.kerware.simulateurreusine  
│  
├── CalculAbattement.java            // Calcul de l’abattement fiscal  
├── CalculCEHR.java                  // Calcul de la contribution exceptionnelle sur les hauts revenus  
├── CalculDecote.java                // Calcul de la décote  
├── CalculerImpotBrut.java           // Calcul de l’impôt brut selon les tranches fiscales  
├── CalculPartFiscale.java           // Calcul du nombre de parts fiscales  
├── DonneesFiscales.java             // Représentation des données fiscales du foyer  
├── PlafondQuotientFamilial.java     // Application du plafonnement du quotient familial  
├── SimulateurReusine.java           // Orchestration des calculs et contrôles de cohérence  
└── SimulateurLauncher.java          // Classe d'exécution et de test de la simulation  


---

## 🎯 Fonctionnalités et exigences couvertes

Chaque exigence est traduite en une classe ou une méthode dédiée :

| 📌 Exigence                                       | 📦 Implémentation                  |
|:------------------------------------------------- |:----------------------------------|
| Calcul du revenu fiscal après abattement         | `CalculAbattement`                |
| Calcul du nombre de parts fiscales               | `CalculPartFiscale`               |
| Calcul de l’impôt brut par part                  | `CalculerImpotBrut`               |
| Calcul de l’impôt brut total                     | `SimulateurReusine`               |
| Application du plafonnement du quotient familial | `PlafondQuotientFamilial`         |
| Application de la décote éventuelle              | `CalculDecote`                    |
| Calcul de la Contribution Exceptionnelle (CEHR)  | `CalculCEHR`                      |
| Contrôle d’intégrité des données fiscales        | `verificationsExceptions()` dans `SimulateurReusine` |

Chaque fonctionnalité est isolée dans une classe indépendante afin de respecter le principe de responsabilité unique et de favoriser la maintenance.

---

## 🧪 Tests et contrôles intégrés

Avant tout calcul, la méthode `verificationsExceptions()` contrôle :
- Que les revenus sont non négatifs.
- Que le nombre d’enfants et d’enfants handicapés est cohérent.
- Que les situations familiales et options déclaratives sont compatibles (ex. : parent isolé ne peut pas être marié ou pacsé).
- Que le nombre d’enfants ne dépasse pas 7.
- Qu’aucun revenu pour un déclarant 2 si la personne est seule.

Ces contrôles garantissent la fiabilité des calculs et préviennent les incohérences.

Une classe de test est également présente afin de tester l'entièreté du code.

---

## 📈 Analyse statique et qualité du code

Le projet a été analysé pour :
- Respect des conventions de nommage et d’organisation.
- Application du principe **single responsibility** sur l’ensemble des classes métier.
- Écrire des tests sur un code, et couvrir les exigences
- S'approprier un code

---

## ✍️ Auteurs

Projet réalisé et documenté par **K'DUAL Louane & YVRAY RUFFINATTI Victor**.
