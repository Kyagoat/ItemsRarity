# Textures de bordures pour les tooltips personnalisés

## Dimensions requises : 256x256 pixels

### Structure de la texture :
- **Coins** : 16x16 pixels chacun
  - Haut-gauche : (0,0) → (16,16)
  - Haut-droite : (240,0) → (256,16)
  - Bas-gauche : (0,240) → (16,256)
  - Bas-droite : (240,240) → (256,256)

- **Bordures** : 8 pixels de largeur
  - Haut : (16,0) → (240,8)
  - Bas : (16,248) → (240,256)
  - Gauche : (0,16) → (8,240)
  - Droite : (248,16) → (256,240)

### Fichiers à créer :
1. common_border.png - Bordure pour rareté COMMON (blanc/gris)
2. uncommon_border.png - Bordure pour rareté UNCOMMON (jaune)
3. rare_border.png - Bordure pour rareté RARE (cyan/aqua)
4. epic_border.png - Bordure pour rareté EPIC (violet)
5. legendary_border.png - Bordure pour rareté LEGENDARY (rouge)
6. mythic_border.png - Bordure pour rareté MYTHIC (couleur spéciale)

### Instructions :
- Utilisez des couleurs qui correspondent aux raretés Minecraft
- Ajoutez des ornements, motifs ou effets selon votre style
- Les bordures peuvent être répétées automatiquement selon la taille du tooltip
- Utilisez la transparence (PNG avec canal alpha) pour des effets avancés
