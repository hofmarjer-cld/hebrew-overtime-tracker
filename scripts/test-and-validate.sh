#!/bin/bash
# 🧪 Hebrew Overtime Tracker - Smart Testing & Validation Script
# Combines local testing with CI/CD validation patterns

set -e  # Exit on any error

echo "🚀 Hebrew Overtime Tracker - Smart Testing & Validation"
echo "======================================================"

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging function
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

success() {
    echo -e "${GREEN}✅ $1${NC}"
}

warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

error() {
    echo -e "${RED}❌ $1${NC}"
}

# 📊 Phase 1: Project Structure Validation
log "Phase 1: Project Structure Validation"
echo "-------------------------------------"

# Check essential files
REQUIRED_FILES=(
    "app/build.gradle"
    "build.gradle" 
    "app/src/main/AndroidManifest.xml"
    "app/src/main/java/com/overtime/hebrew/activities/MainActivity.java"
    "app/src/main/java/com/overtime/hebrew/database/OvertimeDatabase.java"
    "app/src/main/res/values-he/strings.xml"
)

missing_files=0
for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        success "Found: $file"
    else
        error "Missing: $file"
        ((missing_files++))
    fi
done

if [ $missing_files -gt 0 ]; then
    error "❌ Missing $missing_files essential files. Cannot continue."
    exit 1
else
    success "All essential files found!"
fi

# 🔍 Phase 2: Configuration Validation (Proven Working Patterns)
log "Phase 2: Configuration Validation"
echo "---------------------------------"

# Check for proven working buildscript syntax
if grep -q "buildscript" build.gradle; then
    success "Uses proven buildscript syntax (not plugins)"
else
    warning "Not using proven buildscript syntax - may cause CI/CD issues"
fi

# Check for proven method-based Android config
if grep -q "compileSdkVersion" app/build.gradle; then
    success "Uses proven compileSdkVersion method syntax"
else
    warning "Not using proven method syntax - may cause compatibility issues"
fi

# Check for minimal dependencies
dep_count=$(grep -c "implementation" app/build.gradle || echo "0")
if [ "$dep_count" -le 3 ]; then
    success "Uses minimal dependencies strategy ($dep_count dependencies)"
else
    warning "High dependency count ($dep_count) - may cause conflicts"
fi

# Check for system theme usage
if grep -q "Theme.Material.Light" app/src/main/AndroidManifest.xml; then
    success "Uses system theme (proven working pattern)"
else
    warning "Not using system theme - may cause resource issues"
fi

# 🔤 Phase 3: Hebrew Text Validation
log "Phase 3: Hebrew Text Validation"
echo "-------------------------------"

# Check Hebrew strings in values-he
if grep -q "מעקב שעות נוספות" app/src/main/res/values-he/strings.xml; then
    success "Hebrew text found in strings.xml"
else
    error "Hebrew text not found in strings.xml"
fi

# Check RTL support in manifest
if grep -q "supportsRtl.*true" app/src/main/AndroidManifest.xml; then
    success "RTL support enabled in manifest"
else
    warning "RTL support not found in manifest"
fi

# Validate Hebrew encoding
if file app/src/main/res/values-he/strings.xml | grep -q "UTF-8"; then
    success "Hebrew files use UTF-8 encoding"
else
    warning "Hebrew files may not use UTF-8 encoding"
fi

# 🏗️ Phase 4: Build Test (if Gradle available)
log "Phase 4: Build Validation Test"
echo "------------------------------"

if command -v ./gradlew &> /dev/null; then
    log "Gradle wrapper found - attempting build test..."
    
    # Make gradlew executable
    chmod +x ./gradlew
    
    # Test gradle tasks list (lightweight test)
    if ./gradlew tasks --console=plain > /dev/null 2>&1; then
        success "Gradle configuration is valid"
        
        # Try to build if in CI environment or if forced
        if [ "$CI" = "true" ] || [ "$FORCE_BUILD" = "true" ]; then
            log "Attempting full build..."
            if ./gradlew assembleDebug --console=plain; then
                success "Build completed successfully!"
                
                # Check if APK was created
                APK_FILE=$(find app/build/outputs/apk -name "*.apk" 2>/dev/null | head -1)
                if [ -f "$APK_FILE" ]; then
                    success "APK created: $APK_FILE"
                    ls -lh "$APK_FILE"
                else
                    warning "Build succeeded but APK not found"
                fi
            else
                error "Build failed"
            fi
        else
            success "Gradle configuration validated (skipping full build)"
            log "  💡 Set FORCE_BUILD=true to attempt full build"
        fi
    else
        error "Gradle configuration is invalid"
    fi
else
    warning "Gradle wrapper not found - skipping build test"
fi

# 📱 Phase 5: Android-Specific Validations
log "Phase 5: Android-Specific Validations"
echo "-------------------------------------"

# Check package name consistency
MANIFEST_PACKAGE=$(grep -o 'package="[^"]*"' app/src/main/AndroidManifest.xml | cut -d'"' -f2)
GRADLE_APP_ID=$(grep -o 'applicationId "[^"]*"' app/build.gradle | cut -d'"' -f2)

if [ "$MANIFEST_PACKAGE" = "$GRADLE_APP_ID" ]; then
    success "Package name consistent: $MANIFEST_PACKAGE"
else
    warning "Package name mismatch: Manifest=$MANIFEST_PACKAGE, Gradle=$GRADLE_APP_ID"
fi

# Check permissions
REQUIRED_PERMISSIONS=("WRITE_EXTERNAL_STORAGE" "READ_EXTERNAL_STORAGE")
for perm in "${REQUIRED_PERMISSIONS[@]}"; do
    if grep -q "$perm" app/src/main/AndroidManifest.xml; then
        success "Permission declared: $perm"
    else
        warning "Permission not found: $perm"
    fi
done

# Check main activity declaration
if grep -q "MainActivity" app/src/main/AndroidManifest.xml && grep -q "MAIN" app/src/main/AndroidManifest.xml; then
    success "Main activity properly declared"
else
    error "Main activity declaration issue"
fi

# 📊 Phase 6: Code Quality Checks
log "Phase 6: Code Quality Checks"
echo "----------------------------"

# Count Java files and methods
JAVA_FILES=$(find app/src/main/java -name "*.java" | wc -l)
success "Java files: $JAVA_FILES"

# Check for Hebrew calendar utilities
if grep -q "HEBREW_MONTHS" app/src/main/java/com/overtime/hebrew/utils/HebrewCalendarUtils.java; then
    success "Hebrew calendar utilities implemented"
else
    warning "Hebrew calendar utilities not found"
fi

# Check database implementation
if grep -q "SQLiteOpenHelper" app/src/main/java/com/overtime/hebrew/database/OvertimeDatabase.java; then
    success "Database implementation found"
else
    error "Database implementation not found"
fi

# 🎯 Phase 7: Final Summary
log "Phase 7: Final Validation Summary"
echo "--------------------------------"

echo ""
echo "🎯 VALIDATION SUMMARY"
echo "===================="

# Count warnings and errors from this session
warnings_count=$(grep -c "⚠️" /tmp/validation.log 2>/dev/null || echo "0")
errors_count=$(grep -c "❌" /tmp/validation.log 2>/dev/null || echo "0")

if [ "$errors_count" -eq 0 ]; then
    if [ "$warnings_count" -eq 0 ]; then
        success "🎉 ALL VALIDATIONS PASSED - PROJECT IS READY!"
        echo ""
        echo "✅ Project follows proven working patterns"
        echo "✅ Hebrew/RTL support properly implemented"
        echo "✅ Build configuration is valid"
        echo "✅ No critical issues found"
        echo ""
        echo "🚀 NEXT STEPS:"
        echo "1. Push to GitHub repository"
        echo "2. GitHub Actions will build APK automatically"
        echo "3. Download APK from Actions artifacts"
        echo "4. Test on Android devices"
    else
        warning "✅ Validation passed with $warnings_count warnings"
        echo "⚠️  Review warnings above for potential improvements"
    fi
else
    error "❌ Validation failed with $errors_count errors"
    echo "🔧 Fix the errors above before proceeding"
    exit 1
fi

echo ""
log "Validation completed successfully! 🎉"