# Le Site - Application Android

Application Android avec WebView pour accéder à votre réseau local.

## 🚀 Compilation automatique

L'application est compilée automatiquement via **GitHub Actions** à chaque push.

### 📥 Télécharger l'APK

1. Allez dans l'onglet **Actions** sur GitHub
2. Cliquez sur le dernier workflow réussi (✅)
3. Scrollez en bas → Section **Artifacts**
4. Téléchargez **le-site-app-debug**
5. Décompressez le ZIP et installez l'APK

### 🔧 Configuration

Par défaut, l'application charge : `http://192.168.11.1`

Pour changer l'URL :
1. Éditez `app/src/main/res/values/strings.xml`
2. Modifiez la ligne : `<string name="website_url">VOTRE_URL</string>`
3. Commit et push
4. GitHub Actions recompilera automatiquement

### 📱 Installation

1. Téléchargez l'APK depuis Actions
2. Sur Android : Paramètres → Sécurité → Autoriser les sources inconnues
3. Installez l'APK

### 🛠️ Fonctionnalités

- ✅ WebView plein écran
- ✅ Pull-to-refresh
- ✅ Gestion des erreurs réseau
- ✅ Bouton retour intelligent
- ✅ Mode immersif
- ✅ Cache activé
- ✅ JavaScript activé

### 📦 Développement local

```bash
# Cloner le repo
git clone https://github.com/VOTRE_USERNAME/le-site-app.git
cd le-site-app

# Ouvrir avec Android Studio
# Ou compiler en ligne de commande :
./gradlew assembleDebug

# L'APK sera dans : app/build/outputs/apk/debug/
```

### 🔄 Mise à jour

```bash
# Modifier le code
git add .
git commit -m "Update"
git push

# GitHub Actions compile automatiquement
```

---

**Powered by GitHub Actions** 🚀