# 🤝 Contributing to Hebrew Overtime Tracker

## 🎯 Project Overview
This is a native Android version of the Hebrew Overtime Tracker web application, designed for Israeli workplace overtime management.

## 🏗️ Architecture
This project uses **proven working configuration** that succeeded after 17+ failed attempts:
- Traditional `buildscript{}` syntax (not modern `plugins{}`)
- Method-based Android configuration (`compileSdkVersion`)
- Minimal dependencies strategy
- System theme compatibility

## 🧪 Testing & Validation
Before submitting changes:

1. **Run validation script:**
   ```bash
   ./scripts/test-and-validate.sh
   ```

2. **Run debug & fix script:**
   ```bash
   ./scripts/debug-and-fix.sh
   ```

3. **Verify Hebrew support:**
   - Check Hebrew text rendering
   - Test RTL layout behavior
   - Validate calendar integration

## 🚀 Build Process
- **Local builds:** May fail due to Android SDK requirements in Termux
- **CI/CD builds:** Use GitHub Actions (Android SDK pre-installed)
- **Success pattern:** Follows proven working configuration

## 📱 Key Features to Maintain
- Hebrew/RTL interface
- Israeli calendar integration
- SQLite database compatibility
- Proven CI/CD configuration

## 🔧 Development Guidelines
1. Maintain minimal dependencies
2. Use system themes (not custom)
3. Follow Hebrew/RTL best practices
4. Test on actual Android devices
5. Keep configuration proven patterns

## 📊 Success Metrics
- ✅ GitHub Actions build passes
- ✅ APK generates successfully
- ✅ Hebrew text renders correctly
- ✅ App installs and runs on devices
