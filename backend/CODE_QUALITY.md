# Backend Code Quality

This backend project includes comprehensive code quality checks to ensure high standards of code maintainability, security, and performance.

## Code Quality Tools

### 1. **JaCoCo - Code Coverage**
- **Purpose**: Measures test coverage
- **Minimum Coverage**: 50% line coverage
- **Reports**: Generated in `target/site/jacoco/`
- **Configuration**: Built-in rules in pom.xml

### 2. **Checkstyle - Code Style**
- **Purpose**: Enforces coding standards
- **Rules**: Google Java Style Guide
- **Configuration**: Uses `google_checks.xml`
- **Scope**: Main and test sources

### 3. **SpotBugs - Static Analysis**
- **Purpose**: Finds potential bugs and code smells
- **Effort**: Maximum analysis depth
- **Threshold**: Medium severity and above
- **Configuration**: 
  - `spotbugs-exclude.xml` - Excludes false positives
  - `spotbugs-include.xml` - Specific includes

### 4. **PMD - Static Analysis**
- **Purpose**: Detects code problems and design issues
- **Rules**: Best practices, code style, design, error-prone, performance, security
- **Features**: Also includes Copy-Paste Detection (CPD)
- **Minimum Tokens**: 100 (for duplicate detection)

### 5. **OWASP Dependency Check**
- **Purpose**: Identifies known vulnerabilities in dependencies
- **Threshold**: CVSS score ≥ 7.0 fails the build
- **Configuration**: `dependency-check-suppressions.xml`
- **Reports**: Generated in `target/dependency-check-report.html`

### 6. **Maven Dependency Plugin**
- **Purpose**: Analyzes dependency usage
- **Checks**: Unused declared dependencies, used undeclared dependencies

### 7. **Maven Enforcer Plugin**
- **Purpose**: Enforces build environment rules
- **Rules**:
  - Minimum Maven version: 3.6.0
  - Java version: 17
  - No duplicate dependency versions
  - Upper bound dependency conflicts

## Build Commands

### Normal build (with all quality checks):
```bash
mvn clean install
```

### Skip all code quality checks (for quick builds):
```bash
mvn clean install -DskipCodeQuality
```

### Run only specific checks:
```bash
# Only run tests and coverage
mvn clean test

# Only run static analysis
mvn clean validate

# Only run dependency check
mvn dependency-check:check
```

### Generate detailed reports:
```bash
mvn clean install -Pfull-code-quality
mvn site
```

## Profile Usage

### Development Profile
```bash
mvn clean install -Pdev
```

### Production Profile (default)
```bash
mvn clean install -Pprod
```

### Skip Code Quality Profile
```bash
mvn clean install -Pskip-code-quality
```

### Full Code Quality with Reports
```bash
mvn clean install -Pfull-code-quality
```

## Configuration Files

- **spotbugs-exclude.xml**: SpotBugs exclusion rules
- **spotbugs-include.xml**: SpotBugs inclusion rules  
- **dependency-check-suppressions.xml**: OWASP dependency check suppressions

## Reports Location

After running the build, reports are generated in:
- **JaCoCo**: `target/site/jacoco/index.html`
- **Checkstyle**: `target/checkstyle-result.xml`
- **SpotBugs**: `target/spotbugsXml.xml`
- **PMD**: `target/pmd.xml` and `target/cpd.xml`
- **Dependency Check**: `target/dependency-check-report.html`

## Troubleshooting

### If Checkstyle fails:
1. Review the console output for style violations
2. Consider using IDE plugins for automatic formatting
3. Temporarily disable: `-Dcheckstyle.skip=true`

### If SpotBugs fails:
1. Review the SpotBugs report
2. Add exclusions to `spotbugs-exclude.xml` if needed
3. Temporarily disable: `-Dspotbugs.skip=true`

### If PMD fails:
1. Review PMD violations in console output
2. Refactor code to address issues
3. Temporarily disable: `-Dpmd.skip=true`

### If OWASP Dependency Check fails:
1. Review the dependency report
2. Update vulnerable dependencies
3. Add suppressions to `dependency-check-suppressions.xml` for false positives
4. Temporarily disable: `-Ddependency-check.skip=true`

## Continuous Integration

For CI/CD pipelines, consider:
- Running full quality checks on main/master branch
- Running lighter checks on feature branches
- Generating and publishing reports
- Failing builds on critical security vulnerabilities

## IDE Integration

Consider installing IDE plugins for:
- Checkstyle
- SpotBugs
- PMD
- SonarLint (for additional analysis)

This helps catch issues during development before they reach the build pipeline.
